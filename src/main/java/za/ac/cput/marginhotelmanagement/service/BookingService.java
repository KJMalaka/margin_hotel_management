package za.ac.cput.marginhotelmanagement.service;
/*
   Author: Katlego Malaka (230443370)
    Co-Author: Dumisane Madondo (230949703)
   Date: 09 July 2026
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.marginhotelmanagement.domain.Booking;
import za.ac.cput.marginhotelmanagement.repository.BookingRepository;
import za.ac.cput.marginhotelmanagement.util.Helper;

import java.time.LocalDate;
import java.util.List;

@Service

public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking create(Booking booking) {
        // Reject overlapping bookings for the same room before saving.
        // Helper.isRoomAvailable (called via isRoomAvailable below) also
        // throws IllegalArgumentException if the stay period itself is
        // invalid (null dates, or check-out not after check-in).
        if (booking.getRoom() != null && booking.getStayPeriod() != null) {
            LocalDate checkIn = booking.getStayPeriod().getCheckInDate().toLocalDate();
            LocalDate checkOut = booking.getStayPeriod().getCheckOutDate().toLocalDate();
            if (!isRoomAvailable(booking.getRoom().getRoomId(), checkIn, checkOut)) {
                throw new IllegalStateException(
                        "Room " + booking.getRoom().getRoomId() + " is already booked for those dates");
            }
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking read(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    @Override
    public Booking update(Booking booking) {
        if (bookingRepository.existsById(booking.getBookingId())) {
            return bookingRepository.save(booking);
        }
        return null;
    }

    @Override
    public boolean delete(Booking booking) {
        return false;
    }

    @Override
    public List<Booking> findAll() {
        return List.of();
    }

    @Override
    public boolean delete(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Booking> roomBookings = bookingRepository.findByRoomId(roomId);
        return Helper.isRoomAvailable(roomBookings, checkInDate, checkOutDate);
    }
}
