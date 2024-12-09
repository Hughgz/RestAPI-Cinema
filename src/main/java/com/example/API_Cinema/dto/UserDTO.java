package com.example.API_Cinema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class UserDTO {
    @JsonProperty("user_id")
    private int id;
    private String fullName;
    @Size(min = 10, max = 10, message = "Phone must 10 characters")
    private String phone;
    private String email;
    private LocalDate birthdate;
    private String sex;
    private String area;
    private String image;
    private String password;
    @JsonProperty("role_name")
    private String roleName;
    @JsonProperty("role_id")
    private int roleId;
}
