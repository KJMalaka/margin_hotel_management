package za.ac.cput.marginhotelmanagement.dtos;
/*
   Author: Katlego Malaka (230443370)
   Date: 25 August 2026
   */

import lombok.Data;
import za.ac.cput.marginhotelmanagement.enums.BookingChannel;

import java.time.LocalDateTime;

@Data
public class UpdateBookingRequest {
    private Long bookingId;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private BookingChannel bookingChannel;
}
