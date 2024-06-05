package com.example.API_Cinema.apis;


import com.example.API_Cinema.dto.ScheduleDTO;
import com.example.API_Cinema.dto.UserDTO;
import com.example.API_Cinema.model.Schedule;
import com.example.API_Cinema.service.impl.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleApi {
    @Autowired
    private ScheduleService service;

    @PostMapping("/new")
    public ResponseEntity<?> createSchedule(@Valid @RequestBody ScheduleDTO dto, BindingResult result){
        try{
            if(result.hasErrors()){
                List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorMessage);
            }
            service.insert(dto);
            return ResponseEntity.status(201).body("Create schedule successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody ScheduleDTO dto, BindingResult result){
        try{
            if(result.hasErrors()){
                List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorMessage);
            }
            ScheduleDTO scheduleDTO = service.update(dto);
            return ResponseEntity.status(200).body(scheduleDTO);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("scheduleID") int scheduleID){
        try {
            service.delete(scheduleID);
            return ResponseEntity.ok(String.format("Schedule with id = %d deleted successfully", scheduleID));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("")
    public ResponseEntity<List<ScheduleDTO>> getAll(){
        List<ScheduleDTO> scheduleDTOS = service.getAll();
        return ResponseEntity.status(200).body(scheduleDTOS);
    }

    @GetMapping("/find-startTime")
    public ResponseEntity<?> getStartTime(@RequestParam("movieId") int movieId,
                                          @RequestParam("branchId") int branchId,
                                          @RequestParam("startDate") LocalDate startDate)
    {
        try{
            List<String> startTimeList = service.getStartTimeByMovie_IdAndBranch_IdAndStartDate(movieId, branchId, startDate);
            if(startTimeList.isEmpty()){
                return ResponseEntity.badRequest().body("The film's premiere time could not be found");
            }
            return ResponseEntity.status(200).body(startTimeList);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/find-schedule")
    public ResponseEntity<?> getSchedule(@RequestParam("movieId") int movieId,
                                          @RequestParam("branchId") int branchId,
                                          @RequestParam("startDate") LocalDate startDate,
                                          @RequestParam("startTime") LocalTime startTime,
                                          @RequestParam("roomId") int roomId)
    {
        try{
            List<ScheduleDTO> scheduleList = service
                    .getSchedulesByMovieIdAndBranchIdAndStartDateAndStartTimeAndRoomId(movieId, branchId, startDate, startTime, roomId);
            if(scheduleList.isEmpty()){
                return ResponseEntity.badRequest().body("The schedule could not be found");
            }
            return ResponseEntity.status(200).body(scheduleList);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
