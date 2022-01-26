package com.zubayr.tests.model.exam;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "exam_question")
public class ExamQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question")
    private String question;

    @Column(name = "examination_ticket_id")
    private Long examinationTicketId;

    @Column(name = "position", unique = true)
    private Integer position;
}
