package za.ac.cput.marginhotelmanagement.mappers;

/*
   Converts between the Payment entity and its DTOs. Kept as plain static
   methods, same style as the existing Factory classes (BookingFactory,
   PaymentFactory, etc.) so the service layer can call
   PaymentMapper.toEntity(...) / PaymentMapper.toResponseDto(...) without
   needing a Spring bean or a mapping library like MapStruct.
   Author: DM Madondo (230949703)
   Date: 24 August 2026
   */

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import za.ac.cput.marginhotelmanagement.domain.Payment;
import za.ac.cput.marginhotelmanagement.dtos.CreatePaymentRequest;
import za.ac.cput.marginhotelmanagement.dtos.PaymentDto;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "paymentDate", ignore = true)
    Payment toEntity(CreatePaymentRequest request);

    @Mapping(source = "invoice.invoiceId", target = "invoiceId")
    @Mapping(source = "invoice.reference", target = "invoiceReference")
    PaymentDto toDto(Payment payment);
}
