package za.ac.cput.marginhotelmanagement.service;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.marginhotelmanagement.domain.Room;

public interface IRoomService extends JpaRepository<Room, String> {

}
