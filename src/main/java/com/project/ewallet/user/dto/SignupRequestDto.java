package com.project.ewallet.user.dto;

import lombok.Data;

@Data
public class SignupRequestDto {
    private String name;
    private String email;
    private String mobileNumber;
    private String username;
    private String password;
}
