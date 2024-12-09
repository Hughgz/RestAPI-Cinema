package com.example.API_Cinema.apis;


import com.example.API_Cinema.dto.RoomDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.service.impl.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomApi {
    private final RoomService service;
    public RoomApi(RoomService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<?> getAll(){
        List<RoomDTO> list = service.getAll();
        return ResponseEntity.status(200).body(list);
    }
    @PostMapping("/insert")
    public ResponseEntity<?> insertRoom( @Valid @RequestBody RoomDTO dto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errorMessages = bindingResult.getFieldErrors()
                    .stream().map(FieldError::getDefaultMessage)
                    .toList();

            return ResponseEntity.badRequest().body(errorMessages);
        }
        service.insertRoom(dto);
        return ResponseEntity.status(201).body("Created room successfully");
    }

    @GetMapping("/getRoomById/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable int id) throws DataNotFoundException {
        RoomDTO room = service.findById(id);
        return ResponseEntity.ok(room);
    }
}
