package za.ac.cput.marginhotelmanagement.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import za.ac.cput.marginhotelmanagement.enums.RoomType;
import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.domain.Room;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private Room savedRoom;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        roomRepository.deleteAll();

        Room room = new Room.Builder()
                .setRoomNumber(202)
                .setRoomType(RoomType.SINGLE)
                .setPricePerNight(1200.00)
                .setRoomStatus(RoomStatus.AVAILABLE)
                .build();

        savedRoom = roomRepository.save(room);
    }

    @Test
    void testFindByRoomNumber() {
        Optional<Room> found = roomRepository.findByRoomNumber(202);
        assertTrue(found.isPresent());
        assertEquals(savedRoom.getRoomId(), found.get().getRoomId());
    }

    @Test
    void testFindByRoomStatus() {
        // FIXED: Renamed from findRoomByRoomStatus to match RoomRepository definition
        List<Room> activeRooms = roomRepository.findByRoomStatus(RoomStatus.AVAILABLE);
        assertFalse(activeRooms.isEmpty());
        assertTrue(activeRooms.stream().anyMatch(r -> r.getRoomNumber() == 202));
    }

    @Test
    void testFindByRoomType() {
        List<Room> singleRooms = roomRepository.findByRoomType(RoomType.SINGLE);
        assertFalse(singleRooms.isEmpty());
    }
}
