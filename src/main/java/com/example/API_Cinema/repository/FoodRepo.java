package com.example.API_Cinema.repository;


import com.example.API_Cinema.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepo extends JpaRepository<Food, Integer> {
}
