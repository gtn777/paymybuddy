
package com.paymybuddy.api.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.api.dto.BuddyPaymentDto;
import com.paymybuddy.api.entity.BuddyPayment;
import com.paymybuddy.api.entity.User;
import com.paymybuddy.api.exception.NotEnoughMoneyException;
import com.paymybuddy.api.exception.UnknownUserException;
import com.paymybuddy.api.repository.BuddyPaymentRepository;
import com.paymybuddy.api.repository.UserRepository;

@Service
@Transactional
public class BuddyPaymentService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	BuddyPaymentRepository buddyPaymentRepository;

	public BuddyPaymentDto createABuddyPayment(BuddyPaymentDto dto) {
		User sender = this.getUserEntityByUsername(dto.getSenderUsername());
		User receiver= this.getUserEntityByUsername(dto.getReceiverUsername());
		float amount = dto.getAmount();
		String description = dto.getDescription();
		if (sender.getBalance() > amount) {
			sender.setBalance((sender.getBalance()) - amount);
			receiver.setBalance((receiver.getBalance()) + amount);
			BuddyPayment newPayment = new BuddyPayment(sender, receiver, amount, description);
			BuddyPayment result = buddyPaymentRepository.save(newPayment);
			return new BuddyPaymentDto(result);
		} else {
			throw new NotEnoughMoneyException();
		}
	}

	private User getUserEntityByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isEmpty()) {
			throw new UnknownUserException(username);
		} else {
			return user.get();
		}
	}
}
