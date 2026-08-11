package za.ac.cput.marginhotelmanagement.domain;
/* Staff.java
   Abstract Staff entity
   Author: Lithabile Lalela (221340963)
   Date: 21 June 2026 */


import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "staff")
public abstract class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long staffId;

    @Embedded
    protected Name name;
    @Embedded
    protected ContactDetails contactDetails;

    protected Staff() {
    }

    protected Staff(Long staffId, Name name, ContactDetails contactDetails) {
        this.staffId = staffId;
        this.name = name;
        this.contactDetails = contactDetails;
    }

    public Long getStaffId() {
        return staffId;
    }

    public Name getName() {
        return name;
    }

    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "staffId=" + staffId +
                ", name=" + name +
                ", contactDetails=" + contactDetails +
                '}';
    }
}

