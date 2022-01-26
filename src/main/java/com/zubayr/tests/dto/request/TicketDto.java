package com.zubayr.tests.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketDto {

    private String name;

    private Integer size;
}
