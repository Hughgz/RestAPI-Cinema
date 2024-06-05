package com.example.API_Cinema.dto;

import lombok.Data;

@Data
public class SeatDTO {
    private int id;
    private String name;
    private int isOccupied; //Ghế đã đặt
}
