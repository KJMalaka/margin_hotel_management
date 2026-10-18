package za.ac.cput.marginhotelmanagement.domain.dtos.payment;

import lombok.Data;
import za.ac.cput.marginhotelmanagement.enums.PaymentStatus;

/*
   Author: DM Madondo (230949703)
   Date: 24 August 2026
   */
@Data
public class CreatePaymentRequest {
    private double amount;
    private PaymentStatus paymentStatus;
    private Long invoiceId; // reference an existing invoice
}
