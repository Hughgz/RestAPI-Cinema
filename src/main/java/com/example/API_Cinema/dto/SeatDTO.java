package com.example.API_Cinema.dto;

import com.example.API_Cinema.model.Room;
import lombok.Data;

@Data
public class SeatDTO {
    private int id;
    private String name;
    private int colSeat;
    private int rowSeat;
    private String seatType;
    private int isOccupied;
    private RoomDTO room;
}
