package za.ac.cput.marginhotelmanagement.controller;
/*
   Author: Katlego Malaka (230443370)
   Date: 17 July 2026
   Updated on: 25 August 2026
*/

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestRestTemplate
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private RoomRepository roomRepository;

    private final String baseUrl = "/booking";

    private Guest mockGuest;
    private Room mockRoom;
    private BookingDto booking;

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
    void create() {
        CreateBookingRequest request = new CreateBookingRequest();
        request.setCheckInDate(LocalDateTime.now().plusDays(2));
        request.setCheckOutDate(LocalDateTime.now().plusDays(6));
        request.setBookingChannel(BookingChannel.ONLINE);
        request.setGuestId(mockGuest.getGuestId());
        request.setRoomId(mockRoom.getRoomId());

        String url = baseUrl + "/create";
        ResponseEntity<BookingDto> response = restTemplate.postForEntity(url, request, BookingDto.class);
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
        ResponseEntity<BookingDto> response = restTemplate.getForEntity(url, BookingDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(booking.getBookingId(), response.getBody().getBookingId());
    }

    @Test
    @Order(3)
    void update() {
        UpdateBookingRequest request = new UpdateBookingRequest();
        request.setBookingId(booking.getBookingId());
        request.setCheckInDate(LocalDateTime.now().plusDays(3));
        request.setCheckOutDate(LocalDateTime.now().plusDays(7));
        request.setBookingChannel(BookingChannel.WALK_IN);

        String url = baseUrl + "/update";
        ResponseEntity<BookingDto> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(request), BookingDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(booking.getBookingId(), response.getBody().getBookingId());
        assertEquals(BookingChannel.WALK_IN, response.getBody().getBookingChannel());
    }

    @Test
    @Order(4)
    void getAll() {
        String url = baseUrl + "/getall";
        ResponseEntity<BookingDto[]> response = restTemplate.getForEntity(url, BookingDto[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    @Order(5)
    @Disabled
    void delete() {
        String url = baseUrl + "/delete/" + booking.getBookingId();
        restTemplate.delete(url);

        ResponseEntity<BookingDto> checkResponse = restTemplate.getForEntity(baseUrl + "/read/" + booking.getBookingId(), BookingDto.class);
        assertEquals(HttpStatus.NOT_FOUND, checkResponse.getStatusCode());
    }
}
