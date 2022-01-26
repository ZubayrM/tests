package com.zubayr.tests.dto.request;

import lombok.Data;

@Data
public class UserInfoDto {

    private Long id;

    private String fullName;

    private String role;

    private String status;
}
