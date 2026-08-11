package za.ac.cput.marginhotelmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.marginhotelmanagement.domain.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
