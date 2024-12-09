package com.example.API_Cinema.service.impl;

import com.example.API_Cinema.dto.SeatDTO;
import com.example.API_Cinema.model.Room;
import com.example.API_Cinema.model.Seat;
import com.example.API_Cinema.repository.RoomRepo;
import com.example.API_Cinema.repository.ScheduleRepo;
import com.example.API_Cinema.repository.SeatRepo;
import com.example.API_Cinema.repository.TicketRepo;
import com.example.API_Cinema.service.ISeatService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService implements ISeatService {
    private final SeatRepo seatRepo;
    private final ScheduleRepo scheduleRepo;
    private final TicketRepo ticketRepo;
    private final RoomRepo roomRepo;

    public SeatService(SeatRepo seatRepo, ScheduleRepo scheduleRepo, TicketRepo ticketRepo, RoomRepo roomRepo) {
        this.seatRepo = seatRepo;
        this.scheduleRepo = scheduleRepo;
        this.ticketRepo = ticketRepo;
        this.roomRepo = roomRepo;
    }


    @Override
    public Seat insert(SeatDTO dto) {
        Seat seat = new ModelMapper().map(dto, Seat.class);
        roomRepo.findById(seat.getRoom().getId()).orElseThrow(() -> new RuntimeException("Room does not exits"));
        return seatRepo.save(seat);
    }

    @Override
    public List<Seat> insertMultipleSeat(SeatDTO dto) {

        return null;
    }

    @Override
    public SeatDTO update(SeatDTO dto) {
        Seat currentSeat = seatRepo.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Seat does not exits"));
        if(currentSeat != null){
            currentSeat.setName(dto.getName());
            seatRepo.save(currentSeat);
            return convert(currentSeat);
        }
        return null;
    }

    @Override
    public void delete(int seatId) {
        seatRepo.deleteById(seatId);
    }

    @Override
    public SeatDTO getSeatById(int seatId) {
        Seat seat = seatRepo.findById(seatId).orElseThrow(() -> new RuntimeException("Seat does not exits"));
        return convert(seat);
    }


    @Override
    public List<SeatDTO> getAll() {
        List<Seat> listSeat = seatRepo.findAll();
        return listSeat.stream().map(seat -> convert(seat)).collect(Collectors.toList());
    }

    @Override
    public List<SeatDTO> getSeatByScheduleId(int scheduleID) {
        //Lấy ra phòng trong lịch phim
        Room room = scheduleRepo
                .findById(scheduleID)
                .orElseThrow(() -> new RuntimeException("Room does not exits"))
                .getRoom();
        //Lấy ra chổ ngồi trong phòng đó
        List<Seat> listSeat = seatRepo.findSeatByRoomId(room.getId());

        //Lấy ra vé đã được đặt rồi map sang các chổ ngồi
        List<Seat> occupiedSeats = ticketRepo
                .findTicketsByScheduleId(scheduleID)
                .stream().map(ticket -> ticket.getSeat())
                .collect(Collectors.toList());
        //Map danh sách chổ ngồi(listSeat) sang dto
        List<SeatDTO> filteredSeat = listSeat.stream().map(seat -> {
            SeatDTO seatDTO = convert(seat);
            if(occupiedSeats.stream().map(occupiedSeat -> occupiedSeat.getId()).collect(Collectors.toList()).contains(seat.getId())){
                seatDTO.setIsOccupied(1);
            }
            return seatDTO;
        }).collect(Collectors.toList());

        return filteredSeat;
    }

    @Override
    public SeatDTO convert(Seat seat) {
        return new ModelMapper().map(seat, SeatDTO.class);
    }
}
