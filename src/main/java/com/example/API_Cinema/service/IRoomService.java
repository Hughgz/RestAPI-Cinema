package com.example.API_Cinema.service;

import com.example.API_Cinema.dto.RoomDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.model.Room;

import java.util.List;

public interface IRoomService {
    void insertRoom(RoomDTO dto);
    RoomDTO updateRoom(RoomDTO dto);
    RoomDTO findById(int id) throws DataNotFoundException;

    List<RoomDTO> findByRoomOnBranchId(int branchId);
    List<RoomDTO> getAll();
    RoomDTO convert(Room room);
}
