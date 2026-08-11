package za.ac.cput.marginhotelmanagement.repository;
/* ManagerRepositoryTest.java
   Repository Test for Manager
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
class ManagerRepositoryTest {

    @Autowired
    private ManagerRepository repository;

    private Manager manager;

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
        manager = StaffFactory.createManager(1L, name, contactDetails, "Office 1A");
    }

    @Test
    void create() {
        Manager saved = repository.save(manager);
        assertNotNull(saved);
        assertNotNull(saved.getStaffId());
        assertEquals("Office 1A", saved.getOfficeNumber());
        System.out.println("Created: " + saved);
    }

    @Test
    void read() {
        Manager saved = repository.save(manager);
        Manager found = repository.findById(saved.getStaffId()).orElse(null);
        assertNotNull(found);
        assertEquals(saved.getStaffId(), found.getStaffId());
        System.out.println("Read: " + found);
    }

    @Test
    void update() {
        Manager saved = repository.save(manager);
        Name newName = new Name.Builder()
                .setFirstName("Updated")
                .setMiddleName("L")
                .setLastName("Lalela")
                .build();
        ContactDetails contactDetails = new ContactDetails.Builder()
                .setEmail("updated@marginhotel.com")
                .setMobile("0821234567")
                .build();
        Manager updated = new Manager.Builder()
                .copy(saved)
                .setName(newName)
                .setContactDetails(contactDetails)
                .setOfficeNumber("Office 2B")
                .build();
        Manager result = repository.save(updated);
        assertNotNull(result);
        assertEquals("Office 2B", result.getOfficeNumber());
        System.out.println("Updated: " + result);
    }

    @Test
    @Disabled
    void delete() {
        Manager saved = repository.save(manager);
        Long id = saved.getStaffId();
        repository.delete(saved);
        Manager deleted = repository.findById(id).orElse(null);
        assertNull(deleted);
        System.out.println("Deleted manager with ID: " + id);
    }

    @Test
    void findAll() {
        Name name2 = new Name.Builder()
                .setFirstName("Second")
                .setMiddleName("M")
                .setLastName("Manager")
                .build();
        ContactDetails cd2 = new ContactDetails.Builder()
                .setEmail("second@marginhotel.com")
                .setMobile("0831234567")
                .build();
        Manager manager2 = StaffFactory.createManager(2L, name2, cd2, "Office 3C");
        repository.save(manager);
        repository.save(manager2);
        List<Manager> managers = repository.findAll();
        assertNotNull(managers);
        assertFalse(managers.isEmpty());
        System.out.println("All managers: " + managers);
    }
}
