package com.example.API_Cinema.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Table
@Data
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private double costPrice;
    private double price;
    private double profit;
    private int stock;
    private String content;
    private String imgURL;
    private String status;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Bill bill;
}
