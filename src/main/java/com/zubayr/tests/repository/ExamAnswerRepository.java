package com.zubayr.tests.repository;

import com.zubayr.tests.model.exam.ExamAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamAnswerRepository extends JpaRepository<ExamAnswer, Long> {
    Optional<List<ExamAnswer>> findByQuestingId(Long id);

    Optional<ExamAnswer> findByAnswer(String answer);

}
