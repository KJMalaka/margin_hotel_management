package za.ac.cput.marginhotelmanagement.service;

import org.junit.jupiter.api.Test;
import za.ac.cput.marginhotelmanagement.domain.Booking;
import za.ac.cput.marginhotelmanagement.domain.Invoice;
import za.ac.cput.marginhotelmanagement.domain.dtos.invoice.CreateInvoiceRequest;
import za.ac.cput.marginhotelmanagement.domain.dtos.invoice.InvoiceDto;
import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;
import za.ac.cput.marginhotelmanagement.mappers.InvoiceMapper;
import za.ac.cput.marginhotelmanagement.repository.BookingRepository;
import za.ac.cput.marginhotelmanagement.repository.InvoiceRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InvoiceServiceCreateLinkTest {
    @Test
    void createsInvoiceLinkedToResolvedBookingAndReturnsGeneratedId() {
        InvoiceRepository invoiceRepository = mock(InvoiceRepository.class);
        BookingRepository bookingRepository = mock(BookingRepository.class);
        InvoiceMapper invoiceMapper = mock(InvoiceMapper.class);
        InvoiceService service = new InvoiceService(invoiceRepository, bookingRepository, invoiceMapper);

        Booking booking = new Booking.Builder().setBookingId(14L).build();
        CreateInvoiceRequest request = new CreateInvoiceRequest();
        request.setReference("DEMO-INV-21");
        request.setTotalAmount(100.0);
        request.setStatus(InvoiceStatus.PENDING);
        request.setIssueDate(LocalDate.now());
        request.setBookingId(14L);

        Invoice mapped = new Invoice.Builder()
                .setReference(request.getReference())
                .setTotalAmount(request.getTotalAmount())
                .setStatus(request.getStatus())
                .setIssueDate(request.getIssueDate())
                .build();
        when(bookingRepository.findById(14L)).thenReturn(Optional.of(booking));
        when(invoiceMapper.toEntity(request)).thenReturn(mapped);
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice toSave = invocation.getArgument(0);
            return new Invoice.Builder().copy(toSave).setInvoiceId(21L).build();
        });
        when(invoiceMapper.toDto(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice saved = invocation.getArgument(0);
            return new InvoiceDto(
                    saved.getInvoiceId(),
                    saved.getReference(),
                    saved.getTotalAmount(),
                    saved.getStatus(),
                    saved.getIssueDate(),
                    saved.getBooking().getBookingId());
        });

        InvoiceDto result = service.createInvoice(request);

        assertEquals(21L, result.getInvoiceId());
        assertEquals(14L, result.getBookingId());
        verify(invoiceRepository).save(argThat(invoice ->
                invoice.getBooking() == booking
                        && invoice.getBooking().getBookingId().equals(14L)));
    }
}
