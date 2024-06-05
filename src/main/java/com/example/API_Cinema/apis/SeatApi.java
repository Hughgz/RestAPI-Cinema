package com.example.API_Cinema.apis;

import com.example.API_Cinema.dto.SeatDTO;
import com.example.API_Cinema.service.impl.SeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seat")
public class SeatApi {
    @Autowired
    private SeatService service;

    @GetMapping("/getSeatByScheduleId")
    public ResponseEntity<?> getSeatByScheduleId(@Valid @RequestParam("scheduleId") int scheduleId){
        List<SeatDTO> listSeat = service.getSeatByScheduleId(scheduleId);
        if(listSeat.isEmpty()){
            return ResponseEntity.badRequest().body("Seat list in schedule with ID " + scheduleId +  " empty" + "\nPlease try again with another schedule");
        }
        return ResponseEntity.status(200).body(listSeat);
    }
    @PostMapping("/new")
    public ResponseEntity<?> createSeat(@Valid @RequestBody SeatDTO dto){
        service.insert(dto);
        return ResponseEntity.status(201).body("Create seat successfully");
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateSeat(@Valid @RequestBody SeatDTO dto){
        SeatDTO seatDTO = service.update(dto);
        return ResponseEntity.status(201).body(seatDTO);
    }
}
