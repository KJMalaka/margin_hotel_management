package za.ac.cput.marginhotelmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.marginhotelmanagement.domain.Booking;
import za.ac.cput.marginhotelmanagement.service.BookingService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public ResponseEntity<Booking> create(@RequestBody Booking incomingBooking) {


        Booking bookingToSave = new Booking.Builder()
                .copy(incomingBooking)
                .setBookingDate(incomingBooking.getBookingDate() != null ? incomingBooking.getBookingDate() : LocalDate.now())
                .setBookingChannel(incomingBooking.getBookingChannel())
                .setStayPeriod(incomingBooking.getStayPeriod())
                .setGuest(incomingBooking.getGuest())
                .setRoom(incomingBooking.getRoom())
                .build();

        Booking createdBooking = bookingService.create(bookingToSave);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Booking> read(@PathVariable Long id) {
        Booking booking = bookingService.read(id);
        if (booking != null) {
            return new ResponseEntity<>(booking, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Booking> update(@RequestBody Booking booking) {
        Booking updated = bookingService.update(booking);
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // This is what is triggering!
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = bookingService.delete(id);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Booking>> getAll() {
        List<Booking> bookings = bookingService.getAll();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
}
