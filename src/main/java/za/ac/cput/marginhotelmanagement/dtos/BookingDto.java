package za.ac.cput.marginhotelmanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import za.ac.cput.marginhotelmanagement.enums.BookingChannel;

import java.time.LocalDate;
import java.time.LocalDateTime;

/*

   Author: Katlego Malaka (230443370)
   Date:25 August 2026
   */
@AllArgsConstructor
@Getter
public class BookingDto {
    private Long bookingId;
    private LocalDate bookingDate;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private BookingChannel bookingChannel;
    private Long guestId;
    private Long roomId;
    private int roomNumber;
}
