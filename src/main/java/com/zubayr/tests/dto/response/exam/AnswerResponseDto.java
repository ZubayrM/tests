package com.zubayr.tests.dto.response.exam;

import lombok.Data;

@Data
public class AnswerResponseDto {

    private String answer;

    private Long questionId;

    private Long ticketId;
}
