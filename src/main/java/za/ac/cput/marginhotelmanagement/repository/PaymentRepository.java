package za.ac.cput.marginhotelmanagement.repository;
/*
   Author: DM Madondo (230949703)
   Date: 2 July 2026
   */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.marginhotelmanagement.domain.Payment;
import za.ac.cput.marginhotelmanagement.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findPaymentByAmount(double amount);

    List<Payment> findPaymentByPaymentStatus(PaymentStatus paymentStatus);

    List<Payment> findPaymentByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Payment> findPaymentByPaymentId(Long paymentId);

    boolean existsByInvoice_InvoiceId(Long invoiceId);
}
