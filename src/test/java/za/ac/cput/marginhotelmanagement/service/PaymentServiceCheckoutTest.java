package za.ac.cput.marginhotelmanagement.service;

import org.junit.jupiter.api.Test;
import za.ac.cput.marginhotelmanagement.domain.Invoice;
import za.ac.cput.marginhotelmanagement.domain.Payment;
import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;
import za.ac.cput.marginhotelmanagement.enums.PaymentStatus;
import za.ac.cput.marginhotelmanagement.repository.PaymentRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PaymentServiceCheckoutTest {
    @Test
    void createsSuccessfulPaymentForResolvedInvoiceAndMarksItPaid() {
        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        InvoiceService invoiceService = mock(InvoiceService.class);
        PaymentService service = new PaymentService(paymentRepository, invoiceService);
        Invoice invoice = new Invoice.Builder()
                .setInvoiceId(21L)
                .setReference("DEMO-INV-21")
                .setTotalAmount(100.0)
                .setStatus(InvoiceStatus.PENDING)
                .setIssueDate(LocalDate.now())
                .build();

        when(invoiceService.read(21L)).thenReturn(invoice);
        when(paymentRepository.existsByInvoice_InvoiceId(21L)).thenReturn(false);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment toSave = invocation.getArgument(0);
            return new Payment.Builder().copy(toSave).setPaymentId(31L).build();
        });

        Payment result = service.createCheckout(21L);

        assertEquals(31L, result.getPaymentId());
        assertSame(invoice, result.getInvoice());
        assertEquals(21L, result.getInvoice().getInvoiceId());
        assertEquals(100.0, result.getAmount());
        assertEquals(PaymentStatus.SUCCESS, result.getPaymentStatus());
        assertNotNull(result.getPaymentDate());
        verify(paymentRepository).save(argThat(payment ->
                payment.getInvoice().getInvoiceId().equals(21L)
                        && payment.getAmount() == 100.0
                        && payment.getPaymentStatus() == PaymentStatus.SUCCESS));
        verify(invoiceService).update(argThat(updated ->
                updated.getInvoiceId().equals(21L)
                        && updated.getStatus() == InvoiceStatus.PAID));
    }

    @Test
    void rejectsDuplicateCheckoutForInvoice() {
        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        InvoiceService invoiceService = mock(InvoiceService.class);
        PaymentService service = new PaymentService(paymentRepository, invoiceService);
        Invoice invoice = new Invoice.Builder().setInvoiceId(21L).build();
        when(invoiceService.read(21L)).thenReturn(invoice);
        when(paymentRepository.existsByInvoice_InvoiceId(21L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.createCheckout(21L));
        verify(paymentRepository, never()).save(any(Payment.class));
    }
}
