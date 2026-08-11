package za.ac.cput.marginhotelmanagement.repository;
/* ReceptionistRepository.java
   Repository interface for Receptionist
   Author: Lithabile Lalela (221340963)
   Date: 05 July 2026 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.marginhotelmanagement.domain.Receptionist;

@Repository
public interface ReceptionistRepository extends JpaRepository<Receptionist, Long> {
    Receptionist findReceptionistByStaffId(Long staffId);
    Receptionist findReceptionistByDeskNumber(String deskNumber);
}
