package za.ac.cput.marginhotelmanagement.service;
/*
   Author: DM Madondo (230949703)
   Date: 11 July 2026
   Updated: 24 August 2026
   */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.marginhotelmanagement.domain.Invoice;
import za.ac.cput.marginhotelmanagement.domain.Payment;
import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;
import za.ac.cput.marginhotelmanagement.enums.PaymentStatus;
import za.ac.cput.marginhotelmanagement.repository.PaymentRepository;
import za.ac.cput.marginhotelmanagement.util.Helper;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class PaymentService implements IPaymentService {
    private final PaymentRepository paymentRepository;
    private final InvoiceService invoiceService;

    @Autowired
    PaymentService(PaymentRepository paymentRepository,
                   InvoiceService invoiceService) {
        this.paymentRepository = paymentRepository;
        this.invoiceService = invoiceService;
    }

    @Override
    public Payment create(Payment payment) {
        return this.paymentRepository.save(payment);
    }

    @Override
    public Payment read(Long id) {
        return this.paymentRepository.findById(id).orElse(null);
    }

    @Override
    public Payment update(Payment payment) {
        return this.paymentRepository.save(payment);
    }

    @Override
    public boolean delete(Payment payment) {
        this.paymentRepository.delete(payment);
        return true;
    }

    @Override
    public List<Payment> findAll() {
        return this.paymentRepository.findAll();
    }

    @Override
    public List<Payment> findPaymentByAmount(double amount) {
        return this.paymentRepository.findPaymentByAmount(amount);
    }

    @Override
    public List<Payment> findPaymentByPaymentStatus(PaymentStatus paymentStatus) {
        return this.paymentRepository.findPaymentByPaymentStatus(paymentStatus);
    }

    @Override
    public List<Payment> findPaymentByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return this.paymentRepository.findPaymentByPaymentDateBetween(startDate, endDate);
    }

    @Override
    public List<Payment> findPaymentByPaymentId(Long paymentId) {
        return this.paymentRepository.findPaymentByPaymentId(paymentId);
    }

    public Payment createPayment(Payment payment, Long invoiceId) {
        validate(payment);
        Invoice invoice = resolveInvoice(invoiceId);
        Payment paymentToSave = new Payment.Builder()
                .copy(payment)
                .setInvoice(invoice)
                .setPaymentDate(LocalDateTime.now())
                .build();

        Payment savedPayment = this.paymentRepository.save(paymentToSave);
        syncInvoiceStatus(invoice, savedPayment.getPaymentStatus());
        return savedPayment;
    }

    @Transactional
    public Payment createCheckout(Long invoiceId) {
        Invoice invoice = resolveInvoice(invoiceId);
        if (paymentRepository.existsByInvoice_InvoiceId(invoiceId)) {
            throw new IllegalStateException("A payment already exists for this invoice");
        }

        Payment payment = new Payment.Builder()
                .setAmount(invoice.getTotalAmount())
                .setPaymentStatus(PaymentStatus.SUCCESS)
                .setPaymentDate(LocalDateTime.now())
                .setInvoice(invoice)
                .build();
        Payment savedPayment = paymentRepository.save(payment);
        syncInvoiceStatus(invoice, PaymentStatus.SUCCESS);
        return savedPayment;
    }

    public Payment updatePayment(Long id, PaymentStatus newStatus) {
        Payment payment = this.paymentRepository.findById(id).orElse(null);
        if (payment == null) {
            return null;
        }
        if (Helper.isNullOrEmpty(newStatus)) {
            throw new IllegalArgumentException("Payment status is required!");
        }
        Payment updatedPayment = new Payment.Builder()
                .copy(payment)
                .setPaymentStatus(newStatus)
                .build();
        Payment savedPayment = this.paymentRepository.save(updatedPayment);
        syncInvoiceStatus(savedPayment.getInvoice(), newStatus);
        return savedPayment;
    }

    //------ Private Payment helpers ----------//
    private void validate(Payment payment) {
        if (Helper.isNullOrEmpty(payment)) {
            throw new IllegalArgumentException("Payment is required");
        }
        if (!Helper.isValidAmount(payment.getAmount())) {
            throw new IllegalArgumentException("Amount must be greater than R0");
        }
        if (Helper.isNullOrEmpty(payment.getPaymentStatus())) {
            throw new IllegalArgumentException("Payment status is required!");
        }
    }
    // Keeps the linked invoice's status in sync whenever a payment's status is set — on create and on update.
    private void syncInvoiceStatus(Invoice invoice, PaymentStatus paymentStatus) {
        if (invoice == null || paymentStatus == null) {
            return;
        }
        InvoiceStatus newInvoiceStatus = (paymentStatus == PaymentStatus.SUCCESS)
                ? InvoiceStatus.PAID
                : InvoiceStatus.PENDING;
        Invoice updatedInvoice = new Invoice.Builder()
                .copy(invoice)
                .setStatus(newInvoiceStatus)
                .build();
        invoiceService.update(updatedInvoice);
    }

    private Invoice resolveInvoice(Long invoiceId) {
        if (Helper.isNullOrEmpty(invoiceId)) {
            throw new IllegalArgumentException("Invoice ID is required");
        }
        Invoice invoice = this.invoiceService.read(invoiceId);
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice not found with ID# " + invoiceId);
        }
        return invoice;
    }
}
