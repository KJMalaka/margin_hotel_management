package za.ac.cput.marginhotelmanagement.controller;
/*
   Rest Controller for Staff hierarchy
   Author: Lithabile Lalela
   Date: 19 July 2026
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.marginhotelmanagement.domain.Manager;
import za.ac.cput.marginhotelmanagement.domain.Receptionist;
import za.ac.cput.marginhotelmanagement.dtos.*;
import za.ac.cput.marginhotelmanagement.mappers.StaffMapper;
import za.ac.cput.marginhotelmanagement.service.StaffService;

import java.util.List;

@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "http://localhost:3000")
public class StaffController {

    private StaffService staffService;

    @Autowired
    public void setStaffService(StaffService staffService) {
        this.staffService = staffService;
    }

    //Manager

    @PostMapping("/manager/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ManagerDto createManager(@RequestBody CreateManagerRequest request) {
        Manager manager = StaffMapper.toEntity(request);
        Manager saved = staffService.createManager(manager);
        return StaffMapper.toDto(saved);
    }

    @GetMapping("/manager/read/{id}")
    public ManagerDto readManager(@PathVariable Long id) {
        Manager manager = staffService.readManager(id);
        return StaffMapper.toDto(manager);
    }

    @PutMapping("/manager/update")
    public ManagerDto updateManager(@RequestBody UpdateManagerRequest request) {
        Manager manager = StaffMapper.toEntity(request);
        Manager updated = staffService.updateManager(manager);
        return StaffMapper.toDto(updated);
    }

    @DeleteMapping("/manager/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteManager(@PathVariable Long id) {
        staffService.deleteManager(id);
    }

    @GetMapping("/manager/getall")
    public List<ManagerDto> getAllManagers() {
        return staffService.getAllManagers()
                .stream()
                .map(StaffMapper::toDto)
                .toList();
    }

    //  Receptionist

    @PostMapping("/receptionist/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ReceptionistDto createReceptionist(@RequestBody CreateReceptionistRequest request) {
        Receptionist receptionist = StaffMapper.toEntity(request);
        Receptionist saved = staffService.createReceptionist(receptionist);
        return StaffMapper.toDto(saved);
    }

    @GetMapping("/receptionist/read/{id}")
    public ReceptionistDto readReceptionist(@PathVariable Long id) {
        Receptionist receptionist = staffService.readReceptionist(id);
        return StaffMapper.toDto(receptionist);
    }

    @PutMapping("/receptionist/update")
    public ReceptionistDto updateReceptionist(@RequestBody UpdateReceptionistRequest request) {
        Receptionist receptionist = StaffMapper.toEntity(request);
        Receptionist updated = staffService.updateReceptionist(receptionist);
        return StaffMapper.toDto(updated);
    }

    @DeleteMapping("/receptionist/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReceptionist(@PathVariable Long id) {
        staffService.deleteReceptionist(id);
    }

    @GetMapping("/receptionist/getall")
    public List<ReceptionistDto> getAllReceptionists() {
        return staffService.getAllReceptionists()
                .stream()
                .map(StaffMapper::toDto)
                .toList();
    }
}