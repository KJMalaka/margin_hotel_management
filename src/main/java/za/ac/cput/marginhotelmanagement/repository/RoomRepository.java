package za.ac.cput.marginhotelmanagement.repository;
import java.util.Optional;

import za.ac.cput.marginhotelmanagement.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.enums.RoomType;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

    Optional<Room> findByRoomNumber(int roomNumber);

    List<Room> findRoomByRoomType(RoomType roomType);

    List<Room> findRoomByRoomStatus(RoomStatus roomStatus);

    List<Room> findRoomByRoomStatusAndPricePerNightLessThanEqual(RoomStatus roomStatus, double maxPrice);

}
