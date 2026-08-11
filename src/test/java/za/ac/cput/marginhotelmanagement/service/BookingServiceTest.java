package za.ac.cput.marginhotelmanagement.service;
/*
   Author: Katlego Malaka (230443370)
   Date: 09 July 2026
*/

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.marginhotelmanagement.domain.Booking;
import za.ac.cput.marginhotelmanagement.domain.StayPeriod;
import za.ac.cput.marginhotelmanagement.enums.BookingChannel;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookingServiceTest {

    @Autowired
    private BookingService service;

    private static Booking booking1;
    private static Booking booking2;
    private static Booking createdBooking;

    @BeforeEach
    void setUp() {
        StayPeriod stayPeriod = new StayPeriod(LocalDate.now().plusDays(2), LocalDate.now().plusDays(6));


        booking1 = new Booking.Builder()
                .setBookingDate(LocalDate.now())
                .setStayPeriod(stayPeriod)
                .setBookingChannel(BookingChannel.ONLINE)
                .setGuest(null)
                .setRoom(null)
                .build();
    }

    @Test
    @Order(1)
    void create() {
        createdBooking = service.create(booking1);
        assertNotNull(createdBooking);
        assertNotNull(createdBooking.getBookingId());
        System.out.println("Created Real Booking: " + createdBooking);
    }

    @Test
    @Order(2)
    void read() {
        Booking readBooking = service.read(createdBooking.getBookingId());
        assertNotNull(readBooking);
        assertEquals(createdBooking.getBookingId(), readBooking.getBookingId());
        System.out.println("Read Real Booking: " + readBooking);
    }

    @Test
    @Order(3)
    void update() {

        Booking updatedBooking = new Booking.Builder().copy(createdBooking)
                .setBookingChannel(BookingChannel.WALK_IN)
                .build();

        Booking result = service.update(updatedBooking);
        assertNotNull(result);
        assertEquals(BookingChannel.WALK_IN, result.getBookingChannel());
        System.out.println("Updated Real Booking: " + result);
    }

    @Test
    @Order(5)
    @Disabled
    void delete() {
        boolean success = service.delete(createdBooking.getBookingId());
        assertTrue(success);
        assertNull(service.read(createdBooking.getBookingId()));
        System.out.println("Deleted Booking ID: " + createdBooking.getBookingId() + " successfully.");
    }

    @Test
    @Order(4)
    void getAll() {
        List<Booking> bookingList = service.getAll();
        assertNotNull(bookingList);
        assertTrue(bookingList.size() > 0);
        System.out.println("All Bookings in DB: " + bookingList);
    }
}
