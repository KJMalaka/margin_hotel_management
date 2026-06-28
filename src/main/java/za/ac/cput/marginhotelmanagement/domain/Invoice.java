package za.ac.cput.marginhotelmanagement.domain;
/* Invoice.java
   Invoice POJO class
   Author: MS MALAPILE (222904267)
   Date: 20 June 2026 */

import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;

import java.time.LocalDate;

public class Invoice {

    private Long invoiceId;
    private String reference;
    private double totalAmount;
    private InvoiceStatus status;
    private LocalDate issueDate;
    private Booking booking;


    public Invoice(){

    }

    public Invoice (Builder builder){
        this.invoiceId = builder.invoiceId;
        this.reference = builder.reference;
        this.totalAmount = builder.totalAmount;
        this.issueDate= builder.issueDate;
        this.booking = builder.booking;
        this.status = builder.status;

    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public String getReference() {
        return reference;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public Booking getBooking(){
        return booking;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", reference='" + reference + '\'' +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", issueDate=" + issueDate +
                ", booking=" + booking +
                '}';
    }

    public static class Builder{
        private Long invoiceId;
        private String reference;
        private double totalAmount;
        private LocalDate issueDate;
        private Booking booking;
        private InvoiceStatus status;

        public Builder setInvoiceId(Long invoiceId) {
            this.invoiceId = invoiceId;
            return this;
        }

        public Builder setReference(String reference){
            this.reference = reference;
            return this;
        }

        public Builder setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder setIssueDate(LocalDate issueDate) {
            this.issueDate = issueDate;
            return this;
        }

        public Builder setBooking( Booking booking){
            this.booking = booking;
            return this;
        }

        public Builder setStatus(InvoiceStatus status) {
            this.status = status;
            return this;
        }

        public Builder copy (Invoice invoice){
            this.invoiceId = invoice.invoiceId;
            this.reference = invoice.reference;
            this.totalAmount = invoice.totalAmount;
            this.issueDate = invoice.issueDate;
            this.booking = invoice.booking;
            this.status = invoice.status;
            return this;
        }

        public Invoice build(){
            return new Invoice(this);
        }
    }
}
