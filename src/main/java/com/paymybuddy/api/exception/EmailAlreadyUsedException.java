package com.paymybuddy.api.exception;


public class EmailAlreadyUsedException extends RuntimeException {

    private static final long serialVersionUID = 2904885346235731443L;

    public EmailAlreadyUsedException(String email) {
	super("The email " + email + " is already used.");
    }
}
