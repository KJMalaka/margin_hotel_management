package za.ac.cput.marginhotelmanagement.service;
/*
   Author: DM Madondo (230949703)
   Date: 17 July 2026
   */
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import za.ac.cput.marginhotelmanagement.domain.*;
import za.ac.cput.marginhotelmanagement.enums.BookingChannel;
import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;
import za.ac.cput.marginhotelmanagement.enums.PaymentStatus;
import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.enums.RoomType;
import za.ac.cput.marginhotelmanagement.factory.BookingFactory;
import za.ac.cput.marginhotelmanagement.factory.GuestFactory;
import za.ac.cput.marginhotelmanagement.factory.InvoiceFactory;
import za.ac.cput.marginhotelmanagement.factory.PaymentFactory;
import za.ac.cput.marginhotelmanagement.factory.RoomFactory;
import za.ac.cput.marginhotelmanagement.factory.StayPeriodFactory;
import za.ac.cput.marginhotelmanagement.repository.BookingRepository;
import za.ac.cput.marginhotelmanagement.repository.GuestRepository;
import za.ac.cput.marginhotelmanagement.repository.InvoiceRepository;
import za.ac.cput.marginhotelmanagement.repository.RoomRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentServiceTest {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    private Guest mockGuest;
    private Room mockRoom;
    private StayPeriod mockStayPeriod;
    private Booking mockBooking;
    private Invoice mockInvoice;
    private Payment mockPayment;
    private Payment payment;

    @BeforeAll
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

        mockPayment = PaymentFactory.createPayment(
                1500.00,
                PaymentStatus.SUCCESS,
                LocalDateTime.now().minusMinutes(5),
                mockInvoice);
        assertNotNull(mockPayment, "Mock payment creation failed");
    }

    @Test
    @Order(1)
    void create() {
        Payment created = paymentService.create(mockPayment);
        assertNotNull(created, "Payment should be saved");
        assertNotNull(created.getInvoice(), "Invoice relation should be populated");
        assertEquals(mockInvoice.getInvoiceId(), created.getInvoice().getInvoiceId());

        payment = created;
        System.out.println(created);
    }

    @Test
    @Order(2)
    void read() {
        Payment read = paymentService.read(payment.getPaymentId());
        assertNotNull(read);
        System.out.println(read);
    }

    @Test
    @Order(3)
    void update() {
        Payment updatedPayment = new Payment.Builder().copy(payment).setAmount(2000.00).build();
        Payment updated = paymentService.update(updatedPayment);
        assertNotNull(updated);
        System.out.println(updated);
    }

    @Test
    @Order(4)
    @Disabled
    void delete() {
        // Name deleted = nameService.delete
    }

    @Test
    @Order(5)
    void findAll() {
        List<Payment> payments = paymentService.findAll();
        System.out.println(payments);
    }

    @Test
    @Order(6)
    void findPaymentByAmount() {
        List<Payment> payments = paymentService.findPaymentByAmount(2000.00);
        System.out.println(payments);
    }

    @Test
    @Order(7)
    void findPaymentByPaymentStatus() {
        List<Payment> payments = paymentService.findPaymentByPaymentStatus(PaymentStatus.SUCCESS);
        System.out.println(payments);
    }

    @Test
    @Order(8)
    void findPaymentByPaymentDateBetween() {
        List<Payment> payments = paymentService.findPaymentByPaymentDateBetween(LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1));
        System.out.println(payments);
    }

    @Test
    @Order(9)
    void findPaymentByPaymentId() {
        List<Payment> payments = paymentService.findPaymentByPaymentId(payment.getPaymentId());
        System.out.println(payments);
    }
}