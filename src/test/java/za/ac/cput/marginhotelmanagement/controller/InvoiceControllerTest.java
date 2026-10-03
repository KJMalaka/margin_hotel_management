package za.ac.cput.marginhotelmanagement.controller;
/*
    Author: MS Malapile (222904267)
    Date: 19 July 2026
    Updated: [Your Name] ([Your Student Number]), 08 September 2026 -
    rewritten against the DTO-based contract (CreateInvoiceRequest /
    InvoiceDto / UpdateInvoiceRequest) that InvoiceController now
    exposes, same as PaymentControllerTest.
*/

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.marginhotelmanagement.domain.*;
import za.ac.cput.marginhotelmanagement.domain.dtos.CreateInvoiceRequest;
import za.ac.cput.marginhotelmanagement.domain.dtos.InvoiceDto;
import za.ac.cput.marginhotelmanagement.domain.dtos.UpdateInvoiceRequest;
import za.ac.cput.marginhotelmanagement.enums.*;
import za.ac.cput.marginhotelmanagement.factory.*;
import za.ac.cput.marginhotelmanagement.repository.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InvoiceControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    private Booking mockBooking;
    private Long invoiceId;

    private String BASE_URL() {
        return "http://localhost:" + port + "/marginhotel/invoice";
    }

    @BeforeAll
    void setUp() {
        paymentRepository.deleteAll();
        invoiceRepository.deleteAll();
        bookingRepository.deleteAll();
        roomRepository.deleteAll();
        guestRepository.deleteAll();

        Guest mockGuest = GuestFactory.createGuest(
                new Name.Builder()
                        .setFirstName("Jane")
                        .setMiddleName("A")
                        .setLastName("Doe")
                        .build(),
                new ContactDetails.Builder()
                        .setEmail("jane.doe@example.com")
                        .setMobile("0112233444")
                        .build());
        assertNotNull(mockGuest, "Mock guest creation failed");
        mockGuest = guestRepository.save(mockGuest);

        Room mockRoom = RoomFactory.createRoom(303, RoomType.DOUBLE, 900.00, RoomStatus.AVAILABLE);
        assertNotNull(mockRoom, "Mock room creation failed");
        mockRoom = roomRepository.save(mockRoom);

        StayPeriod mockStayPeriod = StayPeriodFactory.createStayPeriod(
                LocalDate.now().plusDays(1).atStartOfDay(),
                LocalDate.now().plusDays(3).atStartOfDay());
        assertNotNull(mockStayPeriod, "Mock stay period creation failed");

        mockBooking = BookingFactory.createBooking(
                LocalDate.now(),
                mockStayPeriod,
                BookingChannel.ONLINE,
                mockGuest,
                mockRoom);
        assertNotNull(mockBooking, "Mock booking creation failed");
        mockBooking = bookingRepository.save(mockBooking);
    }

    @Test
    @Order(1)
    void create() {
        CreateInvoiceRequest request = new CreateInvoiceRequest();
        request.setReference("INV-TEST-001");
        request.setTotalAmount(1500.00);
        request.setStatus(InvoiceStatus.PENDING);
        request.setIssueDate(LocalDate.now());
        request.setBookingId(mockBooking.getBookingId());

        String url = BASE_URL() + "/create";
        ResponseEntity<InvoiceDto> response = this.restTemplate.postForEntity(url, request, InvoiceDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        InvoiceDto created = response.getBody();
        assertNotNull(created, "Invoice should be created");
        assertNotNull(created.getInvoiceId());
        assertEquals("INV-TEST-001", created.getReference());
        assertEquals(1500.00, created.getTotalAmount());
        assertEquals(InvoiceStatus.PENDING, created.getStatus());
        assertEquals(mockBooking.getBookingId(), created.getBookingId());

        invoiceId = created.getInvoiceId();
        System.out.println("Created: " + created);
    }

    @Test
    @Order(2)
    void read() {
        String url = BASE_URL() + "/read/" + invoiceId;
        ResponseEntity<InvoiceDto> response = this.restTemplate.getForEntity(url, InvoiceDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        InvoiceDto read = response.getBody();
        assertNotNull(read);
        assertEquals(invoiceId, read.getInvoiceId());
        System.out.println("Read: " + read);
    }

    @Test
    @Order(3)
    void readMissingReturnsNotFound() {
        String url = BASE_URL() + "/read/999999999";
        ResponseEntity<InvoiceDto> response = this.restTemplate.getForEntity(url, InvoiceDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(4)
    void update() {
        UpdateInvoiceRequest request = new UpdateInvoiceRequest();
        request.setStatus(InvoiceStatus.PAID);
        request.setTotalAmount(2000.00);

        String url = BASE_URL() + "/update/" + invoiceId;
        HttpEntity<UpdateInvoiceRequest> entity = new HttpEntity<>(request);
        ResponseEntity<InvoiceDto> response = this.restTemplate.exchange(
                url, HttpMethod.PUT, entity, InvoiceDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        InvoiceDto updated = response.getBody();
        assertNotNull(updated);
        assertEquals(InvoiceStatus.PAID, updated.getStatus());
        assertEquals(2000.00, updated.getTotalAmount());
        System.out.println("Updated: " + updated);
    }

    @Test
    @Order(5)
    void updateMissingReturnsNotFound() {
        UpdateInvoiceRequest request = new UpdateInvoiceRequest();
        request.setStatus(InvoiceStatus.PAID);

        String url = BASE_URL() + "/update/999999999";
        HttpEntity<UpdateInvoiceRequest> entity = new HttpEntity<>(request);
        ResponseEntity<String> response = this.restTemplate.exchange(
                url, HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(6)
    void getAll() {
        String url = BASE_URL() + "/getall";
        ResponseEntity<InvoiceDto[]> response = this.restTemplate.getForEntity(url, InvoiceDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        System.out.println("Get All Invoices: " + response.getBody().length);
    }

    @Test
    @Order(7)
    void findByStatus() {
        String url = BASE_URL() + "/findByStatus/PAID";
        ResponseEntity<InvoiceDto[]> response = this.restTemplate.getForEntity(url, InvoiceDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        System.out.println("Find Invoices by Status: " + response.getBody().length);
    }

    @Test
    @Order(8)
    void findByStatusInvalidReturnsBadRequest() {
        String url = BASE_URL() + "/findByStatus/bogus";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(9)
    void findByIssueDate() {
        String url = BASE_URL() + "/findByIssueDate/" + LocalDate.now();
        ResponseEntity<InvoiceDto[]> response = this.restTemplate.getForEntity(url, InvoiceDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        System.out.println("Find Invoices by Issue Date: " + response.getBody().length);
    }

    @Test
    @Order(10)
    void findByBookingId() {
        String url = BASE_URL() + "/findByBookingId/" + mockBooking.getBookingId();
        ResponseEntity<InvoiceDto[]> response = this.restTemplate.getForEntity(url, InvoiceDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        System.out.println("Find Invoices by Booking ID: " + response.getBody().length);
    }

    @Test
    @Order(11)
    void delete() {
        String url = BASE_URL() + "/delete/" + invoiceId;
        this.restTemplate.delete(url);

        ResponseEntity<InvoiceDto> response = this.restTemplate.getForEntity(
                BASE_URL() + "/read/" + invoiceId, InvoiceDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        System.out.println("Deleted Invoice #" + invoiceId);
    }
}