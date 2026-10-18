package za.ac.cput.marginhotelmanagement.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class ContactDetails implements ValueObject {
    private String email;
    private String mobile;

    public ContactDetails() {
    }

    public ContactDetails(Builder builder) {
        this.email = builder.email;
        this.mobile = builder.mobile;
    }

    @Override
    public String toString() {
        return "ContactDetails{" +
                "email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }

    public static class Builder {
        private String email;
        private String mobile;

        public Builder setEmail(String email) {
            this.email = email;
            return this;

        }
        public Builder setMobile(String mobile) {
            this.mobile = mobile;
            return this;

        }
        public Builder copy(ContactDetails contactDetails) {
            this.email = contactDetails.email;
            this.mobile = contactDetails.mobile;
            return this;
        }
        public ContactDetails build() {
            return new ContactDetails(this);
        }
    }


}
