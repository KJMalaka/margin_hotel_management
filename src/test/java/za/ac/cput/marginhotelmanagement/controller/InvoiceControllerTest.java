package za.ac.cput.marginhotelmanagement.controller;
/*
    Author: MS Malapile (222904267)
    Date: 19 July 2026
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InvoiceControllerTest {

    private static Booking mockBooking;

    @Autowired
    private BookingRepository bookingRepository;

    private static Invoice mockInvoice;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/invoice";

    @BeforeEach
    void setUp() {
        mockBooking = BookingFactory.createBooking(
                LocalDate.now(),
                null,
                BookingChannel.ONLINE,
                null,
                null
        );
        assertNotNull(mockBooking, "Mock booking creation failed");
        mockBooking = bookingRepository.save(mockBooking);

        mockInvoice = InvoiceFactory.createInvoice(
                1L,
                "INV-TEST-001",
                1500.00,
                InvoiceStatus.PENDING,
                LocalDate.now(),
                mockBooking
        );
        assertNotNull(mockInvoice, "Mock invoice creation failed");
        mockInvoice = invoiceRepository.save(mockInvoice);
    }

    @Test
    @Order(1)
    void create() {
        String url = BASE_URL + "/create";
        ResponseEntity<Invoice> response = this.restTemplate.postForEntity(url, mockInvoice, Invoice.class);
        assertNotNull(response);
        Invoice invoiceSaved = response.getBody();
        System.out.println("Created: " + invoiceSaved);
    }

    @Test
    @Order(2)
    void read() {
        String url = BASE_URL + "/read/" + mockInvoice.getInvoiceId();
        ResponseEntity<Invoice> response = this.restTemplate.getForEntity(url, Invoice.class);
        assertNotNull(response);
        Invoice invoiceRead = response.getBody();
        System.out.println("Read: " + invoiceRead);
    }

    @Test
    @Order(3)
    void update() {
        String url = BASE_URL + "/update";
        Invoice updatedInvoice = new Invoice.Builder()
                .copy(mockInvoice)
                .setStatus(InvoiceStatus.PAID)
                .setTotalAmount(2000.00)
                .build();
        this.restTemplate.put(url, updatedInvoice);
        ResponseEntity<Invoice> response = this.restTemplate.getForEntity(
                BASE_URL + "/read/" + updatedInvoice.getInvoiceId(), Invoice.class);
        assertNotNull(response);
        System.out.println("Updated: " + response.getBody());
    }

    @Test
    @Order(4)
    @Disabled
    void delete() {
        String url = BASE_URL + "/delete/" + mockInvoice.getInvoiceId();
        this.restTemplate.delete(url);
        System.out.println("Deleted invoice with ID: " + mockInvoice.getInvoiceId());
    }

    @Test
    @Order(5)
    void getAll() {
        String url = BASE_URL + "/getall";
        ResponseEntity<Invoice[]> response = this.restTemplate.getForEntity(url, Invoice[].class);
        assertNotNull(response);
        System.out.println("Get All Invoices");
        if (response.getBody() != null) {
            for (Invoice invoice : response.getBody()) {
                System.out.println(invoice);
            }
        }
    }

    @Test
    @Order(6)
    void findByStatus() {
        String url = BASE_URL + "/findByStatus/" + mockInvoice.getStatus();
        ResponseEntity<Invoice[]> response = this.restTemplate.getForEntity(url, Invoice[].class);
        assertNotNull(response);
        System.out.println("Find Invoices by Status");
        if (response.getBody() != null) {
            for (Invoice invoice : response.getBody()) {
                System.out.println(invoice);
            }
        }
    }
}