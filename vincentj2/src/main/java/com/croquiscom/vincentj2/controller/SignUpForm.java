package com.croquiscom.vincentj2.controller;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SignUpForm {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String team;
}
