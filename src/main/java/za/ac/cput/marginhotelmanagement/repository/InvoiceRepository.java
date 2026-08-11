package za.ac.cput.marginhotelmanagement.repository;
/* InvoiceRepository.java
   Repository for Invoice entity
   Author: MS Malapile (222904267)
   Date: 12 July 2026 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.marginhotelmanagement.domain.Invoice;
import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // Find invoices by status (PENDING or PAID)
    List<Invoice> findByStatus(InvoiceStatus status);

    // Find invoices by issue date
    List<Invoice> findByIssueDate(LocalDate issueDate);

    // Find invoices by booking ID
    List<Invoice> findByBooking_BookingId(Long bookingId);
}