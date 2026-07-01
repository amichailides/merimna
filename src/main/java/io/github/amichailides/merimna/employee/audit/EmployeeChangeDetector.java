package io.github.amichailides.merimna.employee.audit;

import io.github.amichailides.merimna.audit.EntityChangeSet;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.employee.dto.EmployeeUpdateDTO;
import org.springframework.stereotype.Component;

@Component
public class EmployeeChangeDetector {
    public EntityChangeSet detectChanges(Employee employee, EmployeeUpdateDTO dto, String newPositionCode) {
        EntityChangeSet.Builder builder = EntityChangeSet.builder()
                .trackIfPresent("firstName", employee.getFirstName(), dto.firstName())
                .trackIfPresent("lastName", employee.getLastName(), dto.lastName())
                .trackIfPresent("dateOfBirth", employee.getDateOfBirth(), dto.dateOfBirth())
                .trackIfPresent("contactEmail", employee.getContactEmail(), dto.contactEmail())
                .trackIfPresent("mobileNumber", employee.getMobileNumber(), dto.mobileNumber())
                .trackIfPresent("positionCode", employee.getPosition().getCode().getValue(), newPositionCode)
                .trackIfPresent("hireDate", employee.getHireDate(), dto.hireDate())
                .trackIfPresent("emergencyContactName", employee.getEmergencyContactName(), dto.emergencyContactName())
                .trackIfPresent(
                        "emergencyContactPhoneNumber",
                        employee.getEmergencyContactPhoneNumber(),
                        dto.emergencyContactPhoneNumber()
                );

        if (dto.address() != null) {
            builder
                    .trackIfPresent("address.street", employee.getAddress().getStreet(), dto.address().street())
                    .trackIfPresent("address.streetNumber", employee.getAddress().getStreetNumber(), dto.address().streetNumber())
                    .trackIfPresent("address.city", employee.getAddress().getCity(), dto.address().city())
                    .trackIfPresent("address.zipCode", employee.getAddress().getZipCode(), dto.address().zipCode());
        }

        return builder.build();
    }
}
