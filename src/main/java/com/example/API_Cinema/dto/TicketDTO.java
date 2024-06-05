package com.example.API_Cinema.dto;

import com.example.API_Cinema.model.Bill;
import com.example.API_Cinema.model.Schedule;
import com.example.API_Cinema.model.Seat;
import lombok.Data;

@Data
public class TicketDTO {
    private int id;
    private String qrImgUrl;
    private Seat seat;
    private Schedule schedule;
    private Bill bill;
}
