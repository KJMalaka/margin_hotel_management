package za.ac.cput.marginhotelmanagement.service;
/*
    Author: MS Malapile (222904267)
    Date: 12 July 2026
*/

import za.ac.cput.marginhotelmanagement.domain.Invoice;
import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;

import java.time.LocalDate;
import java.util.List;

public interface IInvoiceService extends IService<Invoice, Long> {
    List<Invoice> findByStatus(InvoiceStatus status);
    List<Invoice> findByIssueDate(LocalDate issueDate);
    List<Invoice> findByBookingId(Long bookingId);
}