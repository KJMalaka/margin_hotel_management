package za.ac.cput.marginhotelmanagement.mappers;

/*
   Converts between the Payment entity and its DTOs. Kept as plain static
   methods, same style as the existing Factory classes (BookingFactory,
   PaymentFactory) so the service layer can call
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
    // MapStruct's builder introspection just sees
    // Payment.Builder.copy(Payment) and treats it like one; ignoring it here
    // just silences that false-positive "unmapped target property" warning.
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "paymentDate", ignore = true)
    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "copy", ignore = true)
    Payment toEntity(CreatePaymentRequest request);

    @Mapping(source = "invoice.invoiceId", target = "invoiceId")
    @Mapping(source = "invoice.reference", target = "invoiceReference")
    PaymentDto toDto(Payment payment);
}
