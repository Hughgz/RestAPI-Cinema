package com.example.API_Cinema.service;

import com.example.API_Cinema.dto.TicketDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.model.Ticket;

import java.util.List;

public interface ITicketService {
    TicketDTO getTicketById(int id) throws DataNotFoundException;
    List<TicketDTO> getTicketByUserId(int userId);

    Double getTotalAmountByUserId(int userId);
}
