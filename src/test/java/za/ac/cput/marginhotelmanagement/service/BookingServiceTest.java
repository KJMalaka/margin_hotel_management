package za.ac.cput.marginhotelmanagement.service;
/*
   Author: Katlego Malaka (230443370)
   Date: 09 July 2026
   Updated: 25 August 2026
*/

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.marginhotelmanagement.domain.ContactDetails;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.domain.Name;
import za.ac.cput.marginhotelmanagement.domain.Room;
import za.ac.cput.marginhotelmanagement.dtos.BookingDto;
import za.ac.cput.marginhotelmanagement.dtos.CreateBookingRequest;
import za.ac.cput.marginhotelmanagement.dtos.UpdateBookingRequest;
import za.ac.cput.marginhotelmanagement.enums.BookingChannel;
import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.enums.RoomType;
import za.ac.cput.marginhotelmanagement.factory.GuestFactory;
import za.ac.cput.marginhotelmanagement.factory.RoomFactory;
import za.ac.cput.marginhotelmanagement.repository.GuestRepository;
import za.ac.cput.marginhotelmanagement.repository.RoomRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingServiceTest {

    @Autowired
    private BookingService service;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private RoomRepository roomRepository;

    private Guest mockGuest;
    private Room mockRoom;
    private BookingDto createdBooking;

    @BeforeAll
    void setUp() {
        mockGuest = GuestFactory.createGuest(
                new Name.Builder()
                        .setFirstName("Kat")
                        .setLastName("Disuru")
                        .build(),
                new ContactDetails.Builder()
                        .setEmail("katdisuru@gmail.com")
                        .setMobile("0761234563")
                        .build());
        assertNotNull(mockGuest, "Mock guest creation failed");
        mockGuest = guestRepository.save(mockGuest);

        int uniqueRoomNumber = (int) (System.currentTimeMillis() % 100_000);
        mockRoom = RoomFactory.createRoom(uniqueRoomNumber, RoomType.SINGLE, 850.00, RoomStatus.AVAILABLE);
        assertNotNull(mockRoom, "Mock room creation failed");
        mockRoom = roomRepository.save(mockRoom);
    }

    @Test
    @Order(1)
    void createBooking() {
        CreateBookingRequest request = new CreateBookingRequest();
        request.setCheckInDate(LocalDateTime.now().plusDays(2));
        request.setCheckOutDate(LocalDateTime.now().plusDays(6));
        request.setBookingChannel(BookingChannel.ONLINE);
        request.setGuestId(mockGuest.getGuestId());
        request.setRoomId(mockRoom.getRoomId());

        createdBooking = service.createBooking(request);
        assertNotNull(createdBooking);
        assertNotNull(createdBooking.getBookingId());
        assertEquals(mockGuest.getGuestId(), createdBooking.getGuestId());
        assertEquals(mockRoom.getRoomId(), createdBooking.getRoomId());
        System.out.println("Created Booking: " + createdBooking);
    }

    @Test
    @Order(2)
    void readBooking() {
        BookingDto readBooking = service.readBooking(createdBooking.getBookingId());
        assertNotNull(readBooking);
        assertEquals(createdBooking.getBookingId(), readBooking.getBookingId());
        System.out.println("Read Booking: " + readBooking);
    }

    @Test
    @Order(3)
    void updateBooking() {
        UpdateBookingRequest request = new UpdateBookingRequest();
        request.setBookingId(createdBooking.getBookingId());
        request.setCheckInDate(LocalDateTime.now().plusDays(3));
        request.setCheckOutDate(LocalDateTime.now().plusDays(7));
        request.setBookingChannel(BookingChannel.WALK_IN);

        BookingDto result = service.updateBooking(request);
        assertNotNull(result);
        assertEquals(BookingChannel.WALK_IN, result.getBookingChannel());
        System.out.println("Updated Booking: " + result);
    }

    @Test
    @Order(4)
    void getAllBookings() {
        List<BookingDto> bookingList = service.getAllBookings();
        assertNotNull(bookingList);
        assertTrue(bookingList.size() > 0);
        System.out.println("All Bookings in DB: " + bookingList);
    }

    @Test
    @Order(5)
    @Disabled
    void deleteBooking() {
        boolean success = service.deleteBooking(createdBooking.getBookingId());
        assertTrue(success);
        assertNull(service.readBooking(createdBooking.getBookingId()));
        System.out.println("Deleted Booking ID: " + createdBooking.getBookingId() + " successfully.");
    }
}
