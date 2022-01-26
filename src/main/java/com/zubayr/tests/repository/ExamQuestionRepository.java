package com.zubayr.tests.repository;

import com.zubayr.tests.model.exam.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {

    Optional<ExamQuestion> findByExaminationTicketIdAndPosition(Long examId, Integer position);

    Optional<Integer> countByExaminationTicketId(Long tickedId);
}
