package com.example.API_Cinema.repo;

import com.example.API_Cinema.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, Integer> {
    @Query("SELECT t FROM Ticket t WHERE t.schedule.id = :scheduleId AND t.seat.id = :seatId")
    List<Ticket> findTicketsByScheduleIdAndSeatId(Integer scheduleId, Integer seatId);

    @Query("SELECT t FROM Ticket t WHERE t.schedule.id = :scheduleId")
    List<Ticket> findTicketsByScheduleId(Integer scheduleId);
}
