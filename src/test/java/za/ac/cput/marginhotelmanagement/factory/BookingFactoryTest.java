package za.ac.cput.marginhotelmanagement.factory;
/*
   Author: Katlego Malaka (230443370)
   Date: 25 June 2026 */

import org.junit.jupiter.api.*;
import za.ac.cput.marginhotelmanagement.domain.*;
import za.ac.cput.marginhotelmanagement.enums.BookingChannel;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookingFactoryTest {
    private StayPeriod stayPeriod;
    private Guest guest;
    private Room room;

    @BeforeEach
    void setUp() {

        this.guest = new Guest.Builder()
                .setName(new Name.Builder().setFirstName("Kat").setLastName("Malaka").build())
                .setContactDetails(new ContactDetails.Builder().setEmail("katmalaka@gmail.com").setMobile("0761234563").build())
                .build();

        this.room = new Room.Builder()
                .setRoomId("RM-202")
                .setRoomNumber(202)
                .build();


        this.stayPeriod = new StayPeriod(LocalDate.now().plusDays(2), LocalDate.now().plusDays(6));
    }

    @Test
    @Order(1)
    void createBookingSuccess() {
        Booking booking = BookingFactory.createBooking(67L, LocalDate.now().minusDays(1), stayPeriod, BookingChannel.ONLINE, guest, room);
        assertNotNull(booking);
        assertEquals(67L, booking.getBookingId());
        assertEquals(BookingChannel.ONLINE, booking.getBookingChannel());
        System.out.println("Booking created successfully: " + booking);
    }

    @Test
    @Order(2)
    void createBookingWithInvalidBookingId() {
        Booking booking = BookingFactory.createBooking(-1L, LocalDate.now().minusDays(1), stayPeriod, BookingChannel.ONLINE, guest, room);
        assertNull(booking);
        System.out.println("Booking creation failed due to invalid booking ID.");
    }

    @Test
    @Order(3)
    void testCreateBookingWithFutureBookingDate() {
        Booking booking = BookingFactory.createBooking(68L, LocalDate.now().plusDays(1), stayPeriod, BookingChannel.ONLINE, guest, room);
        assertNull(booking);
        System.out.println("Booking creation failed due to future booking date.");
    }

    @Test
    @Order(4)
    void testCreateBookingWithInvalidStayPeriod() {

        StayPeriod invalidStayPeriod = new StayPeriod(LocalDate.now().plusDays(3), LocalDate.now().plusDays(1));

        Booking booking = BookingFactory.createBooking(69L, LocalDate.now().minusDays(1), invalidStayPeriod, BookingChannel.ONLINE, guest, room);
        assertNull(booking);
        System.out.println("Booking creation failed due to invalid stay period.");
    }
}