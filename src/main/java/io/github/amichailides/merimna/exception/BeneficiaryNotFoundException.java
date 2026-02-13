package io.github.amichailides.merimna.exception;

public class BeneficiaryNotFoundException extends RuntimeException{
    private final Long beneficiaryId;

    public BeneficiaryNotFoundException(Long id) {
        super("Beneficiary not found with id: " + id);
        this.beneficiaryId = id;
    }

    public Long getBeneficiaryId() {
        return beneficiaryId;
    }
}
