package za.ac.cput.marginhotelmanagement.repository;
/* ManagerRepository.java
   Repository interface for Manager
   Author: Lithabile Lalela (221340963)
   Date: 05 July 2026 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.marginhotelmanagement.domain.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Manager findManagerByStaffId(Long staffId);
    Manager findManagerByOfficeNumber(String officeNumber);
}
