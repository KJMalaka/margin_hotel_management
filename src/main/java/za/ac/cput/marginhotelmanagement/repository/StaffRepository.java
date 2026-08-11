package za.ac.cput.marginhotelmanagement.repository;
/* StaffRepository.java
   Repository interface for Staff
   Author: Lithabile Lalela (221340963)
   Date: 05 July 2026 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.marginhotelmanagement.domain.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Staff findStaffByStaffId(Long staffId);
}
