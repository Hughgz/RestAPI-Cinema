package com.example.API_Cinema.service.impl;

import com.example.API_Cinema.dto.BookingDTO;
import com.example.API_Cinema.model.Bill;
import com.example.API_Cinema.model.Schedule;
import com.example.API_Cinema.model.Ticket;
import com.example.API_Cinema.model.User;
import com.example.API_Cinema.repository.*;
import com.example.API_Cinema.service.IBillService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BillService implements IBillService {
    private final ScheduleRepo scheduleRepo;
    private final TicketRepo ticketRepo;
    private final UserRepo userRepo;
    private final SeatRepo seatRepo;
    private final BillRepo billRepo;


    public BillService(ScheduleRepo scheduleRepo, TicketRepo ticketRepo, UserRepo userRepo, SeatRepo seatRepo, BillRepo billRepo) {
        this.scheduleRepo = scheduleRepo;
        this.ticketRepo = ticketRepo;
        this.userRepo = userRepo;
        this.seatRepo = seatRepo;
        this.billRepo = billRepo;
    }

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

            Ticket ticket = new Ticket();
            ticket.setSeat(seatRepo.findById(seatId).orElseThrow(() -> new RuntimeException("Seat not found")));
            ticket.setSchedule(schedule);
            ticket.setTotalAmount(bookingDTO.getTotalAmount());
            ticket.setBill(saveBill);

            ticket.setPaymentMethod("VNPay");
            ticketRepo.save(ticket);
        });
    }
}
