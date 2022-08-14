
package com.paymybuddy.api.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.api.entity.BankAccount;
import com.paymybuddy.api.entity.User;
import com.paymybuddy.api.exception.NotEnoughMoneyException;
import com.paymybuddy.api.exception.UnknownUserException;
import com.paymybuddy.api.repository.UserRepository;


@Service
@Transactional
public class BankAccountService {

    @Autowired
    UserRepository userRepository;

    public boolean addBankAccount(String username, String bankName, long accountNumber) {
	User user = this.getUserEntityByUsername(username);
	BankAccount newBankAccount = new BankAccount(bankName, accountNumber);
	user.setBankAccount(newBankAccount);
	return true;
    }

    public boolean deleteBankAccount(String username) {
	User user = this.getUserEntityByUsername(username);
	user.setBankAccount(null);
	return true;
    }

    public boolean sendMoneyToBank(String username, float amount) {
	User user = this.getUserEntityByUsername(username);
	float currentBalance = user.getBalance();
	if (currentBalance >= amount) {
	    user.setBalance((user.getBalance()) - amount);
	    return true;
	} else {
	    throw new NotEnoughMoneyException();
	}
    }

    public boolean receiveMoneyFromBank(String username, float amount) {
	User user = this.getUserEntityByUsername(username);
	float currentBalance = user.getBalance();
	user.setBalance(currentBalance + amount);
	return true;
    }

    private User getUserEntityByUsername(String username) {
	Optional<User> optUser = userRepository.findByUsername(username);
	if (optUser.isEmpty()) {
	    throw new UnknownUserException(username);
	} else {
	    return optUser.get();
	}
    }

}
