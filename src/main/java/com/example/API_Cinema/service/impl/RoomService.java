package com.example.API_Cinema.service.impl;

import com.example.API_Cinema.dto.RoomDTO;
import com.example.API_Cinema.model.Room;
import com.example.API_Cinema.repo.RoomRepo;
import com.example.API_Cinema.service.IRoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService implements IRoomService {
    @Autowired
    RoomRepo repository;


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
