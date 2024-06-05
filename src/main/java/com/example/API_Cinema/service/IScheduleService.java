package com.example.API_Cinema.service;

import com.example.API_Cinema.dto.ScheduleDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.model.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IScheduleService {
    void insert(ScheduleDTO dto) throws DataNotFoundException;
    ScheduleDTO update(ScheduleDTO dto);
    void delete(int id);
    List<ScheduleDTO> getAll();
    List<String> getStartTimeByMovie_IdAndBranch_IdAndStartDate
            (int movieID, int branchID, LocalDate startDate);
    List<ScheduleDTO> getSchedulesByMovieIdAndBranchIdAndStartDateAndStartTimeAndRoomId
            (int movieId, int branchId
            , LocalDate startDate, LocalTime startTime, int roomId);

    ScheduleDTO convert(Schedule schedule);
}
