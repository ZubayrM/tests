package com.zubayr.tests.dto.request;

import lombok.Data;

@Data
public class RegisUserDto {

    private String surname;

    private String name;

    private String patronymic;

    private String role;

    private String login;

    private String password;

}
