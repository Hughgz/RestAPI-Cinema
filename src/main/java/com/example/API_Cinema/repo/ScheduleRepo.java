package com.example.API_Cinema.repo;

import com.example.API_Cinema.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRepo extends JpaRepository<Schedule, Integer> {
    @Query("SELECT s FROM Schedule s WHERE s.movie.id = :movieId AND s.branch.id = :branchId AND s.startDate = :startDate AND s.startTime = :startTime AND s.room.id = :roomId")
    List<Schedule> getSchedulesByMovieIdAndBranchIdAndStartDateAndStartTimeAndRoomId(
            @Param("movieId") int movieId,
            @Param("branchId") int branchId,
            @Param("startDate") LocalDate startDate,
            @Param("startTime") LocalTime startTime,
            @Param("roomId") int roomId
    );
    @Query("SELECT DISTINCT s.startTime FROM Schedule s WHERE s.movie.id=:movieId AND s.branch.id" +
            "= :branchId AND s.startDate=:startDate")
    List<LocalTime> getStartTimeByMovieIdAndBranchIdAndStartDate(@Param("movieId") Integer movieId
            , @Param("branchId") Integer branchId
            , @Param("startDate") LocalDate startDate);
}
