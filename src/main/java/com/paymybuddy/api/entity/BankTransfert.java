//package com.paymybuddy.api.entity;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//import lombok.Data;
//
//@Entity
//@Data
//@Table(name = "bank_transfert")
//public class BankTransfert {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "bank_account_id", nullable = false, unique = true)
//	private int id;
//
//	private BankAccount bankAccount;
//
//	private User user;
//
//	private Boolean isUserSender;
//	
//	private int amount;
//
//}
