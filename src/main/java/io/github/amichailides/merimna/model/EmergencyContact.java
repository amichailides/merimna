package io.github.amichailides.merimna.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContact {
    private String firstName;
    private String lastName;
    private String relationship; // γονεας, δικαστικος συμπαραστατης κλπ
    private String phoneNumber;
    private String mobileNumber;
    private String email;
}
