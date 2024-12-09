package com.example.API_Cinema.service.impl;


import com.example.API_Cinema.dto.TicketDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.model.Ticket;
import com.example.API_Cinema.repository.TicketRepo;
import com.example.API_Cinema.service.ITicketService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService implements ITicketService {
    private final TicketRepo repository;

    public TicketService(TicketRepo repository) {
        this.repository = repository;
    }

    @Override
    public TicketDTO getTicketById(int id) throws DataNotFoundException {
        Ticket ticket = repository.findById(id).orElseThrow(() -> new DataNotFoundException("Ticket not found"));
        return new ModelMapper().map(ticket, TicketDTO.class);
    }

    @Override
    public List<TicketDTO> getTicketByUserId(int userId) {
        List<Ticket> tickets = repository.findTicketByUserId(userId);
        return tickets.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public Double getTotalAmountByUserId(int userId) {
        return repository.totalAmountByUserId(userId);
    }

    public TicketDTO convert(Ticket ticket){
        return new ModelMapper().map(ticket, TicketDTO.class);
    }

}
