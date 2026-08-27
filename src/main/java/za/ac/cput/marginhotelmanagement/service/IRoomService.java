package za.ac.cput.marginhotelmanagement.service;
import za.ac.cput.marginhotelmanagement.domain.Room;
import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.enums.RoomType;
import java.util.List;

public interface IRoomService extends IService<Room,Long> {
    List<Room> getRoomByStatus(RoomStatus status);

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.marginhotelmanagement.domain.Room;

public interface IRoomService extends JpaRepository<Room, String> {

}
