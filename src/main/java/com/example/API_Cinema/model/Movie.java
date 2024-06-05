package com.example.API_Cinema.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(length = 1000)
    private String smallImgMovie;
    @Column(length = 1000)
    private String largeImgMovie;
    @Column(length = 500)
    private String shortDescription;
    @Column(length = 1500)
    private String longDescription;
    @Column(length = 50)
    private String director;// đạo diễn
    @Column(length = 50)
    private String actors;
    @Column(length = 100)//diễn viên
    private String categories;//thể loại
    private Date releaseDate; // ngày ra mắt
    private int duration; //sức chứa
    @Column(length = 1000)
    private String trailerURL;
    private String country;
    private String rated;
    private int showing;

}
