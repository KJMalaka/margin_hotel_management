package za.ac.cput.marginhotelmanagement.repository;
/* InvoiceRepositoryTest.java
   Author: MS Malapile (222904267)
   Date: 12 July 2026 */

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.marginhotelmanagement.domain.Booking;
import za.ac.cput.marginhotelmanagement.domain.Invoice;
import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
@SpringBootTest
class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    private Invoice invoice;

    @BeforeEach
    void setUp() {
        invoice = new Invoice.Builder()
                .setReference("INV-001")
                .setTotalAmount(1500.00)
                .setStatus(InvoiceStatus.PENDING)
                .setIssueDate(LocalDate.of(2026, 7, 10))
                .build();
    }

    @Test
    void create() {
        Invoice savedInvoice = invoiceRepository.save(invoice);
        assertNotNull(savedInvoice);
        assertNotNull(savedInvoice.getInvoiceId());
        assertEquals("INV-001", savedInvoice.getReference());
        System.out.println("Created: " + savedInvoice);
    }

    @Test
    void read() {
        Invoice savedInvoice = invoiceRepository.save(invoice);
        Invoice foundInvoice = invoiceRepository.findById(savedInvoice.getInvoiceId()).orElse(null);
        assertNotNull(foundInvoice);
        assertEquals(savedInvoice.getInvoiceId(), foundInvoice.getInvoiceId());
        System.out.println("Read: " + foundInvoice);
    }

    @Test
    void update() {
        Invoice savedInvoice = invoiceRepository.save(invoice);
        Invoice updatedInvoice = new Invoice.Builder()
                .copy(savedInvoice)
                .setStatus(InvoiceStatus.PAID)
                .setTotalAmount(2000.00)
                .build();
        Invoice result = invoiceRepository.save(updatedInvoice);
        assertNotNull(result);
        assertEquals(InvoiceStatus.PAID, result.getStatus());
        assertEquals(2000.00, result.getTotalAmount());
        System.out.println("Updated: " + result);
    }

    @Test
    @Disabled
    void delete() {
        Invoice savedInvoice = invoiceRepository.save(invoice);
        Long id = savedInvoice.getInvoiceId();
        invoiceRepository.delete(savedInvoice);
        Invoice deletedInvoice = invoiceRepository.findById(id).orElse(null);
        assertNull(deletedInvoice);
        System.out.println("Deleted invoice with ID: " + id);
    }

    @Test
    void findAll() {
        Invoice invoice2 = new Invoice.Builder()
                .setReference("INV-002")
                .setTotalAmount(3000.00)
                .setStatus(InvoiceStatus.PAID)
                .setIssueDate(LocalDate.of(2026, 7, 12))
                .build();
        invoiceRepository.save(invoice);
        invoiceRepository.save(invoice2);
        List<Invoice> invoices = invoiceRepository.findAll();
        assertNotNull(invoices);
        assertFalse(invoices.isEmpty());
        System.out.println("All invoices: " + invoices);
    }
}