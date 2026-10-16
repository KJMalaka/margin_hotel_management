package za.ac.cput.marginhotelmanagement.mappers;

/*
   Converts between the Room entity and the Room DTO. Plain static methods,
   same style as GuestMapper and StaffMapper, since the Room entity has a
   private constructor and no static builder() that MapStruct could use.
   */

import za.ac.cput.marginhotelmanagement.domain.Room;
import za.ac.cput.marginhotelmanagement.domain.dtos.room.CreateRequest;
import za.ac.cput.marginhotelmanagement.domain.dtos.room.UpdateRequest;
import za.ac.cput.marginhotelmanagement.domain.dtos.room.RoomDto;

public class RoomMapper {

    public static Room toEntity(CreateRequest request) {
        return new Room.Builder()
                .setRoomNumber(request.getRoomNumber())
                .setRoomType(request.getRoomType())
                .setPricePerNight(request.getPricePerNight())
                .setRoomStatus(request.getRoomStatus())
                .build();
    }

    public static Room toEntity(UpdateRequest request) {
        return new Room.Builder()
                .setRoomId(request.getRoomId())
                .setRoomNumber(request.getRoomNumber())
                .setRoomType(request.getRoomType())
                .setPricePerNight(request.getPricePerNight())
                .setRoomStatus(request.getRoomStatus())
                .build();
    }

    public static RoomDto toDto(Room room) {
        return new RoomDto.Builder()
                .setRoomId(room.getRoomId())
                .setRoomNumber(room.getRoomNumber())
                .setRoomType(room.getRoomType())
                .setPricePerNight(room.getPricePerNight())
                .setRoomStatus(room.getRoomStatus())
                .build();
    }
}
