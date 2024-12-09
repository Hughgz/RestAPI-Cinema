package com.example.API_Cinema.apis;

import com.example.API_Cinema.dto.FoodDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.service.impl.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodApi {
    private final FoodService service;

    public FoodApi(FoodService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> insertFood(@RequestBody FoodDTO foodDTO){
        service.insertFood(foodDTO);
        return ResponseEntity.status(201).body("Created food successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllFood() throws DataNotFoundException {
        List<FoodDTO> foods = service.getAll();
        if(foods.isEmpty()){
            return ResponseEntity.badRequest().body("Food is empty");
        }
        return ResponseEntity.ok(foods);
    }
    @PostMapping("/uploadImage/{id}")
    public ResponseEntity<?> uploadSmallImage(@PathVariable final Integer id, @RequestPart final MultipartFile file) throws DataNotFoundException {
        service.uploadImage(id, file);
        return ResponseEntity.ok("Upload successfully");
    }
}
