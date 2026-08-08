package com.project.ewallet.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtClaimsDto {
    private Long userId;
    private String username;
    private String email;
}
