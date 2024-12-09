package com.example.API_Cinema.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table
@Data
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    @Column(length = 1000)
    private String description;
    @Column(length = 1000)
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdDate;
    private String image;
    private String status;
    private String type;
}
