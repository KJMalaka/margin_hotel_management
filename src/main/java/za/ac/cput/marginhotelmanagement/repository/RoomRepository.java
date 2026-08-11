package za.ac.cput.marginhotelmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.ac.cput.marginhotelmanagement.domain.Room;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

    @Query("SELECT r FROM Room r WHERE NOT EXISTS (" +
            "SELECT b FROM Booking b WHERE b.room = r " +
            "AND b.stayPeriod.checkInDate < :checkOutDate " +
            "AND b.stayPeriod.checkOutDate > :checkInDate)")
    List<Room> findAvailableRooms(
            @Param("checkInDate") LocalDateTime checkInDate,
            @Param("checkOutDate") LocalDateTime checkOutDate);
}

