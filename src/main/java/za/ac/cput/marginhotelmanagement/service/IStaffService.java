package za.ac.cput.marginhotelmanagement.service;

import za.ac.cput.marginhotelmanagement.domain.Manager;
import za.ac.cput.marginhotelmanagement.domain.Receptionist;
import java.util.List;

public interface IStaffService {
    Manager createManager(Manager manager);
    Manager readManager(Long id);
    Manager updateManager(Manager manager);
    void deleteManager(Long id);
    List<Manager> getAllManagers();

    Receptionist createReceptionist(Receptionist receptionist);
    Receptionist readReceptionist(Long id);
    Receptionist updateReceptionist(Receptionist receptionist);
    void deleteReceptionist(Long id);
    List<Receptionist> getAllReceptionists();
}