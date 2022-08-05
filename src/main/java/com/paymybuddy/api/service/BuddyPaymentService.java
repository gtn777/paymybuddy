
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
		if (sender.getBalance() > amount) {
			sender.setBalance((sender.getBalance()) - amount);
			receiver.setBalance((receiver.getBalance()) + amount);
			BuddyPayment newPayment = new BuddyPayment(sender, receiver, amount);
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
	/*
	 * 
	 * 
	 * 
	 * 
	 * */
//
//	public UserDto getUserDtoByUsername(String username) {
//		Optional<User> user = userRepository.findByUsername(username);
//		if (user.isEmpty()) {
//			throw new UnknownUserException(username);
//		} else {
//			return new UserDto(user.get());
//		}
//	}
//
//	public UserDto createUser(LoginDto dto) {
//		if (!userRepository.findByUsername(dto.getUsername()).isEmpty()) {
//			throw new EmailAlreadyUsedException(dto.getUsername());
//		} else {
//			User newUser = new User();
//			newUser.setUsername(dto.getUsername());
//			newUser.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
//			return new UserDto(userRepository.save(newUser));
//		}
//	}
//
//	public List<String> getBuddyList(String username) {
//		UserDto userDto = getUserDtoByUsername(username);
//		return userDto.getFriendList();
//	}
//
//	public ConnectionDto addNewBuddy(String username, String friend) {
//		User user = this.getUserEntityByUsername(username);
//		User newFriend = this.getUserEntityByUsername(friend);
//		if (user.getAllBuddies().contains(newFriend)) {
//			throw new FriendshipAlreadyKnwon(username, friend);
//		} else {
//			user.getFriends().add(newFriend);
//			return new ConnectionDto(user.getUsername(), newFriend.getUsername());
//		}
//	}
//
//	public void sendMoneyToBank(String username, float transfertAmount) {
//	}
//
//	public void receiveMoneyFromBank(String username, float transfertAmount) {
//	}
//
//	public Iterable<UserDto> getAllUser() {
//		Set<UserDto> result = new HashSet<UserDto>();
//		Iterable<User> allUser = userRepository.findAll();
//		for (User u : allUser) {
//			result.add(new UserDto(u));
//		}
//		return result;
//	}
//
//	public Iterable<UserDto> addManyUser() {
//		this.createUser(new LoginDto("mike@mail.com", "passmike"));
//		this.createUser(new LoginDto("bob@mail.com", "passbob"));
//		this.createUser(new LoginDto("julie@mail.com", "passjulie"));
//		return null;
//	}

}
