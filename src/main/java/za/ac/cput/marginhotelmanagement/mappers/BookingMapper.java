package za.ac.cput.marginhotelmanagement.mappers;

/*
   Converts a Booking entity into its response DTO.

   Author: Katlego Malaka (230443370)
   Date: 25 August 2026
   */

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import za.ac.cput.marginhotelmanagement.domain.Booking;
import za.ac.cput.marginhotelmanagement.dtos.BookingDto;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(source = "stayPeriod.checkInDate", target = "checkInDate")
    @Mapping(source = "stayPeriod.checkOutDate", target = "checkOutDate")
    @Mapping(source = "guest.guestId", target = "guestId")
    @Mapping(source = "room.roomId", target = "roomId")
    @Mapping(source = "room.roomNumber", target = "roomNumber")
    BookingDto toDto(Booking booking);
}
