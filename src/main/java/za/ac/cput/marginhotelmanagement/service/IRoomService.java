package za.ac.cput.marginhotelmanagement.service;
import za.ac.cput.marginhotelmanagement.domain.Room;
import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.enums.RoomType;
import java.util.List;

public interface IRoomService extends IService<Room,Long> {
    List<Room> getRoomByStatus(RoomStatus status);
}
