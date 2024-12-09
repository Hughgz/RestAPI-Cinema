package com.example.API_Cinema.apis;


import com.example.API_Cinema.dto.TicketDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.service.impl.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ticket")
public class TicketApi {
    private final TicketService service;

    public TicketApi(TicketService service) {
        this.service = service;
    }

    @GetMapping("/getTicketByUserId/{userId}")
    public ResponseEntity<?> getTicketByUserId(@PathVariable int userId){
        List<TicketDTO> tickets = service.getTicketByUserId(userId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/getTicketById/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable int id) throws DataNotFoundException {
        TicketDTO ticketDTO = service.getTicketById(id);
        return ResponseEntity.ok(ticketDTO);
    }

    @GetMapping("/totalAmountByUserId/{id}")
    public ResponseEntity<?> getTotalAmountByUserId(@PathVariable int id) {
        Double ticketDTO = service.getTotalAmountByUserId(id);
        return ResponseEntity.ok(ticketDTO);
    }
}
