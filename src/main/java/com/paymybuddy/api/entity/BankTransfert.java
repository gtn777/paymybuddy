
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@Table(name = "bank_transfert")
public class BankTransfert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_transfert_id", nullable = false, unique = true)
    private int id;

    @ManyToOne(targetEntity = User.class,
	       cascade = { CascadeType.MERGE, CascadeType.PERSIST },
	       fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @EqualsAndHashCode.Exclude
    @OneToOne(targetEntity = BankAccount.class,
	      cascade = { CascadeType.MERGE, CascadeType.PERSIST },
	      fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_account_id", referencedColumnName = "bank_account_id")
    private BankAccount bankAccount;

    private Boolean isUserSender;

    private int amount;

}
