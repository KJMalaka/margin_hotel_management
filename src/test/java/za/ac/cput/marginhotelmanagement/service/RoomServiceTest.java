package za.ac.cput.marginhotelmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.marginhotelmanagement.domain.Room;
import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.enums.RoomType;
import za.ac.cput.marginhotelmanagement.service.impl.RoomServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RoomServiceTest {

    private RoomService roomService;
    private Room room1;
    private Room room2;

    @BeforeEach
    void setUp() {
        // Assuming your project uses a Singleton repository layer or mock/in-memory map for now
        roomService = RoomServiceImpl.getInstance();

        // Build test entities using your Room Builder pattern
        room1 = new Room.Builder()
                .setRoomId("RM-101")
                .setRoomNumber(101)
                .setRoomType(RoomType.DELUXE) // Adjust based on your actual enum values
                .setPricePerNight(1500.00)
                .setRoomStatus(RoomStatus.AVAILABLE) // Adjust based on your actual enum values
                .build();

        room2 = new Room.Builder()
                .setRoomId("RM-102")
                .setRoomNumber(102)
                .setRoomType(RoomType.STANDARD)
                .setPricePerNight(850.00)
                .setRoomStatus(RoomStatus.OCCUPIED)
                .build();
    }

    @Test
    void testCreateAndRead() {
        Room created = roomService.create(room1);
        assertNotNull(created);
        assertEquals("RM-101", created.getRoomId());

        Optional<Room> read = roomService.read("RM-101");
        assertTrue(read.isPresent());
        assertEquals(101, read.get().getRoomNumber());
    }

    @Test
    void testUpdate() {
        roomService.create(room1);

        // Use copy builder to update status
        Room updatedRoom = new Room.Builder()
                .copy(room1)
                .setRoomStatus(RoomStatus.MAINTENANCE)
                .build();

        Room result = roomService.update(updatedRoom);
        assertNotNull(result);
        assertEquals(RoomStatus.MAINTENANCE, result.getRoomStatus());
    }

    @Test
    void testDelete() {
        roomService.create(room2);
        boolean deleted = roomService.delete("RM-102");
        assertTrue(deleted);

        Optional<Room> read = roomService.read("RM-102");
        assertFalse(read.isPresent());
    }

    @Test
    void testFindAvailableRooms() {
        roomService.create(room1); // AVAILABLE
        roomService.create(room2); // OCCUPIED

        List<Room> availableRooms = roomService.findAvailableRooms();
        assertFalse(availableRooms.isEmpty());

        // Match against your actual enum value for checked-in rooms
        boolean containsOccupied = availableRooms.stream()
                .anyMatch(r -> r.getRoomStatus() == RoomStatus.OCCUPIED);
        assertFalse(containsOccupied);
    }
}
