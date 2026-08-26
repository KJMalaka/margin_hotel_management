package za.ac.cput.marginhotelmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import za.ac.cput.marginhotelmanagement.domain.Room;
import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.enums.RoomType;
import za.ac.cput.marginhotelmanagement.service.IRoomService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IRoomService roomService;

    @Autowired
    private ObjectMapper objectMapper;

    private Room mockRoom;

    @BeforeEach
    void setUp() {
        // Correctly instantiate Room via its Builder pattern to fix the protected constructor error
        mockRoom = new Room.Builder()
                .setRoomId(1L)
                .setRoomNumber(101) // Uses integer as expected by RoomFactory
                .setRoomType(RoomType.SINGLE) // Ensure RoomType enum value is set
                .setPricePerNight(850.00)
                .setRoomStatus(RoomStatus.AVAILABLE)
                .build();
    }

    @Test
    @Order(1)
    void testCreate() throws Exception {
        Mockito.when(roomService.create(any(Room.class))).thenReturn(mockRoom);

        mockMvc.perform(post("/room/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRoom)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roomId").value(1L))
                .andExpect(jsonPath("$.roomNumber").value(101));
    }

    @Test
    @Order(2)
    void testRead() throws Exception {
        Mockito.when(roomService.read(1L)).thenReturn(mockRoom);

        mockMvc.perform(get("/room/read/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomId").value(1L))
                .andExpect(jsonPath("$.roomNumber").value(101));
    }

    @Test
    @Order(3)
    void testUpdate() throws Exception {
        Mockito.when(roomService.update(any(Room.class))).thenReturn(mockRoom);

        mockMvc.perform(put("/room/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRoom)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void testFindAll() throws Exception {
        List<Room> allRooms = Collections.singletonList(mockRoom);
        Mockito.when(roomService.findAll()).thenReturn(allRooms);

        mockMvc.perform(get("/room/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomNumber").value(101));
    }

    @Test
    @Order(5)
    void testGetRoomByStatus() throws Exception {
        List<Room> availableRooms = Collections.singletonList(mockRoom);
        Mockito.when(roomService.getRoomByStatus(RoomStatus.AVAILABLE)).thenReturn(availableRooms);

        mockMvc.perform(get("/room/status/AVAILABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomStatus").value("AVAILABLE"));
    }

    @Test
    @Order(6)
    void testDelete() throws Exception {
        Mockito.when(roomService.read(1L)).thenReturn(mockRoom);
        Mockito.when(roomService.delete(any(Room.class))).thenReturn(true);

        mockMvc.perform(delete("/room/delete/1"))
                .andExpect(status().isNoContent());
    }
}
