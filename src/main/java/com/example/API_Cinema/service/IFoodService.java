package com.example.API_Cinema.service;

import com.example.API_Cinema.dto.FoodDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.model.Food;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFoodService {
    void insertFood(FoodDTO dto);
    FoodDTO updateFood(FoodDTO dto);
    List<FoodDTO> getAll();
    FoodDTO getFoodById(int id) throws DataNotFoundException;
    void uploadImage(final Integer id, final MultipartFile file) throws DataNotFoundException;
    FoodDTO convert(Food food);

}
