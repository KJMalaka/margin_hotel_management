package za.ac.cput.marginhotelmanagement.factory;
/* StaffFactoryTest.java
   TDD Factory Test for Staff hierarchy
   Author: Lithabile Lalela (221340963)
   Date: 27 June 2026 */

import org.junit.jupiter.api.*;
import za.ac.cput.marginhotelmanagement.domain.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StaffFactoryTest {

    private Name name;
    private ContactDetails contactDetails;

    @BeforeEach
    void setUp() {
        this.name = NameFactory.createName("Lithabile", "L", "Lalela");
        this.contactDetails = new ContactDetails.Builder()
                .setEmail("lithabile@marginhotel.com")
                .setMobile("0678435765")
                .build();
    }



    @Test
    @Order(1)
    void createManagerSuccess() {
        Manager manager = StaffFactory.createManager(
                1L, name, contactDetails, "Office 1A");
        assertNotNull(manager);
        assertEquals(1L, manager.getStaffId());
        assertEquals("Office 1A", manager.getOfficeNumber());
        System.out.println("Manager created successfully: " + manager);
    }

    @Test
    @Order(2)
    void createManagerWithInvalidId() {
        Manager manager = StaffFactory.createManager(
                -1L, name, contactDetails, "Office 1A");
        assertNull(manager);
        System.out.println("Manager creation failed due to invalid ID.");
    }

    @Test
    @Order(3)
    void createManagerWithNullName() {
        Manager manager = StaffFactory.createManager(
                1L, null, contactDetails, "Office 1A");
        assertNull(manager);
        System.out.println("Manager creation failed due to null name.");
    }

    @Test
    @Order(4)
    void createManagerWithNullContactDetails() {
        Manager manager = StaffFactory.createManager(
                1L, name, null, "Office 1A");
        assertNull(manager);
        System.out.println("Manager creation failed due to null contact details.");
    }

    @Test
    @Order(5)
    void createManagerWithEmptyOfficeNumber() {
        Manager manager = StaffFactory.createManager(
                -1L, name, contactDetails, "");
        assertNull(manager);
        System.out.println("Manager creation failed due to empty office number.");
    }



    @Test
    @Order(6)
    void createReceptionistSuccess() {
        Receptionist receptionist = StaffFactory.createReceptionist(
                2L, name, contactDetails, "Desk 3B");
        assertNotNull(receptionist);
        assertEquals(2L, receptionist.getStaffId());
        assertEquals("Desk 3B", receptionist.getDeskNumber());
        System.out.println("Receptionist created successfully: " + receptionist);
    }

    @Test
    @Order(7)
    void createReceptionistWithInvalidId() {
        Receptionist receptionist = StaffFactory.createReceptionist(
                -1L, name, contactDetails, "Desk 3B");
        assertNull(receptionist);
        System.out.println("Receptionist creation failed due to invalid ID.");
    }

    @Test
    @Order(8)
    void createReceptionistWithNullName() {
        Receptionist receptionist = StaffFactory.createReceptionist(
                2L, null, contactDetails, "Desk 3B");
        assertNull(receptionist);
        System.out.println("Receptionist creation failed due to null name.");
    }

    @Test
    @Order(9)
    void createReceptionistWithNullContactDetails() {
        Receptionist receptionist = StaffFactory.createReceptionist(
                2L, name, null, "Desk 3B");
        assertNull(receptionist);
        System.out.println("Receptionist creation failed due to null contact details.");
    }

    @Test
    @Order(10)
    void createReceptionistWithEmptyDeskNumber() {
        Receptionist receptionist = StaffFactory.createReceptionist(
                2L, name, contactDetails, "");
        assertNull(receptionist);
        System.out.println("Receptionist creation failed due to empty desk number.");
    }
}