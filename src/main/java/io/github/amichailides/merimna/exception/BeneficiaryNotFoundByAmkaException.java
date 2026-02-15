package io.github.amichailides.merimna.exception;

public class BeneficiaryNotFoundByAmkaException extends BeneficiaryNotFoundException{
    private final String beneficiaryAmka;

    public BeneficiaryNotFoundByAmkaException(String beneficiaryAmka) {
        super("Δεν βρέθηκε ωφελούμενος στο σύστημα με ΑΜΚΑ: " + beneficiaryAmka);
        this.beneficiaryAmka = beneficiaryAmka;
    }

    public String getMessageKey() {
        return "beneficiaryByAmka.notFound";
    }

    public Object[] getMessageArgs() {
        return new Object[]{beneficiaryAmka};
    }
}
