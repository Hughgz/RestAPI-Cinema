package com.example.API_Cinema.service;

import com.example.API_Cinema.dto.RoomDTO;
import com.example.API_Cinema.model.Room;

import java.util.List;

public interface IRoomService {
    List<RoomDTO> getAll();
    RoomDTO convert(Room room);
}
