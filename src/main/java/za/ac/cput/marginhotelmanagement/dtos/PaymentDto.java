package za.ac.cput.marginhotelmanagement.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import za.ac.cput.marginhotelmanagement.enums.PaymentStatus;

import java.time.LocalDateTime;

/*
   Full shape returned to the client on reads. Flattens the linked Invoice
   down to invoiceId + invoiceReference instead of nesting the whole Invoice
   entity.
   Author: DM Madondo (230949703)
   Date: 24 August 2026
   */
@AllArgsConstructor
@Getter
public class PaymentDto {
    private Long paymentId;
    private double amount;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private Long invoiceId;
    private String invoiceReference;
}
