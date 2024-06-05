package com.example.API_Cinema.dto;


import lombok.Data;

import java.util.List;

@Data
public class BookingDTO { //Lưu những người đặt vé
    private int userID;
    private int scheduleID;
    private List<Integer> listSeatID;
}
