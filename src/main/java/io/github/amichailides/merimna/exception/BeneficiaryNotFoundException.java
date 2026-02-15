package io.github.amichailides.merimna.exception;

public abstract class BeneficiaryNotFoundException extends RuntimeException{

    public BeneficiaryNotFoundException(String message) {
        super(message);  // Περνάει το message στο RuntimeException
    }
    public abstract String getMessageKey();
    public abstract Object[] getMessageArgs();
}
