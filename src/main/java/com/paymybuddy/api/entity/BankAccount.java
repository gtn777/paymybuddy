
package com.paymybuddy.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "bank_account")
public class BankAccount {

	public BankAccount() {
		super();
	}

	public BankAccount(String bankName, long accountNumber2) {
		super();
		this.bankName = bankName;
		this.accountNumber = accountNumber2;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bank_account_id", nullable = false, unique = true)
	private int id;

	@Column(name = "bank_name", nullable = false, length = 127)
	private String bankName;

	@Column(name = "account_number", nullable = false, unique = true, length = 15)
	private long accountNumber;

}
