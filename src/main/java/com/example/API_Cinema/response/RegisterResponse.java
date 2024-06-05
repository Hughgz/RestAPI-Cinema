package com.example.API_Cinema.response;

import com.example.API_Cinema.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("user")
    private User user;
}
