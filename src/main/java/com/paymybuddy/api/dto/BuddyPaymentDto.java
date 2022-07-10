
package com.paymybuddy.api.dto;

import java.io.Serializable;

import com.paymybuddy.api.entity.BuddyPayment;

import lombok.Data;


@Data
public class BuddyPaymentDto implements Serializable {

    private static final long serialVersionUID = -8451958447382834717L;

    public BuddyPaymentDto() { super(); }

    public BuddyPaymentDto(BuddyPayment bp) {
	super();
	this.sender = bp.getSenderUser().getUsername();
	this.receiver = bp.getRecipientUser().getUsername();
	this.amount = bp.getAmount();
    }

    private String sender;

    private String receiver;

    private float amount;

}
