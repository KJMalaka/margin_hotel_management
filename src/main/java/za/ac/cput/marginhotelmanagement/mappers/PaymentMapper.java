package za.ac.cput.marginhotelmanagement.mappers;

/*
   Converts between the Payment entity and its DTOs using a Spring-managed
   MapStruct mapper.
   Author: DM Madondo (230949703)
   Date: 24 August 2026
   */

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import za.ac.cput.marginhotelmanagement.domain.Payment;
import za.ac.cput.marginhotelmanagement.domain.dtos.payment.CreatePaymentRequest;
import za.ac.cput.marginhotelmanagement.domain.dtos.payment.PaymentDto;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    // "invoice" and "paymentDate" are set explicitly in PaymentService, not
    // from the request. "paymentId" is DB-generated. "copy" isn't a real
    // property — MapStruct's builder introspection just sees
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
