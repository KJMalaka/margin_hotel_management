package za.ac.cput.marginhotelmanagement.factory;
/*
   NameFactoryTest.java
   Test for NameFactory
   Author: Hlomla Magopeni (218070349)
   Date: 26 June 2026 */

import org.junit.jupiter.api.*;
import za.ac.cput.marginhotelmanagement.domain.Name;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NameFactoryTest {

    @Test
    @Order(1)
    void createNameSuccess() {
        Name name = NameFactory.createName("Hlomla", "S", "Magopeni");
        assertNotNull(name);
        assertEquals("Hlomla", name.getFirstName());
        assertEquals("Magopeni", name.getLastName());
        System.out.println("Name created successfully: " + name);
    }

    @Test
    @Order(2)
    void createNameWithNullFirstName() {
        Name name = NameFactory.createName(null, "S", "Magopeni");
        assertNull(name);
        System.out.println("Name creation failed due to null first name.");
    }

    @Test
    @Order(3)
    void createNameWithEmptyLastName() {
        Name name = NameFactory.createName("Hlomla", "Moosa", "");
        assertNull(name);
        System.out.println("Name creation failed due to empty last name.");
    }

    @Test
    @Order(4)
    void createNameWithNoMiddleName() {
        Name name = NameFactory.createName("Hlomla", null, "Magopeni");
        assertNotNull(name);
        System.out.println("Name created with no middle name: " + name);
    }
}