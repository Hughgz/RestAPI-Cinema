package com.example.API_Cinema.dto;

import com.example.API_Cinema.model.Branch;
import com.example.API_Cinema.model.Movie;
import com.example.API_Cinema.model.Room;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleDTO {
    private int id;
    private LocalDate startDate;
    @Column(unique = true)
    private LocalTime startTime;

    @NotNull(message = "Branch ID is required")
    @JsonProperty("branch_id")
    private int branchID;

    @NotNull(message = "Room ID is required")
    @JsonProperty("room_id")
    private int roomID;

    @NotNull(message = "Movie ID is required")
    @JsonProperty("movie_id")
    private int movieID;
}
