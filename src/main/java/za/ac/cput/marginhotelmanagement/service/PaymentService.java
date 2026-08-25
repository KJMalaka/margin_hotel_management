package za.ac.cput.marginhotelmanagement.service;
/*
   Author: DM Madondo (230949703)
   Date: 11 July 2026
   Updated: 24 August 2026
   */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.marginhotelmanagement.domain.Invoice;
import za.ac.cput.marginhotelmanagement.domain.Payment;
import za.ac.cput.marginhotelmanagement.dtos.CreatePaymentRequest;
import za.ac.cput.marginhotelmanagement.dtos.PaymentDto;
import za.ac.cput.marginhotelmanagement.dtos.UpdatePaymentRequest;
import za.ac.cput.marginhotelmanagement.enums.PaymentStatus;
import za.ac.cput.marginhotelmanagement.mappers.PaymentMapper;
import za.ac.cput.marginhotelmanagement.repository.PaymentRepository;
import za.ac.cput.marginhotelmanagement.util.Helper;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class PaymentService implements IPaymentService {
    private final PaymentRepository paymentRepository;
    private final InvoiceService invoiceService;
    private final PaymentMapper paymentMapper; //MapStruct generated bean

    @Autowired
    PaymentService(PaymentRepository paymentRepository,
                   InvoiceService invoiceService,
                   PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.invoiceService = invoiceService;
        this.paymentMapper = paymentMapper;
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

    //==== DTO base methods, this is what PaymentController actually calls ====//

    public PaymentDto createPayment(CreatePaymentRequest request) {
        validate(request);
        Invoice invoice = invoiceService.read(request.getInvoiceId());
        Payment mapped = paymentMapper.toEntity(request);
        Payment payment = new Payment.Builder()
                .copy(mapped)
                .setInvoice(invoice)
                .setPaymentDate(LocalDateTime.now())
                .build();

        Payment savedPayment = this.paymentRepository.save(payment);
        return paymentMapper.toDto(savedPayment);
    }

    public PaymentDto readPayment(Long id) {
        Payment payment = this.paymentRepository.findById(id).orElse(null);
        if (payment == null) {
            return null;
        }
        return paymentMapper.toDto(payment);
    }

    public PaymentDto updatePayment(Long id, UpdatePaymentRequest request) {
        Payment payment = this.paymentRepository.findById(id).orElse(null);
        if (payment == null) {
            return null; //Controller returns 404 Not Found
        }
        // Update the payment object with the values from the request
        if (Helper.isNullOrEmpty(request.getPaymentStatus())) {
            throw new IllegalArgumentException("Payment status is required!");
        }
        //Only the status changes
        Payment updatedPayment = new Payment.Builder()
                .copy(payment)
                .setPaymentStatus(PaymentStatus.valueOf(request.getPaymentStatus()))
                .build();
        Payment savedPayment = this.paymentRepository.save(updatedPayment);
        return paymentMapper.toDto(savedPayment);
    }

    public boolean deletePayment(Long id) {
        Payment payment = this.paymentRepository.findById(id).orElse(null);
        if (payment == null) {
            return false;
        }
        this.paymentRepository.delete(payment);
        return true;
    }

    public List<PaymentDto> getAllPayments() {
        return this.paymentRepository.findAll()
                .stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    public List<PaymentDto> getPaymentsByAmount(double amount) {
        return this.paymentRepository.findPaymentByAmount(amount)
                .stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    public List<PaymentDto> getPaymentsByPaymentStatus(PaymentStatus paymentStatus) {
        return this.paymentRepository.findPaymentByPaymentStatus(paymentStatus)
                .stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    public List<PaymentDto> getPaymentsByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return  this.paymentRepository.findPaymentByPaymentDateBetween(startDate, endDate)
                .stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    //------ Private Payment helpers ----------//
    private void validate(CreatePaymentRequest request) {
        if (!Helper.isValidAmount(request.getAmount())) {
            throw new IllegalArgumentException("Amount must be greater than R0");
        }
        if (!Helper.isNullOrEmpty(request.getPaymentStatus())) {
            throw new IllegalArgumentException("Payment status is required!");
        }
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
