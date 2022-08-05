
package com.paymybuddy.api.entity;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "buddy_payment")
public class BuddyPayment {

	public BuddyPayment() {
		super();
	}

	public BuddyPayment(User sender, User receiver, float amount) {
		this.senderUser = sender;
		this.receiverUser = receiver;
		this.amount = amount;
		this.paymentDate = LocalDate.now();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "buddy_payment_id", nullable = false, unique = true)
	private int id;

	@EqualsAndHashCode.Exclude
	@ManyToOne(targetEntity = User.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "sender_user_id")
	@JsonManagedReference
	private User senderUser;

	@EqualsAndHashCode.Exclude
	@ManyToOne(targetEntity = User.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "recipient_user_id")
	@JsonManagedReference
	private User receiverUser;

	@Column(name = "amount", unique = false, nullable = false)
	private float amount;

	@Column(name = "payment_date", unique = false, nullable = false)
	private LocalDate paymentDate;

}
