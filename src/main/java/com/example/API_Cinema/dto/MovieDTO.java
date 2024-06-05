package com.example.API_Cinema.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class MovieDTO {
    @NotNull(message = "Movie ID cannot be null")
    private int id;

    @NotBlank(message = "Name cannot be blank")
    private String name;
    private String smallImgMovie;
    private String largeImgMovie;
    @NotBlank(message = "Short description cannot be blank")
    @Size(max = 500, message = "Short description must be less than or equal to 500 characters")
    private String shortDescription;

    @NotBlank(message = "Long description cannot be blank")
    @Size(max = 1500, message = "Long description must be less than or equal to 1500 characters")
    private String longDescription;

    @NotBlank(message = "Director cannot be blank")
    @Size(max = 50, message = "Director must be less than or equal to 50 characters")
    private String director;

    @NotBlank(message = "Actors cannot be blank")
    @Size(max = 50, message = "Actors must be less than or equal to 50 characters")
    private String actors;

    @NotBlank(message = "Categories cannot be blank")
    @Size(max = 100, message = "Categories must be less than or equal to 100 characters")
    private String categories;

    @NotNull(message = "Release date cannot be null")
    private Date releaseDate;

    @NotNull(message = "Duration cannot be null")
    private int duration;

    @NotBlank(message = "Trailer URL cannot be blank")
    @Size(max = 1000, message = "Trailer URL must be less than or equal to 1000 characters")
    private String trailerURL;

    @NotBlank(message = "Country cannot be blank")
    private String country;

    private String rated;

    @NotNull(message = "Showing count cannot be null")
    private int showing;
}
