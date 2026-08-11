package za.ac.cput.marginhotelmanagement.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.marginhotelmanagement.domain.*;
import za.ac.cput.marginhotelmanagement.enums.BookingChannel;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final String baseUrl = "/booking";

    private static Booking booking;

    @BeforeAll
    static void setUp() {
        Name name = new Name.Builder()
                .setFirstName("Kat")
                .setLastName("Disuru")
                .build();

        ContactDetails contactDetails = new ContactDetails.Builder()
                .setEmail("katdisuru@gmail.com")
                .setMobile("0761234563")
                .build();

        Guest guest = new Guest.Builder()
                .setName(name)
                .setContactDetails(contactDetails)
                .build();

        String uniqueRoomId = "RM-" + System.currentTimeMillis();

        Room room = new Room.Builder()
                .setRoomId(uniqueRoomId)
                .setRoomNumber(101)
                .build();

        StayPeriod stayPeriod = new StayPeriod(LocalDate.now().plusDays(2), LocalDate.now().plusDays(6));


        booking = new Booking.Builder()
                .setBookingChannel(BookingChannel.ONLINE)
                .setStayPeriod(stayPeriod)
                .setBookingDate(LocalDate.now())
                .setGuest(guest)
                .setRoom(room)
                .build();
    }

    @Test
    @Order(1)
    void create() {
        String url = baseUrl + "/create";
        ResponseEntity<Booking> response = restTemplate.postForEntity(url, booking, Booking.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getBookingId());

        booking = response.getBody();
        System.out.println("Created booking ID: " + booking.getBookingId());
    }

    @Test
    @Order(2)
    void read() {
        String url = baseUrl + "/read/" + booking.getBookingId();
        ResponseEntity<Booking> response = restTemplate.getForEntity(url, Booking.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(booking.getBookingId(), response.getBody().getBookingId());
    }

    @Test
    @Order(3)
    void update() {
        StayPeriod updatedStayPeriod = new StayPeriod(LocalDate.now().plusDays(3), LocalDate.now().plusDays(7));

        Booking updatedBooking = new Booking.Builder()
                .copy(booking)
                .setStayPeriod(updatedStayPeriod)
                .setBookingChannel(BookingChannel.WALK_IN)
                .build();

        String url = baseUrl + "/update";
        HttpEntity<Booking> entity = new HttpEntity<>(updatedBooking);
        ResponseEntity<Booking> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Booking.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedBooking.getBookingId(), response.getBody().getBookingId());
    }

    @Test
    @Order(4)
    void getAll() {
        String url = baseUrl + "/getall";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    @Order(5)
    @Disabled
    void delete() {
        String url = baseUrl + "/delete/" + booking.getBookingId();
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<Booking> checkResponse = restTemplate.getForEntity(baseUrl + "/read/" + booking.getBookingId(), Booking.class);
        assertEquals(HttpStatus.NOT_FOUND, checkResponse.getStatusCode());
    }
}