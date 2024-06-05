package com.example.API_Cinema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    @JsonProperty("user_id")
    private int id;

    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @NotBlank(message = "Phone cannot be blank")
    @Size(min = 10, max = 10, message = "Phone must 10 characters")
    private String phone;

    @NotBlank(message = "Email cannot be blank")
    private String email;

    private Date birthdate;

    @NotBlank(message = "Sex cannot be blank")
    private String sex;

    @NotBlank(message = "Area cannot be blank")
    private String area;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotNull(message = "Role ID is required")
    @JsonProperty("role_id")
    private int roleId;
}
