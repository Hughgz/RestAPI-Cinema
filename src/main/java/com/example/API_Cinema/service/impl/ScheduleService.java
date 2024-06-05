package com.example.API_Cinema.service.impl;

import com.example.API_Cinema.dto.ScheduleDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.model.Branch;
import com.example.API_Cinema.model.Movie;
import com.example.API_Cinema.model.Room;
import com.example.API_Cinema.model.Schedule;
import com.example.API_Cinema.repo.BranchRepo;
import com.example.API_Cinema.repo.MovieRepo;
import com.example.API_Cinema.repo.RoomRepo;
import com.example.API_Cinema.repo.ScheduleRepo;
import com.example.API_Cinema.service.IScheduleService;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService implements IScheduleService {
    @Autowired
    private ScheduleRepo repo;
    @Autowired
    private MovieRepo movieRepo;
    @Autowired
    private BranchRepo branchRepo;
    @Autowired
    private RoomRepo roomRepo;
    @Override
    public void insert(ScheduleDTO dto) throws DataNotFoundException {
        Schedule schedule = new ModelMapper().map(dto, Schedule.class);
        branchRepo.findById(dto.getBranchID())
                .orElseThrow(() -> new DataNotFoundException("Branch with ID " + dto.getBranchID() + " does not exist"));

        roomRepo.findById(dto.getRoomID())
                .orElseThrow(() -> new DataNotFoundException("Room with ID " + dto.getRoomID() + " does not exist"));
        movieRepo.findById(dto.getMovieID())
                .orElseThrow(() -> new DataNotFoundException("Movie with ID " + dto.getMovieID() + " does not exist"));
        repo.save(schedule);
    }

    @Override
    public ScheduleDTO update(ScheduleDTO dto) {
        Schedule currentSchedule = repo.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Schedule does not exits"));
        if(currentSchedule != null){
            currentSchedule.setStartDate(dto.getStartDate());
            currentSchedule.setStartTime(dto.getStartTime());
            currentSchedule.setPrice(dto.getPrice());

            repo.save(currentSchedule);
            return convert(currentSchedule);
        }
        return null;
    }

    @Override
    public void delete(int id) {
        repo.deleteById(id);
    }

    @Override
    public List<ScheduleDTO> getAll() {
        List<Schedule> schedules = repo.findAll();
        return schedules.stream().map(schedule -> convert(schedule)).collect(Collectors.toList());
    }

    @Override
    public List<String> getStartTimeByMovie_IdAndBranch_IdAndStartDate(int movieID, int branchID, LocalDate startDate) {
        return repo.getStartTimeByMovieIdAndBranchIdAndStartDate(movieID, branchID, startDate)
                .stream().map(localTime -> localTime.format(DateTimeFormatter.ofPattern("HH:mm")))
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDTO> getSchedulesByMovieIdAndBranchIdAndStartDateAndStartTimeAndRoomId(int movieId, int branchId, LocalDate startDate, LocalTime startTime, int roomId) {
        List<Schedule> schedules = repo.getSchedulesByMovieIdAndBranchIdAndStartDateAndStartTimeAndRoomId(movieId, branchId, startDate, startTime, roomId);
        return schedules.stream().map(schedule -> convert(schedule)).collect(Collectors.toList());
    }

    @Override
    public ScheduleDTO convert(Schedule schedule) {
        return new ModelMapper().map(schedule, ScheduleDTO.class);
    }
}
