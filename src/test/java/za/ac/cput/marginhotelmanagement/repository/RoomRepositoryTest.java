package za.ac.cput.marginhotelmanagement.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import za.ac.cput.marginhotelmanagement.enums.RoomType;
import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.domain.Room;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)




public class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    private Room room1;
    private Room room2;

    @BeforeEach
    void setup(){
        room1 = new Room.Builder()

                .setRoomNumber(101)
                .setRoomStatus(RoomStatus.AVAILABLE)
                .setRoomType(RoomType.SINGLE)
                .build();

        room2 = new Room.Builder()

                .setRoomNumber(202)
                .setRoomStatus(RoomStatus.OCCUPIED)
                .setRoomType(RoomType.DOUBLE)
                .build();

        roomRepository.save(room1);
        roomRepository.save(room2);


    }
    @Test
    void testFindByRoomNumber() {
        // Act: Search for room number 101 (returns an Optional box!)
        Optional<Room> foundRoomBox = roomRepository.findByRoomNumber(101);

        // Assert: Verify the box is NOT empty and has the correct details
        assertTrue(foundRoomBox.isPresent());
        assertEquals("RM-101", foundRoomBox.get().getRoomId());
        assertEquals(850.00, foundRoomBox.get().getPricePerNight());
    }


    @Test
    void testFindByRoomStatus(){
        List<Room> availableRooms = roomRepository.findRoomByRoomStatus(RoomStatus.AVAILABLE);

        assertEquals(1,availableRooms.size());
        assertEquals(101,availableRooms.get(0).getRoomNumber());
    }



}
