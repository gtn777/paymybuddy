
package com.paymybuddy.api.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Entity
@Table(name = "user")
public class User {

    public User() { super(); }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    private int id;

    @Column(name = "username", nullable = false, unique = true, length = 127)
    private String username;

    @Column(name = "password", nullable = true, length = 127)
    private String password;

    @Column(name = "balance", nullable = false)
    private float balance = 0;

    @Column(name = "role", nullable = false)
    private String role = "ROLE_USER";

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.EAGER)
    @JoinTable(name = "connection",
	       joinColumns = @JoinColumn(name = "user_id"),
	       inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private Set<User> friends = new HashSet<User>();

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.EAGER)
    @JoinTable(name = "connection",
	       joinColumns = @JoinColumn(name = "friend_id"),
	       inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> friendOf = new HashSet<User>();

    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "senderUser",
	       cascade = { CascadeType.MERGE, CascadeType.PERSIST },
	       fetch = FetchType.EAGER)
    private Set<BuddyPayment> receivedBuddyPayments = new HashSet<BuddyPayment>();

    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "receiverUser",
	       cascade = { CascadeType.MERGE, CascadeType.PERSIST },
	       fetch = FetchType.EAGER)
    private Set<BuddyPayment> sentBuddyPayments = new HashSet<BuddyPayment>();

    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user",
	       cascade = { CascadeType.MERGE, CascadeType.PERSIST },
	       fetch = FetchType.EAGER)
    private Set<BankTransfert> bankTransferts = new HashSet<BankTransfert>();

    @EqualsAndHashCode.Exclude
    @OneToOne(targetEntity = BankAccount.class,
	      orphanRemoval = true,
	      cascade = { CascadeType.MERGE, CascadeType.PERSIST },
	      fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_account_id", referencedColumnName = "bank_account_id")
    private BankAccount bankAccount;

    public List<User> getAllBuddies() {
	Set<User> myBuddiesSet = new LinkedHashSet<>(this.getFriends());
	myBuddiesSet.addAll(this.getFriendOf());
	List<User> myBuddiesList = new ArrayList<>(myBuddiesSet);
	return myBuddiesList;
    }

    public Set<BuddyPayment> getAllMyBuddyPayments() {
	Set<BuddyPayment> allMyBuddyPayments = new HashSet<BuddyPayment>();
	allMyBuddyPayments.addAll(this.receivedBuddyPayments);
	allMyBuddyPayments.addAll(this.sentBuddyPayments);
	return allMyBuddyPayments;
    }

}
