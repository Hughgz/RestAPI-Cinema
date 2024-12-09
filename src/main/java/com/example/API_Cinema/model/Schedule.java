package com.example.API_Cinema.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate startDate;
    private LocalTime startTime;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Room room;
}
