package za.ac.cput.marginhotelmanagement.service;
/*
    Author: MS Malapile (222904267)
    Date: 12 July 2026
*/

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.marginhotelmanagement.domain.Booking;
import za.ac.cput.marginhotelmanagement.domain.Invoice;
import za.ac.cput.marginhotelmanagement.enums.BookingChannel;
import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;
import za.ac.cput.marginhotelmanagement.factory.BookingFactory;
import za.ac.cput.marginhotelmanagement.factory.InvoiceFactory;
import za.ac.cput.marginhotelmanagement.repository.BookingRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InvoiceServiceTest {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private BookingRepository bookingRepository;

    private Invoice mockInvoice;
    private Booking mockBooking;

    @BeforeAll
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
    }

    @Test
    @Order(1)
    void create() {
        Invoice created = invoiceService.create(mockInvoice);
        assertNotNull(created);
        mockInvoice = created;
        System.out.println("Created: " + created);
    }

    @Test
    @Order(2)
    void read() {
        Invoice read = invoiceService.read(mockInvoice.getInvoiceId());
        assertNotNull(read);
        assertEquals(mockInvoice.getInvoiceId(), read.getInvoiceId());
        System.out.println("Read: " + read);
    }

    @Test
    @Order(3)
    void update() {
        Invoice updated = new Invoice.Builder()
                .copy(mockInvoice)
                .setStatus(InvoiceStatus.PAID)
                .setTotalAmount(2000.00)
                .build();
        Invoice result = invoiceService.update(updated);
        assertNotNull(result);
        assertEquals(InvoiceStatus.PAID, result.getStatus());
        System.out.println("Updated: " + result);
    }

    @Test
    @Order(4)
    @Disabled
    void delete() {
        boolean deleted = invoiceService.delete(mockInvoice);
        assertTrue(deleted);
        System.out.println("Deleted invoice: " + mockInvoice.getInvoiceId());
    }

    @Test
    @Order(5)
    void findAll() {
        List<Invoice> invoices = invoiceService.findAll();
        assertNotNull(invoices);
        assertFalse(invoices.isEmpty());
        System.out.println("All invoices: " + invoices);
    }

    @Test
    @Order(6)
    void findByStatus() {
        List<Invoice> invoices = invoiceService.findByStatus(InvoiceStatus.PENDING);
        assertNotNull(invoices);
        System.out.println("Invoices by status: " + invoices);
    }
}