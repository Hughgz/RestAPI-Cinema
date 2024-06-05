package com.example.API_Cinema.dto;

import com.example.API_Cinema.model.Ticket;
import com.example.API_Cinema.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BillDTO {
    private int id;
    private LocalDateTime createdDate;
    private List<TicketDTO> listTicket; //danh sách vé đặt
    private User user;
}
