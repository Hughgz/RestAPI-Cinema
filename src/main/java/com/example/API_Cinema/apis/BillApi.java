package com.example.API_Cinema.apis;

import com.example.API_Cinema.dto.BookingDTO;
import com.example.API_Cinema.service.impl.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/bill")
public class BillApi {

    private final BillService service;

    public BillApi(BillService service) {
        this.service = service;
    }

    @PostMapping("/create-bill")
    public ResponseEntity<?> createBill(@RequestBody BookingDTO dto){
        try{
            service.createBill(dto);
        }catch (Exception e){
            System.err.println("Error creating bill: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.status(200).body("Create bill successfully");
    }
}
