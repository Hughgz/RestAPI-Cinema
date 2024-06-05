package com.example.API_Cinema.service.impl;

import com.example.API_Cinema.dto.BookingDTO;
import com.example.API_Cinema.model.Bill;
import com.example.API_Cinema.model.Schedule;
import com.example.API_Cinema.model.Ticket;
import com.example.API_Cinema.model.User;
import com.example.API_Cinema.repo.*;
import com.example.API_Cinema.service.IBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BillService implements IBillService {
    @Autowired
    private ScheduleRepo scheduleRepo;
    @Autowired
    private TicketRepo ticketRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SeatRepo seatRepo;
    @Autowired
    private BillRepo billRepo;
    @Override
    public void createBill(BookingDTO bookingDTO) {
        //Lấy ra lịch chiếu phim
        Schedule schedule = scheduleRepo
                .findById(bookingDTO.getScheduleID())
                .orElseThrow(() -> new RuntimeException("Schedule does not exits"));

        //Lấy ra người dùng
        User user = userRepo
                .findById(bookingDTO.getUserID())
                .orElseThrow(() -> new RuntimeException("User does not exits"));

        //tạo bill mới và lưu thông tin user xuống trước
        Bill createNewBill = new Bill();
        createNewBill.setUser(user);
        createNewBill.setCreatedDate(LocalDateTime.now());

        Bill saveBill = billRepo.save(createNewBill);

        //với mỗi seat, check xem đã đặt chưa, nếu đặt rồi thì throw exception
        //nếu chưa đặt lưu thông tin ghế và lịch vào ticket

        bookingDTO.getListSeatID().forEach(seatId -> {
            //Nếu đã có người đặt rồi thì văng exception
            if(!ticketRepo.findTicketsByScheduleIdAndSeatId(schedule.getId(), seatId).isEmpty()){
                throw new RuntimeException("Already booked, please choose another seat");
            }
            ticketRepo.findById(seatId).orElseThrow(() -> new RuntimeException("Seat does not exits"));
            Ticket ticket = new Ticket();
            ticket.setSeat(seatRepo.findById(seatId).orElse(null));
            ticket.setSchedule(schedule);
            ticket.setBill(saveBill);
            ticket.setQrImgUrl("");
            ticketRepo.save(ticket);
        });
    }
}
