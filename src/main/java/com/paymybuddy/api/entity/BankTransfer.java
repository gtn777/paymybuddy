
package com.paymybuddy.api.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@Table(name = "bank_transfer")
public class BankTransfer {

    
    public BankTransfer() { super(); }

    public BankTransfer(User user, BankAccount bankAccount, Boolean isUserSender, int amount) {
	super();
	this.user = user;
	this.bankAccount = bankAccount;
	this.isUserSender = isUserSender;
	this.amount = amount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_transfer_id", nullable = false, unique = true)
    private int id;

    @ManyToOne(targetEntity = User.class,
	       cascade = { CascadeType.MERGE, CascadeType.PERSIST },
	       fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne(targetEntity = BankAccount.class,
	       cascade = { CascadeType.MERGE, CascadeType.PERSIST },
	       fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_account_id", referencedColumnName = "bank_account_id")
    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    private BankAccount bankAccount;

    @Column(name = "is_user_sender", nullable = false)
    private Boolean isUserSender;

    @Column(name = "amount", nullable = false)
    private int amount;

}
