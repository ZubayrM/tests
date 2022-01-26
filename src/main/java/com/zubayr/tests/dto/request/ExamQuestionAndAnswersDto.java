package com.zubayr.tests.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamQuestionAndAnswersDto {

    private String question;

    private List<String> answers;

    private Long questionId;

    private Long ticketId;;

}
