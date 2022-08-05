
package com.paymybuddy.api.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.paymybuddy.api.dto.ConnectionDto;
import com.paymybuddy.api.dto.LoginDto;
import com.paymybuddy.api.dto.UserDto;
import com.paymybuddy.api.entity.User;
import com.paymybuddy.api.exception.EmailAlreadyUsedException;
import com.paymybuddy.api.exception.FriendshipAlreadyKnwon;
import com.paymybuddy.api.exception.UnknownUserException;
import com.paymybuddy.api.repository.UserRepository;

@Service
@Transactional
public class UserService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserDto getUserDtoByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isEmpty()) {
			throw new UnknownUserException(username);
		} else {
			return new UserDto(user.get());
		}
	}

	public UserDto createUser(LoginDto dto) {
		if (!userRepository.findByUsername(dto.getUsername()).isEmpty()) {
			throw new EmailAlreadyUsedException(dto.getUsername());
		} else {
			User newUser = new User();
			newUser.setUsername(dto.getUsername());
			newUser.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
			return new UserDto(userRepository.save(newUser));
		}
	}

	public List<String> getBuddyList(String username) {
		UserDto userDto = getUserDtoByUsername(username);
		return userDto.getFriendList();
	}

	public ConnectionDto addNewBuddy(String username, String friend) {
		User user = this.getUserEntityByUsername(username);
		User newFriend = this.getUserEntityByUsername(friend);
		if (user.getAllBuddies().contains(newFriend)) {
			throw new FriendshipAlreadyKnwon(username, friend);
		} else {
			user.getFriends().add(newFriend);
			return new ConnectionDto(user.getUsername(), newFriend.getUsername());
		}
	}

	public void sendMoneyToBank(String username, float amount) {
		User user = this.getUserEntityByUsername(username);
		float currentBalance = user.getBalance();
		user.setBalance(currentBalance - amount);
	}

	public void receiveMoneyFromBank(String username, float amount) {
		User user = this.getUserEntityByUsername(username);
		float currentBalance = user.getBalance();
		user.setBalance(currentBalance + amount);
	}

	public Iterable<UserDto> getAllUser() {
		Set<UserDto> result = new HashSet<UserDto>();
		Iterable<User> allUser = userRepository.findAll();
		for (User u : allUser) {
			result.add(new UserDto(u));
		}
		return result;
	}

	public void addManyUser() {
		List<String> list = new ArrayList<String>();
		list.add("mike@mail.com");
		list.add("bob@mail.com");
		list.add("julie@mail.com");
		list.add("franck@mail.com");
		for (String username : list) {
			UserDto newUserDto = this.createUser(new LoginDto(username, "pass"));
			this.receiveMoneyFromBank(newUserDto.getUsername(), 100);
		}

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
