
package com.paymybuddy.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.paymybuddy.api.entity.BankAccount;
import com.paymybuddy.api.entity.BuddyPayment;
import com.paymybuddy.api.entity.User;

import lombok.Data;


@Data
public class UserDto implements Serializable {

    private static final long serialVersionUID = 7966756052250948200L;

    public UserDto() { super(); }

    public UserDto(User user) {
	this.username = user.getUsername();
	this.password = user.getPassword();
	this.role = user.getRole();
	this.balance = user.getBalance();
	this.role = user.getRole();
	this.enabled = user.isEnabled();
	this.bankAccount = user.getBankAccount() == null ? null : user.getBankAccount();
	for (BuddyPayment bp : user.getAllMyBuddyPayments()) {
	    this.allBuddyPaymentDtoList.add(new BuddyPaymentDto(bp));
	}
	Set<String> usernameSet = new HashSet<String>();
	for (User u : user.getAllBuddies()) {
	    usernameSet.add(u.getUsername());
	}
	this.friendList = new ArrayList<String>(usernameSet);
    }

    private String username;

    private String password;

    private float balance;

    private String role;

    private boolean enabled;

    private List<BuddyPaymentDto> allBuddyPaymentDtoList;

    private List<String> friendList;

    private BankAccount bankAccount;

}
