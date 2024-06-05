package com.example.API_Cinema.service;

import com.example.API_Cinema.dto.SeatDTO;
import com.example.API_Cinema.model.Seat;

import java.util.List;

public interface ISeatService {
    Seat insert(SeatDTO dto);
    SeatDTO update(SeatDTO dto);
    void delete(int seatId);
    SeatDTO findById(int seatId);
    List<SeatDTO> getAll();
    List<SeatDTO> getSeatByScheduleId(int scheduleID);

    SeatDTO convert(Seat seat);
}