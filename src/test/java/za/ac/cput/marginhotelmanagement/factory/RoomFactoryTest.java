package za.ac.cput.marginhotelmanagement.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import za.ac.cput.marginhotelmanagement.domain.Room;
import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.enums.RoomType;
import za.ac.cput.marginhotelmanagement.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoomFactoryTest {
    @Autowired
    private RoomRepository roomRepository;

    private Room savedRoom1;
    private Room savedRoom2;

    @BeforeEach
    void setUp() {
        // Step A: Build rooms WITHOUT setting IDs manually
        Room room1 = new Room.Builder()
                .setRoomNumber(101)
                .setRoomType(RoomType.SINGLE)
                .setPricePerNight(850.00)
                .setRoomStatus(RoomStatus.AVAILABLE)
                .build();

        Room room2 = new Room.Builder()
                .setRoomNumber(102)
                .setRoomType(RoomType.SUITE)
                .setPricePerNight(1500.00)
                .setRoomStatus(RoomStatus.OCCUPIED)
                .build();

        // Step B: Save them. The database populates the IDs automatically here!
        savedRoom1 = roomRepository.save(room1);
        savedRoom2 = roomRepository.save(room2);
    }

    @Test
    void testFindByRoomNumber() {
        Optional<Room> foundRoomBox = roomRepository.findByRoomNumber(101);

        assertTrue(foundRoomBox.isPresent());
        // Verify that the database did indeed auto-generate an ID string for us
        assertNotNull(foundRoomBox.get().getRoomId());
        assertEquals(101, foundRoomBox.get().getRoomNumber());
    }

    @Test
    void testFindByRoomStatus() {
        List<Room> availableRooms = roomRepository.findByRoomStatus(RoomStatus.AVAILABLE);

        assertEquals(1, availableRooms.size());
        assertEquals(101, availableRooms.get(0).getRoomNumber());
    }

    @Test
    void testCreateRoomSuccess() {
        Room room = RoomFactory.createRoom(305, RoomType.DOUBLE, 2450.00, RoomStatus.AVAILABLE);

        assertNotNull(room);
        assertNotNull(room.getRoomId()); // Ensures UUID generation succeeded
        assertEquals(305, room.getRoomNumber());
        assertEquals(RoomType.DOUBLE, room.getRoomType());
        assertEquals(2450.00, room.getPricePerNight());
        assertEquals(RoomStatus.AVAILABLE, room.getRoomStatus());
    }

    @Test
    void testCreateRoomWithInvalidNumberThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RoomFactory.createRoom(-5, RoomType.SINGLE, 1200.00, RoomStatus.AVAILABLE);
        });

        assertTrue(exception.getMessage().contains("Room number must be greater than zero"));
    }

}
