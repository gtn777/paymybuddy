package com.paymybuddy.api.exception;

public class NotEnoughMoneyException extends RuntimeException {

	private static final long serialVersionUID = -119898345903734825L;

	public NotEnoughMoneyException() {
		super("Balance is too low to pay your buddy.");
	}

}
