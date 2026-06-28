package za.ac.cput.marginhotelmanagement.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.marginhotelmanagement.domain.Room;
import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.enums.RoomType;

import static org.junit.jupiter.api.Assertions.*;

class RoomFactoryTest {

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
