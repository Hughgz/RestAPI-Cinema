package com.example.API_Cinema.repository;

import com.example.API_Cinema.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, Integer> {
    @Query("SELECT t FROM Ticket t WHERE t.schedule.id = :scheduleId AND t.seat.id = :seatId")
    List<Ticket> findTicketsByScheduleIdAndSeatId(Integer scheduleId, Integer seatId);

    @Query("SELECT t FROM Ticket t WHERE t.schedule.id = :scheduleId")
    List<Ticket> findTicketsByScheduleId(Integer scheduleId);

    @Query("SELECT t FROM Ticket t WHERE t.bill.id IN (" +
            "SELECT b.id FROM Bill b WHERE b.user.id = :userId)")
    List<Ticket> findTicketByUserId(@Param("userId") int userId);
    @Query("SELECT SUM(t.totalAmount)" +
            "FROM Ticket t\n" +
            "         JOIN Bill b ON t.bill.id = b.id\n" +
            "WHERE b.user.id = :userId\n")
    Double totalAmountByUserId(@Param("userId") int userId);
}
