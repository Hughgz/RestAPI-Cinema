package com.example.API_Cinema.service.impl;


import com.example.API_Cinema.dto.FoodDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.model.Food;
import com.example.API_Cinema.repository.FoodRepo;
import com.example.API_Cinema.response.CloudinaryResponse;
import com.example.API_Cinema.service.IFoodService;
import com.example.API_Cinema.utils.FileUploadUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodService implements IFoodService {
    private final FoodRepo repository;
    private final CloudinaryService cloudinaryService;
    public FoodService(FoodRepo repository, CloudinaryService cloudinaryService) {
        this.repository = repository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public void insertFood(FoodDTO dto) {
        Food food = new ModelMapper().map(dto, Food.class);
        repository.save(food);
    }

    @Override
    public FoodDTO updateFood(FoodDTO dto) {
        return null;
    }

    @Override
    public List<FoodDTO> getAll() {
        List<Food> foods = repository.findAll();
        return foods.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public FoodDTO getFoodById(int id) throws DataNotFoundException {
        Food food = repository.findById(id).orElseThrow(() -> new DataNotFoundException("Food not found"));
        return convert(food);
    }
    @Transactional
    public void uploadImage(final Integer id, final MultipartFile file) throws DataNotFoundException {
        final Food food = this.repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Food not found"));
        FileUploadUtils.assertAllowed(file, FileUploadUtils.IMAGE_PATTERN);
        final String fileName = FileUploadUtils.getFileName(file.getOriginalFilename());
        final CloudinaryResponse response = this.cloudinaryService.uploadFile(file, fileName);
        food.setImgURL(response.getUrl());
        this.repository.save(food);
    }
    @Override
    public FoodDTO convert(Food food) {
        return new ModelMapper().map(food, FoodDTO.class);
    }
}
