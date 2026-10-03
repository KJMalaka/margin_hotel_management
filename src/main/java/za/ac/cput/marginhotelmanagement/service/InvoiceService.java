package za.ac.cput.marginhotelmanagement.service;
/*
    Author: MS Malapile (222904267)
    Date: 12 July 2026
    Updated: [Your Name] ([Your Student Number]), 08 September 2026 - added
    the DTO-based create/read/update/delete/find methods (InvoiceController
    now calls these instead of working with the raw Invoice entity
    directly). Same shape as the Payment/PaymentService split.
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.marginhotelmanagement.domain.Booking;
import za.ac.cput.marginhotelmanagement.domain.Invoice;
import za.ac.cput.marginhotelmanagement.domain.dtos.CreateInvoiceRequest;
import za.ac.cput.marginhotelmanagement.domain.dtos.InvoiceDto;
import za.ac.cput.marginhotelmanagement.domain.dtos.UpdateInvoiceRequest;
import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;
import za.ac.cput.marginhotelmanagement.mappers.InvoiceMapper;
import za.ac.cput.marginhotelmanagement.repository.BookingRepository;
import za.ac.cput.marginhotelmanagement.repository.InvoiceRepository;
import za.ac.cput.marginhotelmanagement.util.Helper;

import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceService implements IInvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final BookingRepository bookingRepository;
    private final InvoiceMapper invoiceMapper; //MapStruct generated bean

    @Autowired
    InvoiceService(InvoiceRepository invoiceRepository,
                   BookingRepository bookingRepository,
                   InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.bookingRepository = bookingRepository;
        this.invoiceMapper = invoiceMapper;
    }

    /* ==== IService persistence CRUD (unchanged from before, just now also
       runs validateInvoice()) ==== */

    @Override
    public Invoice create(Invoice invoice) {
        validateInvoice(invoice);
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice read(Long id) { return invoiceRepository.findById(id).orElse(null); }

    @Override
    public Invoice update(Invoice invoice) {
        validateInvoice(invoice);
        return invoiceRepository.save(invoice);
    }

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

    /* ==== DTO based methods - this is what InvoiceController actually calls ==== */

    public InvoiceDto createInvoice(CreateInvoiceRequest request) {
        Booking booking = resolveBooking(request.getBookingId());
        Invoice mapped = invoiceMapper.toEntity(request);
        Invoice invoice = new Invoice.Builder()
                .copy(mapped)
                .setBooking(booking)
                .setStatus(Helper.isNullOrEmpty(request.getStatus()) ? InvoiceStatus.PENDING : request.getStatus())
                .setIssueDate(Helper.isNullOrEmpty(request.getIssueDate()) ? LocalDate.now() : request.getIssueDate())
                .build();

        Invoice savedInvoice = create(invoice);
        return invoiceMapper.toDto(savedInvoice);
    }

    public InvoiceDto readInvoice(Long id) {
        Invoice invoice = read(id);
        if (invoice == null) {
            return null;
        }
        return invoiceMapper.toDto(invoice);
    }

    public InvoiceDto updateInvoice(Long id, UpdateInvoiceRequest request) {
        Invoice invoice = read(id);
        if (invoice == null) {
            return null; //Controller returns 404 Not Found
        }
        Invoice.Builder builder = new Invoice.Builder().copy(invoice);
        if (!Helper.isNullOrEmpty(request.getStatus())) {
            builder.setStatus(request.getStatus());
        }
        if (request.getTotalAmount() != null) {
            builder.setTotalAmount(request.getTotalAmount());
        }
        Invoice savedInvoice = update(builder.build());
        return invoiceMapper.toDto(savedInvoice);
    }

    public boolean deleteInvoice(Long id) {
        Invoice invoice = read(id);
        if (invoice == null) {
            return false;
        }
        return delete(invoice);
    }

    public List<InvoiceDto> getAllInvoices() {
        return findAll()
                .stream()
                .map(invoiceMapper::toDto)
                .toList();
    }

    public List<InvoiceDto> getInvoicesByStatus(InvoiceStatus status) {
        return findByStatus(status)
                .stream()
                .map(invoiceMapper::toDto)
                .toList();
    }

    public List<InvoiceDto> getInvoicesByIssueDate(LocalDate issueDate) {
        return findByIssueDate(issueDate)
                .stream()
                .map(invoiceMapper::toDto)
                .toList();
    }

    public List<InvoiceDto> getInvoicesByBookingId(Long bookingId) {
        return findByBookingId(bookingId)
                .stream()
                .map(invoiceMapper::toDto)
                .toList();
    }

    //------ Private Invoice helpers ----------//
    private void validateInvoice(Invoice invoice) {
        if (Helper.isNullOrEmpty(invoice)) {
            throw new IllegalArgumentException("Invoice must not be null");
        }
        if (Helper.isNullOrEmpty(invoice.getReference())) {
            throw new IllegalArgumentException("Invoice reference is required");
        }
        if (!Helper.isValidAmount(invoice.getTotalAmount())) {
            throw new IllegalArgumentException("Total amount must be greater than R0");
        }
        if (Helper.isNullOrEmpty(invoice.getStatus())) {
            throw new IllegalArgumentException("Invoice status is required");
        }
        if (Helper.isNullOrEmpty(invoice.getIssueDate())) {
            throw new IllegalArgumentException("Issue date is required");
        }
        if (Helper.isNullOrEmpty(invoice.getBooking())) {
            throw new IllegalArgumentException("Booking is required");
        }
    }

    private Booking resolveBooking(Long bookingId) {
        if (Helper.isNullOrEmpty(bookingId)) {
            throw new IllegalArgumentException("Booking ID is required");
        }
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID# " + bookingId));
    }
}