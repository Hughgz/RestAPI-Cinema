package com.example.API_Cinema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Data
public class FoodDTO {
    private int id;
    private String name;
    private double costPrice;
    private double price;
    private double profit;
    private int stock;
    private String imgURL;
    private String content;
    private String status;
}
