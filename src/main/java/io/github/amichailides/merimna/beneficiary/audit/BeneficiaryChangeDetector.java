package io.github.amichailides.merimna.beneficiary.audit;

import io.github.amichailides.merimna.audit.EntityChangeSet;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiaryUpdateDTO;
import io.github.amichailides.merimna.domain.Beneficiary;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class BeneficiaryChangeDetector {

    public EntityChangeSet detectChanges(@NotNull Beneficiary beneficiary,
                                         @NotNull BeneficiaryUpdateDTO dto) {
        EntityChangeSet.Builder builder = EntityChangeSet.builder()
                .trackIfPresent("firstName", beneficiary.getFirstName(), dto.firstName())
                .trackIfPresent("lastName", beneficiary.getLastName(), dto.lastName())
                .trackIfPresent("amka", beneficiary.getAmka(), dto.amka())
                .trackIfPresent("dateOfBirth", beneficiary.getDateOfBirth(), dto.dateOfBirth());

        if (dto.permanentAddress() != null) {
            builder
                    .trackIfPresent(
                            "permanentAddress.street",
                            beneficiary.getPermanentAddress().getStreet(),
                            dto.permanentAddress().street()
                    )
                    .trackIfPresent(
                            "permanentAddress.streetNumber",
                            beneficiary.getPermanentAddress().getStreetNumber(),
                            dto.permanentAddress().streetNumber()
                    )
                    .trackIfPresent(
                            "permanentAddress.city",
                            beneficiary.getPermanentAddress().getCity(),
                            dto.permanentAddress().city()
                    )
                    .trackIfPresent(
                            "permanentAddress.zipCode",
                            beneficiary.getPermanentAddress().getZipCode(),
                            dto.permanentAddress().zipCode()
                    );
        }

        if (dto.emergencyContact() != null) {
            builder
                    .trackIfPresent(
                            "emergencyContact.firstName",
                            beneficiary.getEmergencyContact().getFirstName(),
                            dto.emergencyContact().firstName()
                    )
                    .trackIfPresent(
                            "emergencyContact.lastName",
                            beneficiary.getEmergencyContact().getLastName(),
                            dto.emergencyContact().lastName()
                    )
                    .trackIfPresent(
                            "emergencyContact.relationshipType",
                            beneficiary.getEmergencyContact().getRelationshipType(),
                            dto.emergencyContact().relationshipType()
                    )
                    .trackIfPresent(
                            "emergencyContact.landline",
                            beneficiary.getEmergencyContact().getLandlinePhone(),
                            dto.emergencyContact().landlinePhone()
                    )
                    .trackIfPresent(
                            "emergencyContact.mobileNumber",
                            beneficiary.getEmergencyContact().getMobileNumber(),
                            dto.emergencyContact().mobileNumber()
                    )
                    .trackIfPresent(
                            "emergencyContact.email",
                            beneficiary.getEmergencyContact().getEmail(),
                            dto.emergencyContact().email()
                    )
                    .trackIfPresent(
                            "emergencyContact.address",
                            beneficiary.getEmergencyContact().getAddress(),
                            dto.emergencyContact().address()
                    );
        }

        return builder.build();
    }
}
