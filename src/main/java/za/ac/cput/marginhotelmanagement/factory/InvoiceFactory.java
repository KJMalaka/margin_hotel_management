package za.ac.cput.marginhotelmanagement.factory;

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
            Helper.isNullOrEmpty(booking)){
            return null;
        }

        if (invoiceId <=0){
            return null;
        }

        if (totalAmount <=0){
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
}
