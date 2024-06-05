package com.example.API_Cinema.dto;

import com.example.API_Cinema.model.Branch;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RoomDTO {
    private int id;
    private String name;
    private int capacity;
    private double totalArea;
    private String imgURL;
    @JsonProperty("branch_id")
    private int branchId;
}