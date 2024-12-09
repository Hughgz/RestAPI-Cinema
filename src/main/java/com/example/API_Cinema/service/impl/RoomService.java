package com.example.API_Cinema.service.impl;

import com.example.API_Cinema.dto.RoomDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.model.Room;
import com.example.API_Cinema.repository.RoomRepo;
import com.example.API_Cinema.service.IRoomService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService implements IRoomService {
    private final RoomRepo repository;

    public RoomService(RoomRepo repository) {
        this.repository = repository;
    }

    @Override
    public void insertRoom(RoomDTO dto) {
        Room room = new ModelMapper().map(dto, Room.class);
        repository.save(room);
    }

    @Override
    public RoomDTO updateRoom(RoomDTO dto) {
        return null;
    }

    @Override
    public RoomDTO findById(int id) throws DataNotFoundException {
        Room room = repository.findById(id).orElseThrow(() -> new DataNotFoundException("Room not found"));
        return convert(room);
    }

    @Override
    public List<RoomDTO> findByRoomOnBranchId(int branchId) {
        return null;
    }

    @Override
    public List<RoomDTO> getAll() {
        List<Room> rooms = repository.findAll();
        return rooms.stream().map(room -> convert(room)).collect(Collectors.toList());
    }

    @Override
    public RoomDTO convert(Room room) {
        return new ModelMapper().map(room, RoomDTO.class);
    }
}
