package za.ac.cput.marginhotelmanagement.repository;
/* ReceptionistRepositoryTest.java
   Repository Test for Receptionist
   Author: Lithabile Lalela (221340963)
   Date: 05 July 2026 */

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.marginhotelmanagement.domain.*;
import za.ac.cput.marginhotelmanagement.factory.StaffFactory;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
@SpringBootTest
class ReceptionistRepositoryTest {

    @Autowired
    private ReceptionistRepository repository;

    private Receptionist receptionist;

    @BeforeEach
    void setUp() {
        Name name = new Name.Builder()
                .setFirstName("Lithabile")
                .setMiddleName("L")
                .setLastName("Lalela")
                .build();
        ContactDetails contactDetails = new ContactDetails.Builder()
                .setEmail("lithabile@marginhotel.com")
                .setMobile("0821234567")
                .build();
        receptionist = StaffFactory.createReceptionist(2L, name, contactDetails, "Desk 3B");
    }

    @Test
    void create() {
        Receptionist saved = repository.save(receptionist);
        assertNotNull(saved);
        assertNotNull(saved.getStaffId());
        assertEquals("Desk 3B", saved.getDeskNumber());
        System.out.println("Created: " + saved);
    }

    @Test
    void read() {
        Receptionist saved = repository.save(receptionist);
        Receptionist found = repository.findById(saved.getStaffId()).orElse(null);
        assertNotNull(found);
        assertEquals(saved.getStaffId(), found.getStaffId());
        System.out.println("Read: " + found);
    }

    @Test
    void update() {
        Receptionist saved = repository.save(receptionist);
        Name newName = new Name.Builder()
                .setFirstName("Updated")
                .setMiddleName("L")
                .setLastName("Lalela")
                .build();
        ContactDetails contactDetails = new ContactDetails.Builder()
                .setEmail("updated@marginhotel.com")
                .setMobile("0821234567")
                .build();
        Receptionist updated = new Receptionist.Builder()
                .copy(saved)
                .setName(newName)
                .setContactDetails(contactDetails)
                .setDeskNumber("Desk 5A")
                .build();
        Receptionist result = repository.save(updated);
        assertNotNull(result);
        assertEquals("Desk 5A", result.getDeskNumber());
        System.out.println("Updated: " + result);
    }

    @Test
    @Disabled
    void delete() {
        Receptionist saved = repository.save(receptionist);
        Long id = saved.getStaffId();
        repository.delete(saved);
        Receptionist deleted = repository.findById(id).orElse(null);
        assertNull(deleted);
        System.out.println("Deleted receptionist with ID: " + id);
    }

    @Test
    void findAll() {
        Name name2 = new Name.Builder()
                .setFirstName("Second")
                .setMiddleName("R")
                .setLastName("Receptionist")
                .build();
        ContactDetails cd2 = new ContactDetails.Builder()
                .setEmail("second@marginhotel.com")
                .setMobile("0831234567")
                .build();
        Receptionist receptionist2 = StaffFactory.createReceptionist(3L, name2, cd2, "Desk 6B");
        repository.save(receptionist);
        repository.save(receptionist2);
        List<Receptionist> receptionists = repository.findAll();
        assertNotNull(receptionists);
        assertFalse(receptionists.isEmpty());
        System.out.println("All receptionists: " + receptionists);
    }
}