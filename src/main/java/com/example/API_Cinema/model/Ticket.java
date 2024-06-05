package com.example.API_Cinema.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 500)
    private String qrImgUrl;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Bill bill;


}
