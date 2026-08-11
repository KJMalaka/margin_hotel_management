package za.ac.cput.marginhotelmanagement.service;
/*
    Author: MS Malapile (222904267)
    Date: 12 July 2026
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.marginhotelmanagement.domain.Invoice;
import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;
import za.ac.cput.marginhotelmanagement.repository.InvoiceRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceService implements IInvoiceService {

    private InvoiceRepository invoiceRepository;

    @Autowired
    InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Invoice create(Invoice invoice) { return invoiceRepository.save(invoice); }

    @Override
    public Invoice read(Long id) { return invoiceRepository.findById(id).orElse(null); }

    @Override
    public Invoice update(Invoice invoice) { return invoiceRepository.save(invoice); }

    @Override
    public boolean delete(Invoice invoice) {
        invoiceRepository.delete(invoice);
        return true;
    }

    @Override
    public List<Invoice> findAll() { return invoiceRepository.findAll(); }

    @Override
    public List<Invoice> findByStatus(InvoiceStatus status) {
        return invoiceRepository.findByStatus(status);
    }

    @Override
    public List<Invoice> findByIssueDate(LocalDate issueDate) {
        return invoiceRepository.findByIssueDate(issueDate);
    }

    @Override
    public List<Invoice> findByBookingId(Long bookingId) {
        return invoiceRepository.findByBooking_BookingId(bookingId);
    }
}