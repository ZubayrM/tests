package com.zubayr.tests.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class ResultTestDto {

    private String nameTicket;

    private Set<UserAndDateAndResultExamDto> users;
}
