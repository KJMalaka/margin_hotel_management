package za.ac.cput.marginhotelmanagement.dtos;

import lombok.Data;

import java.time.LocalDateTime;

/*
   Author: DM Madondo (230949703)
   Date: 24 August 2026
   */
@Data
public class UpdatePaymentRequest {
    private String paymentStatus;
}
