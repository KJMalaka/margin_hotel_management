package za.ac.cput.marginhotelmanagement.domain;
/*
   Author: DM Madondo (230949703)
   Date: 20 June 2026
   */

import za.ac.cput.marginhotelmanagement.enums.PaymentStatus;

import java.time.LocalDateTime;

public class Payment {
    private Long paymentId;
    private double amount;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private Invoice invoice;

    public Payment() {
    }

    public Payment(Builder builder) {
        this.paymentId = builder.paymentId;
        this.amount = builder.amount;
        this.paymentStatus = builder.paymentStatus;
        this.paymentDate = builder.paymentDate;
        this.invoice = builder.invoice;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public Invoice getInvoice(){return invoice;}

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", invoiceId=" + invoice +
                ", amount=" + amount +
                ", paymentStatus=" + paymentStatus +
                ", paymentDate=" + paymentDate +
                '}';
    }

    public static class Builder{
        private Long paymentId;
        private double amount;
        private PaymentStatus paymentStatus;
        private LocalDateTime paymentDate;
        private Invoice invoice;

        public Builder setPaymentId(Long paymentId){
            this.paymentId = paymentId;
            return this;
        }
        public Builder setAmount(double amount){
            this.amount = amount;
            return this;
        }
        public Builder setPaymentStatus(PaymentStatus paymentStatus){
            this.paymentStatus = paymentStatus;
            return this;
        }
        public Builder setPaymentDate(LocalDateTime paymentDate){
            this.paymentDate = paymentDate;
            return this;
        }
        public Builder setInvoice(Invoice invoice){
            this.invoice = invoice;
            return this;
        }

        public Builder copy(Payment payment){
            this.paymentId = payment.paymentId;
            this.amount = payment.amount;
            this.paymentStatus = payment.paymentStatus;
            this.paymentDate = payment.paymentDate;
            this.invoice = payment.invoice;
            return this;
        }

        public Payment build(){
            return new Payment(this);
        }
    }
}
