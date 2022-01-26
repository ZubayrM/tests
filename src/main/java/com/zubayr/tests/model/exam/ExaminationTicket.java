package com.zubayr.tests.model.exam;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "examination_ticket")
public class ExaminationTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;


}
