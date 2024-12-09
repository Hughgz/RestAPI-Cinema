package com.example.API_Cinema.repository;

import com.example.API_Cinema.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepo extends JpaRepository<Seat, Integer> {
    @Query("select seat from Seat seat where seat.room.id = :roomId")
    List<Seat> findSeatByRoomId(@Param("roomId") int roomId);
}
