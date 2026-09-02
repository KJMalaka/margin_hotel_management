package za.ac.cput.marginhotelmanagement.service;
/*
   Author: Katlego Malaka (230443370)
    Co-Author: Dumisane Madondo (230949703)
   Date: 09 July 2026
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.marginhotelmanagement.domain.Booking;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.domain.Room;
import za.ac.cput.marginhotelmanagement.domain.StayPeriod;
import za.ac.cput.marginhotelmanagement.dtos.BookingDto;
import za.ac.cput.marginhotelmanagement.dtos.CreateBookingRequest;
import za.ac.cput.marginhotelmanagement.dtos.UpdateBookingRequest;
import za.ac.cput.marginhotelmanagement.factory.BookingFactory;
import za.ac.cput.marginhotelmanagement.factory.StayPeriodFactory;
import za.ac.cput.marginhotelmanagement.mappers.BookingMapper;
import za.ac.cput.marginhotelmanagement.repository.BookingRepository;
import za.ac.cput.marginhotelmanagement.util.Helper;

import java.time.LocalDate;
import java.util.List;

@Service

public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final GuestService guestService;
    private final RoomService roomService;
    private final BookingMapper bookingMapper; //MapStruct generated bean

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                           GuestService guestService,
                           RoomService roomService,
                           BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.guestService = guestService;
        this.roomService = roomService;
        this.bookingMapper = bookingMapper;
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
        List<Booking> roomBookings = bookingRepository.findByRoom_RoomId(roomId);
        return Helper.isRoomAvailable(roomBookings, checkInDate, checkOutDate);
    }

    //==== DTO based methods, this is what BookingController actually calls ====//

    @Transactional
    public BookingDto createBooking(CreateBookingRequest request) {
        Guest guest = resolveGuest(request.getGuestId());
        Room room = resolveRoom(request.getRoomId());

        StayPeriod stayPeriod = StayPeriodFactory.createStayPeriod(request.getCheckInDate(), request.getCheckOutDate());
        if (stayPeriod == null) {
            throw new IllegalArgumentException("Check-in date and check-out date must be valid, with check-out after check-in");
        }
        if (Helper.isNullOrEmpty(request.getBookingChannel())) {
            throw new IllegalArgumentException("Booking channel is required");
        }
        if (!isRoomAvailable(room.getRoomId(), stayPeriod.getCheckInDate().toLocalDate(), stayPeriod.getCheckOutDate().toLocalDate())) {
            throw new IllegalStateException("Room " + room.getRoomId() + " is already booked for those dates");
        }

        Booking booking = BookingFactory.createBooking(LocalDate.now(), stayPeriod, request.getBookingChannel(), guest, room);
        if (booking == null) {
            throw new IllegalArgumentException("Booking could not be created — check the supplied details");
        }

        Booking savedBooking = this.bookingRepository.save(booking);
        return bookingMapper.toDto(savedBooking);
    }

    public BookingDto readBooking(Long id) {
        Booking booking = this.bookingRepository.findById(id).orElse(null);
        if (booking == null) {
            return null;
        }
        return bookingMapper.toDto(booking);
    }

    public BookingDto updateBooking(UpdateBookingRequest request) {
        if (Helper.isNullOrEmpty(request.getBookingId())) {
            throw new IllegalArgumentException("Booking ID is required");
        }
        Booking existing = this.bookingRepository.findById(request.getBookingId()).orElse(null);
        if (existing == null) {
            return null; //Controller returns 404 Not Found
        }

        StayPeriod stayPeriod = StayPeriodFactory.createStayPeriod(request.getCheckInDate(), request.getCheckOutDate());
        if (stayPeriod == null) {
            throw new IllegalArgumentException("Check-in date and check-out date must be valid, with check-out after check-in");
        }
        if (Helper.isNullOrEmpty(request.getBookingChannel())) {
            throw new IllegalArgumentException("Booking channel is required");
        }

        Booking updatedBooking = new Booking.Builder()
                .copy(existing)
                .setStayPeriod(stayPeriod)
                .setBookingChannel(request.getBookingChannel())
                .build();

        Booking savedBooking = this.bookingRepository.save(updatedBooking);
        return bookingMapper.toDto(savedBooking);
    }

    public boolean deleteBooking(Long id) {
        if (!this.bookingRepository.existsById(id)) {
            return false;
        }
        this.bookingRepository.deleteById(id);
        return true;
    }

    public List<BookingDto> getAllBookings() {
        return this.bookingRepository.findAll()
                .stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    //------ Private Booking helpers ----------//

    private Guest resolveGuest(Long guestId) {
        if (Helper.isNullOrEmpty(guestId)) {
            throw new IllegalArgumentException("Guest ID is required");
        }
        Guest guest = this.guestService.read(guestId);
        if (guest == null) {
            throw new IllegalArgumentException("Guest not found with ID# " + guestId);
        }
        return guest;
    }

    private Room resolveRoom(Long roomId) {
        if (Helper.isNullOrEmpty(roomId)) {
            throw new IllegalArgumentException("Room ID is required");
        }
        Room room = this.roomService.read(roomId);
        if (room == null) {
            throw new IllegalArgumentException("Room not found with ID# " + roomId);
        }
        return room;
    }
}
