package com.zubayr.tests.model.exam;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "exam_answer")
public class ExamAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "answer")
    private String answer;

    @Column(name = "question_id")
    private Long questingId;

    @Column(name = "actual")
    private Boolean actual;

}
