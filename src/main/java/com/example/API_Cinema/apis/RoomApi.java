package com.example.API_Cinema.apis;


import com.example.API_Cinema.dto.RoomDTO;
import com.example.API_Cinema.service.impl.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomApi {
    @Autowired
    RoomService service;

    @GetMapping("")
    public ResponseEntity<?> getAll(){
        List<RoomDTO> list = service.getAll();
        return ResponseEntity.status(200).body(list);
    }
}
