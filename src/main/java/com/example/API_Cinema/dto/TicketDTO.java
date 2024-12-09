package com.example.API_Cinema.dto;

import com.example.API_Cinema.model.Bill;
import com.example.API_Cinema.model.Schedule;
import com.example.API_Cinema.model.Seat;
import lombok.Data;

@Data
public class TicketDTO {
    private int id;
    private double totalAmount;
    private SeatDTO seat;
    private ScheduleDTO schedule;
    private BillDTO bill;
}
