package za.ac.cput.marginhotelmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.marginhotelmanagement.domain.Room;
import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.repository.RoomRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoomService implements IRoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room create(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room read(Long roomId) {
        return roomRepository.findById(roomId).orElse(null);
    }

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public Room update(Room room) {
        if (roomRepository.existsById(room.getRoomId())) {
            return roomRepository.save(room);
        }
        return null;
    }

    @Override
    public boolean delete(Room room) {
        if (room != null && roomRepository.existsById(room.getRoomId())) {
            roomRepository.delete(room);
            return !roomRepository.existsById(room.getRoomId());
        }
        return false;
    }

    @Override
    public List<Room> getRoomByStatus(RoomStatus status) {
        return roomRepository.findByRoomStatus(status);
    }

    //Called from BookingController.create() — that's the separate double-booking
    @Override
    public List<Room> findAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null || !checkInDate.isBefore(checkOutDate)) {
            return List.of();
        }

        LocalDateTime start = checkInDate.atStartOfDay();
        LocalDateTime end = checkOutDate.atTime(23, 59, 59, 999_999_999);
        return roomRepository.findAvailableRooms(start, end);
    }
}
