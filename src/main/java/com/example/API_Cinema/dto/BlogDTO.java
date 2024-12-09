package com.example.API_Cinema.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BlogDTO {
    private int id;
    private String title;
    private String description;
    private String content;
    private LocalDate createdDate;
    private String image;
    private String status;
    private String type;
}
