package za.ac.cput.marginhotelmanagement.domain;
/* Invoice.java
   Invoice POJO class
   Author: MS MALAPILE (222904267)
   Date: 20 June 2026 */

import java.time.LocalDate;

public class Invoice {

    private Long invoiceId;
    private double totalAmount;
    private LocalDate issueDate;
    private String bookingId;
    private int guestId;

    protected Invoice(){

    }

    public Invoice (Builder builder){
        this.invoiceId = builder.invoiceId;
        this.totalAmount = builder.totalAmount;
        this.issueDate= builder.issueDate;

    }


    public Long getInvoiceId() {
        return invoiceId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }


    public LocalDate getIssueDate() {
        return issueDate;
    }

    public String getBookingId(){
        return bookingId;
    }

    public int getGuestId(){
        return guestId;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", totalAmount=" + totalAmount +
                ", issueDate=" + issueDate +
                ", bookingId='" + bookingId + '\'' +
                ", guestId=" + guestId +
                '}';
    }

    public static class Builder{
        private Long invoiceId;
        private double totalAmount;
        private LocalDate issueDate;
        private String bookingId;
        private int guestId;

        public Builder setInvoiceId(Long invoiceId) {
            this.invoiceId = invoiceId;
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

        public Builder setBookingId(String book){
            this.bookingId = bookingId;
            return this;
        }

        public Builder setGuestId(int guestId){
            this.guestId = guestId;
            return this;
        }
        public Builder copy (Invoice invoice){
            this.invoiceId = invoice.invoiceId;
            this.totalAmount = invoice.totalAmount;
            this.issueDate = invoice.issueDate;
            this.bookingId = invoice.bookingId;
            this.guestId = invoice.guestId;
            return this;
        }

        public Invoice build(){
            return new Invoice(this);
        }
    }
}
