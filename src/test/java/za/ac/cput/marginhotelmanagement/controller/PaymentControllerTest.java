package za.ac.cput.marginhotelmanagement.controller;
/*
   Author: DM Madondo (230949703)
   Date: 17 July 2026
   */
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.marginhotelmanagement.domain.*;
import za.ac.cput.marginhotelmanagement.enums.*;
import za.ac.cput.marginhotelmanagement.factory.*;
import za.ac.cput.marginhotelmanagement.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentControllerTest {

    private static Guest mockGuest;
    @Autowired
    private GuestRepository guestRepository;
    private static Room mockRoom;
    @Autowired
    private RoomRepository roomRepository;
    private static StayPeriod mockStayPeriod;
    private static Booking mockBooking;
    @Autowired
    private BookingRepository bookingRepository;
    private static Invoice mockInvoice;
    @Autowired
    private InvoiceRepository invoiceRepository;

    private static Payment payment;
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TestRestTemplate restTemplate;
    private static final String BASE_URL = "http://localhost:8080/payment";

    @BeforeEach
    void setUp() {
         mockGuest = GuestFactory.createGuest(
                new Name.Builder()
                        .setFirstName("John")
                        .setMiddleName("M")
                        .setLastName("Doe")
                        .build(),
                new ContactDetails.Builder()
                        .setEmail("john.doe@example.com")
                        .setMobile("0123456789")
                        .build());
        assertNotNull(mockGuest, "Mock guest creation failed");
        mockGuest = guestRepository.save(mockGuest);

        mockRoom = RoomFactory.createRoom(101, RoomType.SINGLE, 750.00, RoomStatus.AVAILABLE);
        assertNotNull(mockRoom, "Mock room creation failed");
        mockRoom = roomRepository.save(mockRoom);

        mockStayPeriod = StayPeriodFactory.createStayPeriod(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3));
        assertNotNull(mockStayPeriod, "Mock stay period creation failed");

        mockBooking = BookingFactory.createBooking(
                LocalDate.now(),
                mockStayPeriod,
                BookingChannel.ONLINE,
                mockGuest,
                mockRoom);
        assertNotNull(mockBooking, "Mock booking creation failed");
        mockBooking = bookingRepository.save(mockBooking);

        mockInvoice = InvoiceFactory.createInvoice(
                "INV-TEST-001",
                1500.00,
                InvoiceStatus.PENDING,
                LocalDate.now(),
                mockBooking);
        assertNotNull(mockInvoice, "Mock invoice creation failed");
        mockInvoice = invoiceRepository.save(mockInvoice);

        Payment payment = PaymentFactory.createPayment(
                1500.00,
                PaymentStatus.SUCCESS,
                LocalDateTime.now().minusMinutes(5),
                mockInvoice);
        assertNotNull(payment, "Mock payment creation failed");
        PaymentControllerTest.payment = payment;
    }

    @Test
    @Order(1)
    void create() {
        String url = BASE_URL + "/create";
        ResponseEntity<Payment> response = this.restTemplate.postForEntity(url, payment, Payment.class);
        assertNotNull(response);
        Payment paymentSaved = response.getBody();
        //assertEquals(payment.getAmount(), paymentSaved.getAmount());
        System.out.println("Created Name: " + paymentSaved);

    }

    @Test
    @Order(2)
    void read() {
        String url = BASE_URL + "/read/" + payment.getPaymentId();
        ResponseEntity<Payment> response = this.restTemplate.getForEntity(url, Payment.class);
        assertNotNull(response);
        Payment paymentRead = response.getBody();
        ///assertEquals(payment.getPaymentId(), paymentRead.getPaymentId());
        System.out.println("Read Name: " + paymentRead);
    }

    @Test
    @Order(3)
    void update() {
        String url = BASE_URL + "/update" ;
        Payment updatedPayment = new Payment.Builder().copy(payment).setAmount(2010.00).build();
        this.restTemplate.put(url, updatedPayment);
        ResponseEntity<Payment> response = this.restTemplate.getForEntity(BASE_URL + "/read/" + updatedPayment.getPaymentId(), Payment.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        //assertEquals(updatedName.getLastName(), response.getBody().getLastName());
        System.out.println("Updated Name: " + response.getBody());
    }

    @Test
    @Disabled
    @Order(4)
    void delete() {
        String url = BASE_URL + "/delete/" + payment.getPaymentId();
        this.restTemplate.delete(url);
        //Verify that the name has been deleted
        ResponseEntity<Payment> response = this.restTemplate.getForEntity(BASE_URL + "/read/", Payment.class);
        assertNull(response.getBody());
        System.out.println("Deleted Name");
    }

    @Test
    @Order(5)
    void getAll() {
        String url = BASE_URL + "/getall";
        ResponseEntity<Payment[]> response = this.restTemplate.getForEntity(url, Payment[].class);
        assertNotNull(response);
        System.out.println("Get All Payments");
        if (response.getBody() != null) {
            for (Payment payment : response.getBody()) {
                System.out.println( payment);
            }
        }

    }

    @Test
    @Order(6)
    void findByAmount() {
        String url = BASE_URL + "/findByAmount" + payment.getAmount();
        ResponseEntity<Payment[]> response = this.restTemplate.getForEntity(url, Payment[].class);
        assertNotNull(response);
        System.out.println("Find Payments by Amount");
        if (response.getBody() != null) {
            for (Payment payment : response.getBody()) {
                System.out.println(payment);
            }
        }
    }

    @Test
    @Order(7)
    void findPaymentByPaymentStatus() {
        String url = BASE_URL + "/findByPaymentStatus" + payment.getPaymentStatus();
        ResponseEntity<Payment[]> response = this.restTemplate.getForEntity(url, Payment[].class);
        assertNotNull(response);
        System.out.println("Find Payments by Payment Status");
        if (response.getBody() != null) {
            for (Payment payment : response.getBody()) {
                System.out.println(payment);
            }
        }
    }

    @Test
    @Order(8)
    void findPaymentByPaymentDateBetween() {
        String url = BASE_URL + "/findByPaymentDateBetween";
        ResponseEntity<Payment[]> response = this.restTemplate.getForEntity(url, Payment[].class);
        assertNotNull(response);
        System.out.println("Find Payments by Payment Date Range");
        if (response.getBody() != null) {
            for (Payment payment : response.getBody()) {
                System.out.println(payment);
            }
        }
    }
}