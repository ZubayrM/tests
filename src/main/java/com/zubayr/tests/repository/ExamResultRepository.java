package com.zubayr.tests.repository;

import com.zubayr.tests.model.enums.StatusExam;
import com.zubayr.tests.model.exam.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {

    Optional<ExamResult> findFirstByUserIdAndDateStartTestAfterAndExaminationTicketIdOrderByDateStartTest(Long id, Date date, Long ticketId);

    List<ExamResult> findByExaminationTicketId(Long id);


}
