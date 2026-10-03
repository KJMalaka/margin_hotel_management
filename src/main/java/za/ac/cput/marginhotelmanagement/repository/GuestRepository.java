package za.ac.cput.marginhotelmanagement.repository;
/*
   GuestRepository.java
   Repository for Guest entity
   Author: Hlomla Magopeni (218070349)
   Date: 04 July 2026 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.marginhotelmanagement.domain.Guest;

import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    List<Guest> findByName_FirstName(String firstName);

    List<Guest> findByName_LastName(String lastName);

    List<Guest> findByContactDetails_Email(String email); //Queries multiple guests with the same email.
}
