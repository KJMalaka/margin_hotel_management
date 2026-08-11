package za.ac.cput.marginhotelmanagement.service;
/*
   Author: DM Madondo (230949703)
   Date: 11 July 2026
   */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.marginhotelmanagement.domain.Payment;
import za.ac.cput.marginhotelmanagement.enums.PaymentStatus;
import za.ac.cput.marginhotelmanagement.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService implements IPaymentService {
    private PaymentRepository paymentRepository;

    @Autowired
    PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
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
}
