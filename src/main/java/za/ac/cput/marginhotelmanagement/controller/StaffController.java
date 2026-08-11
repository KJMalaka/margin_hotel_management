package za.ac.cput.marginhotelmanagement.controller;
/*
   Rest Controller for Staff hierarchy
   Author: Lithabile Lalela
   Date:19 July 2026
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.marginhotelmanagement.domain.Manager;
import za.ac.cput.marginhotelmanagement.domain.Receptionist;
import za.ac.cput.marginhotelmanagement.domain.Staff;
import za.ac.cput.marginhotelmanagement.service.StaffService;

import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {

    private StaffService staffService;

    @Autowired
    public void setStaffService(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping("/manager/create")
    public Staff createStaff(@RequestBody Manager manager) {
        return staffService.createManager(manager);
    }
    @GetMapping("/manager/read/{id}")
    public Staff readStaff(@PathVariable Long id) {
        return staffService.readManager(id);}

    @PutMapping("/manager/update")
    public Staff updateStaff(@RequestBody Manager manager) {
        return staffService.updateManager(manager);}

    @DeleteMapping("/manager/delete/{id}")
    public boolean deleteStaff(@PathVariable Long id) {
        return staffService.deleteManager(id);}

    @GetMapping("/manager/getall")
    public List<Manager> getAllStaff() {
        return staffService.getAllManagers();
    }

    @PostMapping("/receptionist/create")
    public Receptionist createReceptionist(@RequestBody Receptionist receptionist) {
        return staffService.createReceptionist(receptionist);
    }

    @GetMapping("/requeptionist/read/{id}")
    public Receptionist readReceptionist(@PathVariable Long id) {
        return staffService.readReceptionist(id);}

    @PutMapping("/receptionist/update")
    public Receptionist updateReceptionist(@RequestBody Receptionist receptionist) {
        return staffService.updateReceptionist(receptionist);}

    @DeleteMapping("/receptionist/delete/{id}")
    public boolean deleteReceptionist(@PathVariable Long id) {
        return staffService.deleteReceptionist(id);}

    @GetMapping("/receptionist/getall")
    public List<Receptionist> getAllReceptionists() {
        return staffService.getAllReceptionists();
}

}



