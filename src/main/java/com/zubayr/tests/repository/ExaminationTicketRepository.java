package com.zubayr.tests.repository;

import com.zubayr.tests.model.exam.ExaminationTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExaminationTicketRepository extends JpaRepository<ExaminationTicket, Long> {

    Optional<ExaminationTicket> findByName(String name);

}
