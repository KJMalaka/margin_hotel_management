package za.ac.cput.marginhotelmanagement.factory;
/*
   Author: DM Madondo (230949703)
   Date: 26 June 2026
   */

import za.ac.cput.marginhotelmanagement.domain.Invoice;
import za.ac.cput.marginhotelmanagement.domain.Payment;
import za.ac.cput.marginhotelmanagement.enums.PaymentStatus;
import za.ac.cput.marginhotelmanagement.util.Helper;

import java.time.LocalDateTime;

public class PaymentFactory {
    public static Payment createPayment(Long paymentId, double amount, PaymentStatus paymentStatus,
                                        LocalDateTime paymentDate, Invoice invoice) {

        if (Helper.isNullOrEmpty(paymentId) || Helper.isNullOrEmpty(paymentStatus) ||
                Helper.isNullOrEmpty(paymentDate) || Helper.isNullOrEmpty(invoice)) {
            return null;
        }

        if (paymentId <= 0) {
            return null;
        }
        if (!Helper.isValidAmount(amount)) {
            return null;
        }
        if (!Helper.isValidPaymentDate(paymentDate)) {
            return null;
        }

        return new Payment.Builder()
                .setPaymentId(paymentId)
                .setAmount(amount)
                .setPaymentStatus(paymentStatus)
                .setPaymentDate(paymentDate)
                .setInvoice(invoice)
                .build();
    }
}
