package za.ac.cput.marginhotelmanagement.mappers;

/*
   Converts between the Invoice entity and its DTOs.
   Author: Matuma Malapile(222904267)
   Date: 08 September 2026
   */

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import za.ac.cput.marginhotelmanagement.domain.Invoice;
import za.ac.cput.marginhotelmanagement.domain.dtos.CreateInvoiceRequest;
import za.ac.cput.marginhotelmanagement.domain.dtos.InvoiceDto;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "invoiceId", ignore = true)
    @Mapping(target = "copy", ignore = true)
    Invoice toEntity(CreateInvoiceRequest request);

    @Mapping(source = "booking.bookingId", target = "bookingId")
    InvoiceDto toDto(Invoice invoice);
}