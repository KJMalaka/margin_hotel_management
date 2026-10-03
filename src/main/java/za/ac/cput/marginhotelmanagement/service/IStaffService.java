package za.ac.cput.marginhotelmanagement.service;
/* IStaffService.java
   Staff Service Interface
   Author: Lithabile Lalela (221340963)
   Date: 12 July 2026 */

<<<<<<< HEAD
import za.ac.cput.marginhotelmanagement.domain.Manager;
import za.ac.cput.marginhotelmanagement.domain.Receptionist;
import java.util.List;

public interface IStaffService {
    Manager createManager(Manager manager);
    Manager readManager(Long id);
    Manager updateManager(Manager manager);
    boolean deleteManager(Long id);
    List<Manager> getAllManagers();

    Receptionist createReceptionist(Receptionist receptionist);
    Receptionist readReceptionist(Long id);
    Receptionist updateReceptionist(Receptionist receptionist);
    boolean deleteReceptionist(Long id);
    List<Receptionist> getAllReceptionists();
}
=======
public interface IStaffService extends IManagerService, IReceptionistService {
}
>>>>>>> c16694433b2b29cd839cc7775c39a80b19f708fc
