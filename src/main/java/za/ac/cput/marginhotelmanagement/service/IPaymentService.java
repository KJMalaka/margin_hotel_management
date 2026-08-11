package za.ac.cput.marginhotelmanagement.service;
/*
   Author: DM Madondo (230949703)
   Date: 11 July 2026
   */

import za.ac.cput.marginhotelmanagement.domain.Payment;
import za.ac.cput.marginhotelmanagement.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface IPaymentService extends IService<Payment, Long> {
    List<Payment> findPaymentByAmount(double amount);
    List<Payment> findPaymentByPaymentStatus(PaymentStatus paymentStatus);
    List<Payment> findPaymentByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Payment> findPaymentByPaymentId(Long paymentId);
}
