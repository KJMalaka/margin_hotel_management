package za.ac.cput.marginhotelmanagement.service;
/*
   Author: Katlego Malaka (230443370)
   Date: 09 July 2026
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.marginhotelmanagement.domain.Booking;
import za.ac.cput.marginhotelmanagement.repository.BookingRepository;

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
}


