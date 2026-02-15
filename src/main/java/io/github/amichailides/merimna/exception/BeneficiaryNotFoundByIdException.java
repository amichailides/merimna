package io.github.amichailides.merimna.exception;

public class BeneficiaryNotFoundByIdException extends BeneficiaryNotFoundException{
    private final Long beneficiaryId;

    public BeneficiaryNotFoundByIdException(Long BeneficiaryId) {
        super("Δεν βρέθηκε ωφελούμενος στο σύστημα με ID: " + BeneficiaryId);  // Για logs
        this.beneficiaryId = BeneficiaryId;
    }

    @Override
    public String getMessageKey() {
        return "beneficiaryById.notFound";
    }

    @Override
    public Object[] getMessageArgs() {
        return new Object[]{beneficiaryId};
    }
}
