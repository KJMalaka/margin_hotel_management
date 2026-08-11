package za.ac.cput.marginhotelmanagement.service;
/* StaffServiceImpl.java
   Staff Service Implementation
   Author: Lithabile Lalela (221340963)
   Date: 12 July 2026 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.marginhotelmanagement.domain.Manager;
import za.ac.cput.marginhotelmanagement.domain.Receptionist;
import za.ac.cput.marginhotelmanagement.repository.ManagerRepository;
import za.ac.cput.marginhotelmanagement.repository.ReceptionistRepository;

import java.util.List;

@Service
public class StaffService implements IStaffService {

    private final ManagerRepository managerRepository;
    private final ReceptionistRepository receptionistRepository;

    @Autowired
    public StaffService(ManagerRepository managerRepository,
                            ReceptionistRepository receptionistRepository) {
        this.managerRepository = managerRepository;
        this.receptionistRepository = receptionistRepository;
    }

    @Override
    public Manager createManager(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public Manager readManager(Long id) {
        return managerRepository.findById(id).orElse(null);
    }

    @Override
    public Manager updateManager(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public boolean deleteManager(Long id) {
        if (!managerRepository.existsById(id)) return false;
        managerRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    @Override
    public Receptionist createReceptionist(Receptionist receptionist) {
        return receptionistRepository.save(receptionist);
    }

    @Override
    public Receptionist readReceptionist(Long id) {
        return receptionistRepository.findById(id).orElse(null);
    }

    @Override
    public Receptionist updateReceptionist(Receptionist receptionist) {
        return receptionistRepository.save(receptionist);
    }

    @Override
    public boolean deleteReceptionist(Long id) {
        if (!receptionistRepository.existsById(id)) return false;
        receptionistRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Receptionist> getAllReceptionists() {
        return receptionistRepository.findAll();
    }
}
