package za.ac.cput.marginhotelmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.marginhotelmanagement.dtos.BookingDto;
import za.ac.cput.marginhotelmanagement.dtos.CreateBookingRequest;
import za.ac.cput.marginhotelmanagement.dtos.UpdateBookingRequest;
import za.ac.cput.marginhotelmanagement.service.BookingService;

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
    public ResponseEntity<?> create(@RequestBody CreateBookingRequest request) {
        try {
            BookingDto createdBooking = bookingService.createBooking(request);
            return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            // Room exists but is already booked for these dates
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            // Stay period itself is invalid, or the guest/room reference doesn't exist
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<BookingDto> read(@PathVariable Long id) {
        BookingDto booking = bookingService.readBooking(id);
        if (booking != null) {
            return new ResponseEntity<>(booking, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody UpdateBookingRequest request) {
        try {
            BookingDto updated = bookingService.updateBooking(request);
            if (updated == null) {
                return new ResponseEntity<>("No booking found with ID #" + request.getBookingId(), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = bookingService.deleteBooking(id);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<BookingDto>> getAll() {
        List<BookingDto> bookings = bookingService.getAllBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
}
