package za.ac.cput.marginhotelmanagement.service;

import za.ac.cput.marginhotelmanagement.domain.Room;
import za.ac.cput.marginhotelmanagement.enums.RoomStatus;

import java.time.LocalDate;
import java.util.List;

public interface IRoomService extends IService<Room, Long> {
    List<Room> getRoomByStatus(RoomStatus status);

    List<Room> findAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate);
}
