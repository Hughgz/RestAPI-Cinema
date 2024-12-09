package com.example.API_Cinema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RoomDTO {
    private int id;
    private String name;
    private int capacity;
    private int colNum;
    private int rowNum;
    @JsonProperty("branch_id")
    private int branchId;
}