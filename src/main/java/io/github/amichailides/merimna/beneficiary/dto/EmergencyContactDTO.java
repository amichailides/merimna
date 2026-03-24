package io.github.amichailides.merimna.beneficiary.dto;

import io.github.amichailides.merimna.domain.RelationshipType;

import io.github.amichailides.merimna.validation.annotations.*;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.github.amichailides.merimna.validation.validators.HasPhoneFields;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Schema(description = "Emergency contact person for the beneficiary. At least one phone number is required.")
@AtLeastOnePhonePresent(message = "{contact.missing}", groups = FirstOrder.class)
@Builder
public record EmergencyContactDTO(
        @Schema(description = "First name of the emergency contact", example = "Μαρία")
        @NotBlank(message = "{emergency.firstName.required}", groups = FirstOrder.class)
        @ValidFirstName(message = "{emergency.firstName.invalid}", groups = SecondOrder.class)
        String firstName,

        @Schema(description = "Last name of the emergency contact", example = "Παπαδοπούλου")
        @NotBlank(message = "{emergency.lastName.required}", groups = FirstOrder.class)
        @ValidLastName(message = "{emergency.lastName.invalid}", groups = SecondOrder.class)
        String lastName,

        @Schema(description = "Relationship to the beneficiary", example = "CHILD")
        @NotNull(message = "{emergency.relationship.required}", groups = FirstOrder.class)
        RelationshipType relationshipType,

        @Schema(description = "Landline phone number", example = "2101234567")
        @ValidLandline(message = "{emergency.landline.invalid}", groups = SecondOrder.class)
        String landlinePhone,

        @Schema(description = "Mobile phone number", example = "6971234567")
        @ValidMobile(message = "{emergency.mobile.invalid}", groups = SecondOrder.class)
        String mobileNumber,

        @Schema(description = "Email address of the emergency contact", example = "maria@example.com")
        @Email(message = "{emergency.email.invalid}", groups = SecondOrder.class)
        String email,

        @Schema(description = "Residential address of the emergency contact")
        @NotNull(message = "{address.required}", groups = FirstOrder.class)
        @Valid
        AddressDTO address
) implements HasPhoneFields {}