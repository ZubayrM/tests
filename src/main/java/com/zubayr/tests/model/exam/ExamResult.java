package com.zubayr.tests.model.exam;

import com.zubayr.tests.model.enums.StatusExam;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "exam_result")
public class ExamResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "ticket_id")
    private Long examinationTicketId;

    @Column(name = "max_size_answers")
    private Integer maxSizeAnswers;

    @Column(name = "size_answers")
    private Integer sizeAnswers;

    @Column(name = "date")
    private Date dateStartTest;

    @Column(name = "status_exam")
    @Enumerated(EnumType.STRING)
    private StatusExam statusExam;

    @Column(name = "position")
    private Integer position;

    public void actualAnswer(){
        if (this.sizeAnswers == null) sizeAnswers = 0;
        if (this.position == null) position = 1;
        this.position++;
        this.sizeAnswers++;
    }

    public void notActualAnswer(){
        if (this.position == null) position = 1;
        this.position++;
    }
}
