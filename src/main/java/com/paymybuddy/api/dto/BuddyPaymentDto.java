
package com.paymybuddy.api.dto;

import java.io.Serializable;

import com.paymybuddy.api.entity.BuddyPayment;

import lombok.Data;

@Data
public class BuddyPaymentDto implements Serializable {

	private static final long serialVersionUID = -8451958447382834717L;

	public BuddyPaymentDto() {
		super();
	}

	public BuddyPaymentDto(BuddyPayment bp) {
		this.senderUsername = bp.getSenderUser().getUsername();
		this.receiverUsername = bp.getReceiverUser().getUsername();
		this.amount = bp.getAmount();
	}

	public BuddyPaymentDto(String authenticatedUsername, String buddy, Float amount) {
		this.senderUsername = authenticatedUsername;
		this.receiverUsername = buddy;
		this.amount = amount;
	}

	private String senderUsername;

	private String receiverUsername;

	private float amount;

}
