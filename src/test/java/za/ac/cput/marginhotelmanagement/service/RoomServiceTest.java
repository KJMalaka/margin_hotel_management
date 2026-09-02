package za.ac.cput.marginhotelmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.marginhotelmanagement.domain.Room;
import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.enums.RoomType;
import za.ac.cput.marginhotelmanagement.repository.RoomRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    private Room mockRoom;

    @BeforeEach
    void setUp() {

        mockRoom = new Room.Builder()
                .setRoomId(1L)
                .setRoomNumber(101)
                .setRoomType(RoomType.SINGLE)
                .setPricePerNight(850.00)
                .setRoomStatus(RoomStatus.AVAILABLE)
                .build();
    }

    @Test
    void testCreateSuccess() {
        Mockito.when(roomRepository.findByRoomNumber(101)).thenReturn(Optional.empty());
        Mockito.when(roomRepository.save(any(Room.class))).thenReturn(mockRoom);

        Room created = roomService.create(mockRoom);
        assertNotNull(created);
        assertEquals(1L, created.getRoomId());
    }

    @Test
    void testCreateDuplicateThrowsException() {
        Mockito.when(roomRepository.findByRoomNumber(101)).thenReturn(Optional.of(mockRoom));

        assertThrows(IllegalArgumentException.class, () -> roomService.create(mockRoom));
    }

    @Test
    void testRead() {
        Mockito.when(roomRepository.findById(1L)).thenReturn(Optional.of(mockRoom));

        Room found = roomService.read(1L);
        assertNotNull(found);
        assertEquals(101, found.getRoomNumber());
    }

    @Test
    void testFindAll() {
        Mockito.when(roomRepository.findAll()).thenReturn(Collections.singletonList(mockRoom));

        List<Room> list = roomService.findAll();
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }

    @Test
    void testUpdate() {
        Mockito.when(roomRepository.existsById(1L)).thenReturn(true);
        Mockito.when(roomRepository.save(any(Room.class))).thenReturn(mockRoom);

        Room updated = roomService.update(mockRoom);
        assertNotNull(updated);
    }

    @Test
    void testDelete() {
        Mockito.when(roomRepository.existsById(1L)).thenReturn(true).thenReturn(false);
        boolean deleted = roomService.delete(mockRoom);
        assertTrue(deleted);
    }

    @Test
    void testGetRoomByStatus() {
        Mockito.when(roomRepository.findByRoomStatus(RoomStatus.AVAILABLE))
                .thenReturn(Collections.singletonList(mockRoom));

        List<Room> structuralList = roomService.getRoomByStatus(RoomStatus.AVAILABLE);
        assertFalse(structuralList.isEmpty());
    }
}
