package za.ac.cput.marginhotelmanagement.service;
/*
   Author: Katlego Malaka (230443370)
   Co-Author: Dumisane Madondo (230949703)
   Date: 09 July 2026
*/

import za.ac.cput.marginhotelmanagement.domain.Booking;

import java.util.List;
import java.time.LocalDate;

public interface IBookingService extends IService<Booking, Long> {
    boolean delete(Long id);

    List<Booking> getAll();

    boolean isRoomAvailable(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);

}
