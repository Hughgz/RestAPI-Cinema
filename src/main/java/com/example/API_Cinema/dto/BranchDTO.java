package com.example.API_Cinema.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BranchDTO {
    private int id;
    @NotBlank(message = "Name branch is required")
    @Size(min = 3, max = 200, message = "Name must be between 3 and 200 characters")
    private String name;
    private String imgURL;
    private String address;
    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Phone must be exactly 10 characters")
    private String phone;
}
