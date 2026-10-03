package za.ac.cput.marginhotelmanagement.domain;

/*
   Author: DM Madondo (230949703)
   Date: 16 September 2026
   */

import jakarta.persistence.*;
import za.ac.cput.marginhotelmanagement.enums.UserRole;

import javax.management.relation.Role;

@Entity
@Table(name = "app_users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    //This is the login key for the user, it must be unique
    @Column(unique = true)
    private String email;

    private String password; //BCrypt hashed password

    @Enumerated(EnumType.STRING)
    private UserRole role;

    //Optional link to an existing guest.
    @OneToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;

    public AppUser() {
    }

    public AppUser(Builder builder) {
        this.userId = builder.userId;
        this.email = builder.email;
        this.password = builder.password;
        this.role = builder.role;
        this.guest = builder.guest;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public Guest getGuest() {
        return guest;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", guest=" + (guest != null ? guest.getGuestId() : null) +
                '}';
    }

    public static class Builder {
        private Long userId;
        private String email;
        private String password;
        private UserRole role;
        private Guest guest;

        public Builder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setRole(UserRole role) {
            this.role = role;
            return this;
        }

        public Builder setGuest(Guest guest) {
            this.guest = guest;
            return this;
        }

        public Builder copy(AppUser appUser) {
            this.userId = appUser.userId;
            this.email = appUser.email;
            this.password = appUser.password;
            this.role = appUser.role;
            this.guest = appUser.guest;
            return this;
        }
        public AppUser build() {
            return new AppUser(this);
        }
    }
}
