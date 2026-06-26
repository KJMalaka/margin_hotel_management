package za.ac.cput.marginhotelmanagement.factory;
/*
   Author: Katlego Malaka(230443370)
   Date: 25 June 2026 */

import za.ac.cput.marginhotelmanagement.domain.Booking;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.domain.Room;
import za.ac.cput.marginhotelmanagement.domain.StayPeriod;
import za.ac.cput.marginhotelmanagement.enums.BookingChannel;
import za.ac.cput.marginhotelmanagement.util.Helper;

import java.time.LocalDate;

public class BookingFactory {
    public static Booking createBooking(Long bookingId, LocalDate bookingDate, StayPeriod stayPeriod, BookingChannel bookingChannel, Guest guest, Room room) {

        if (Helper.isNullOrEmpty(bookingId) ||
                Helper.isNullOrEmpty(bookingDate) ||
                Helper.isNullOrEmpty(stayPeriod) ||
                Helper.isNullOrEmpty(bookingChannel) ||
                Helper.isNullOrEmpty(guest) ||
                Helper.isNullOrEmpty(room)) {
            return null;
        }
        if ( bookingId <= 0) {
           return null;
        }
        if (bookingDate.isAfter(LocalDate.now())) {
            return null;
        }
        if ( stayPeriod.getCheckInDate() == null || stayPeriod.getCheckOutDate() == null || stayPeriod.getCheckInDate().isAfter(stayPeriod.getCheckOutDate())) {
            return null;
        }

        return new Booking.Builder()
                .setBookingId(bookingId)
                .setBookingDate(bookingDate)
                .setStayPeriod(stayPeriod)
                .setBookingChannel(bookingChannel)
                .setGuest(guest)
                .setRoom(room)
                .build();
    }
}
