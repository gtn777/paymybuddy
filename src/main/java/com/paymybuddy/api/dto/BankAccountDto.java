
package com.paymybuddy.api.dto;

import java.io.Serializable;

import com.paymybuddy.api.entity.BankAccount;

import lombok.Data;

@Data
public class BankAccountDto implements Serializable {

	private static final long serialVersionUID = -1393153834431798076L;

	public BankAccountDto() {
		super();
	}

	public BankAccountDto(BankAccount ba) {
		this.bankName = ba.getBankName();
		String number = "" + ba.getAccountNumber();
		this.accountNumber = Character.toString(number.charAt(0)) + Character.toString(number.charAt(1))
				+ Character.toString(number.charAt(2)) + "...";
	}

	private String bankName;
	private String accountNumber;

}
