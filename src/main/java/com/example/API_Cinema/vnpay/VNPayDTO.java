package com.example.API_Cinema.vnpay;

import lombok.Builder;
public abstract class VNPayDTO {
    @Builder
    public static class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;
    }
}