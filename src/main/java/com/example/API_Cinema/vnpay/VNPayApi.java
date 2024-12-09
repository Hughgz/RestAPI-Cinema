package com.example.API_Cinema.vnpay;

import com.example.API_Cinema.response.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class VNPayApi {
    private final VNPayService vnPayService;
    @PostMapping("/submitOrder")
    public ResponseEntity<Map<String, String>> submitOrder(@RequestParam("amount") int  orderTotal,
                                                           @RequestParam("orderInfo") String orderInfo,
                                                           HttpServletRequest request) {
        // Lấy URL thanh toán VNPay
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);

        // Tạo response map chứa URL thanh toán
        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", vnpayUrl);

        // Trả về URL thanh toán dưới dạng JSON
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vnpay-payment")
    public ResponseEntity<Map<String, Object>> handleVnPayPayment(HttpServletRequest request) {
        // Kiểm tra trạng thái thanh toán từ VNPay
        int paymentStatus = vnPayService.orderReturn(request);
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        String bankCode = request.getParameter("vnp_BankCode");
        String transactionStatus = request.getParameter("vnp_TransactionStatus");

        // Kiểm tra trạng thái thanh toán để quyết định success hay fail
        String successStatus = paymentStatus == 1 ? "success" : "fail";

        // Tạo redirect URL
        String redirectUrl = "http://localhost:2000/ket-qua-dat-ve?" +
                "success=" + successStatus +
                "&orderId=" + orderInfo +
                "&transactionId=" + transactionId +
                "&vnp_Amount=" + totalPrice +
                "&vnp_BankCode=" + bankCode +
                "&vnp_TransactionStatus=" + transactionStatus +
                "&paymentTime=" + paymentTime;

        // Trả về dưới dạng JSON với redirect URL
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, redirectUrl)
                .build();
    }

}
