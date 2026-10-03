package za.ac.cput.marginhotelmanagement.domain.dtos;

import lombok.Data;
import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;

/*
   Author: Matuma Malapile (222904267)
   Date: 08 September 2026
   */
@Data
public class UpdateInvoiceRequest {
    private InvoiceStatus status;
    private Double totalAmount;
}