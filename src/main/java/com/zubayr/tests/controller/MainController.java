package com.zubayr.tests.controller;

import com.zubayr.tests.dto.request.ExamQuestionAndAnswersDto;
import com.zubayr.tests.dto.request.ResultTestDto;
import com.zubayr.tests.dto.request.TicketDto;
import com.zubayr.tests.dto.request.UserAndDateAndResultExamDto;
import com.zubayr.tests.dto.response.exam.AnswerResponseDto;
import com.zubayr.tests.exeption.TestException;
import com.zubayr.tests.model.User;
import com.zubayr.tests.model.enums.StatusExam;
import com.zubayr.tests.model.exam.ExamAnswer;
import com.zubayr.tests.model.exam.ExamQuestion;
import com.zubayr.tests.model.exam.ExamResult;
import com.zubayr.tests.model.exam.ExaminationTicket;
import com.zubayr.tests.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MainController {

    private final ExaminationTicketRepository examinationTicketRepository;
    private final UserRepository userRepository;
    private final ExamResultRepository examResultRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final ExamAnswerRepository examAnswerRepository;
    private final Logger logger;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");

    @Value("${milliseconds_between_tests}")
    private Long MILLISECONDS_BETWEEN_TESTS;

    @Autowired
    public MainController(ExaminationTicketRepository ticketRepository, UserRepository userRepository, ExamResultRepository examResultRepository, ExamQuestionRepository examQuestionRepository, ExamAnswerRepository examAnswerRepository) {
        this.examinationTicketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.examResultRepository = examResultRepository;
        this.examQuestionRepository = examQuestionRepository;
        this.examAnswerRepository = examAnswerRepository;

        logger = LoggerFactory.getLogger(MainController.class);
    }

    @PreAuthorize(value = "permitAll()")
    @GetMapping("/")
    public String home(Model model, Principal principal){
        model.addAttribute("auth", principal != null);
        return "home";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    @GetMapping("/testsResult")
    public String testsResult(Model model){
        List<ExaminationTicket> tickets = examinationTicketRepository.findAll();
        List<ResultTestDto> results = new ArrayList<>();
        tickets.forEach(e-> {
            List<ExamResult> examResults = examResultRepository.findByExaminationTicketId(e.getId());
            Set<UserAndDateAndResultExamDto> users = new HashSet<>();
            examResults.forEach(r->{
                UserAndDateAndResultExamDto userAndDataAndResultExamDto = new UserAndDateAndResultExamDto();
                userAndDataAndResultExamDto.setUsername(userRepository.findById(r.getUserId()).orElseThrow().getFullName());
                userAndDataAndResultExamDto.setDate(dateFormat.format(r.getDateStartTest()));
                userAndDataAndResultExamDto.setResult(100.0 / ((double) r.getMaxSizeAnswers()) * ((double) r.getSizeAnswers()) + "% правельных ответов");
                users.add(userAndDataAndResultExamDto);
            });
            ResultTestDto dto = new ResultTestDto();
            dto.setNameTicket(e.getName());
            dto.setUsers(users);
            results.add(dto);
        });
        model.addAttribute("results", results);
        return "testsResult";
    }

    @GetMapping("/tests")
    public String testsPage(Model model) {
        List<ExaminationTicket> all = examinationTicketRepository.findAll();
        List<TicketDto> ticketDtoList = new ArrayList<>();
        all.forEach(t-> {
            Integer size = examQuestionRepository.countByExaminationTicketId(t.getId()).orElse(0);
            ticketDtoList.add(new TicketDto(t.getName(), size));
        });
        model.addAttribute("tests", ticketDtoList);
        return "tests";
    }

    @PostMapping("/testing")
    public String postTesting(@ModelAttribute AnswerResponseDto dto, Principal principal, Model model){
        Optional<User> optionalUser = userRepository.findByUserName(principal.getName());
        Optional<ExaminationTicket> ticket = examinationTicketRepository.findById(dto.getTicketId());
        ExamResult examResult = getExamResult(optionalUser, ticket);
        ExamAnswer examAnswer = examAnswerRepository.findByAnswer(dto.getAnswer()).orElseThrow(TestException::new);
        if (examAnswer.getActual()){
            examResult.actualAnswer();
        } else {
            examResult.notActualAnswer();
        }
        examResultRepository.save(examResult);
        return testingPage(ticket.orElseThrow(TestException::new).getName(), principal, model);
    }

    @GetMapping("/testing")
    public String testingPage(@RequestParam String nameTest, Principal principal, Model model) throws TestException {
        Optional<User> optionalUser = userRepository.findByUserName(principal.getName());
        Optional<ExaminationTicket> ticket = examinationTicketRepository.findByName(nameTest);
        try {
            ExamResult eResult = getExamResult(optionalUser, ticket);
            if (eResult.getPosition().longValue() > eResult.getMaxSizeAnswers().longValue()) {
                eResult.setStatusExam(StatusExam.COMPLETED);
                examResultRepository.save(eResult);
                model.addAttribute("result", eResult.getSizeAnswers() +" из " + eResult.getMaxSizeAnswers());
                model.addAttribute("msg", "Следующее тестирование можно пройти через " + (MILLISECONDS_BETWEEN_TESTS / 60000L) + " минут");
                return "endTest";
            }
            ExamQuestion question = examQuestionRepository.findByExaminationTicketIdAndPosition(
                    eResult.getExaminationTicketId(),
                    eResult.getPosition()).orElseThrow(TestException::new);
            List<ExamAnswer> examAnswerList =
                    examAnswerRepository.findByQuestingId(question.getId()).orElseThrow(TestException::new);
            List<String> answerList = new ArrayList<>();
            examAnswerList.forEach(answer -> answerList.add(answer.getAnswer()));
            ExamQuestionAndAnswersDto dto =
                    new ExamQuestionAndAnswersDto(
                            question.getQuestion(),
                            answerList,
                            question.getId(),
                            ticket.orElseThrow(TestException::new).getId()

                    );
            model.addAttribute("test", dto);
            return "testing";
        } catch (TestException e){
            return "testing";
        }
    }

    private ExamResult getExamResult(Optional<User> optionalUser, Optional<ExaminationTicket> optionalTicket) {
        if (optionalUser.isPresent() && optionalTicket.isPresent()) {
            User user = optionalUser.get();
            ExaminationTicket ticket = optionalTicket.get();

            Optional<ExamResult> optionalExamResult = examResultRepository
                    .findFirstByUserIdAndDateStartTestAfterAndExaminationTicketIdOrderByDateStartTest(
                            user.getId(),
                            new Date(new Date().getTime()  - MILLISECONDS_BETWEEN_TESTS),
                            ticket.getId()
                    );

            if (optionalExamResult.isPresent()) {
                return optionalExamResult.get();
            } else {
                ExamResult examResult = new ExamResult();
                examResult.setPosition(1);
                examResult.setSizeAnswers(0);
                examResult.setUserId(user.getId());
                examResult.setExaminationTicketId(ticket.getId());
                examResult.setStatusExam(StatusExam.NOT_COMPLETED);
                examResult.setDateStartTest(new Date());
                examResult.setMaxSizeAnswers(
                        examQuestionRepository
                                .countByExaminationTicketId(ticket.getId()).orElse(0));
                return examResultRepository.save(examResult);
            }
        }else {
            throw new TestException();
        }
    }


    @GetMapping("/auth")
    public String authPage() {
        return "auth";
    }
}
