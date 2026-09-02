package za.ac.cput.marginhotelmanagement.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.ac.cput.marginhotelmanagement.domain.Room;
import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.enums.RoomType;

import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByRoomStatus(RoomStatus status);

    // Query method to find rooms by their type
    List<Room> findByRoomType(RoomType type);

    @Query("SELECT r FROM Room r WHERE NOT EXISTS (" +
            "SELECT b FROM Booking b WHERE b.room = r " +
            "AND b.stayPeriod.checkInDate < :checkOutDate " +
            "AND b.stayPeriod.checkOutDate > :checkInDate)")
    List<Room> findAvailableRooms(
            @Param("checkInDate") LocalDateTime checkInDate,
            @Param("checkOutDate") LocalDateTime checkOutDate);


    Optional<Room> findByRoomNumber(int roomNumber);
}
