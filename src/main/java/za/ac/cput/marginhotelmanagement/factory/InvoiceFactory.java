package za.ac.cput.marginhotelmanagement.factory;

/*
    Author: MS MALAPILE (222904267)
    Date: 27 June 2026
 */
import za.ac.cput.marginhotelmanagement.domain.Booking;
import za.ac.cput.marginhotelmanagement.domain.Invoice;
import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;
import za.ac.cput.marginhotelmanagement.util.Helper;

import java.time.LocalDate;

public class InvoiceFactory {
    public static Invoice createInvoice(Long invoiceId, String reference, double totalAmount,
            InvoiceStatus status, LocalDate issueDate, Booking booking) {

        if (Helper.isNullOrEmpty(invoiceId) || Helper.isNullOrEmpty(reference) ||
                Helper.isNullOrEmpty(status) || Helper.isNullOrEmpty(issueDate) ||
                Helper.isNullOrEmpty(booking)) {
            return null;
        }

        if (invoiceId <= 0) {
            return null;
        }

        if (totalAmount <= 0) {
            return null;
        }

        return new Invoice.Builder()
                .setInvoiceId(invoiceId)
                .setReference(reference)
                .setTotalAmount(totalAmount)
                .setStatus(status)
                .setIssueDate(issueDate)
                .setBooking(booking)
                .build();
    }

    // Overloaded method without invoiceId
    public static Invoice createInvoice(String reference, double totalAmount,
            InvoiceStatus status, LocalDate issueDate, Booking booking) {
        if (Helper.isNullOrEmpty(reference) || Helper.isNullOrEmpty(status) ||
                Helper.isNullOrEmpty(issueDate) || Helper.isNullOrEmpty(booking)) {
            return null;
        }

        if (totalAmount <= 0) {
            return null;
        }

        return new Invoice.Builder()
                .setReference(reference)
                .setTotalAmount(totalAmount)
                .setStatus(status)
                .setIssueDate(issueDate)
                .setBooking(booking)
                .build();
    }
}
