
package com.paymybuddy.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.paymybuddy.api.dto.FriendshipDto;
import com.paymybuddy.api.dto.LoginIdDto;
import com.paymybuddy.api.dto.UserDto;
import com.paymybuddy.api.entity.User;
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

    private User getUserEntityByUsername(String username) {
	Optional<User> user = userRepository.findByUsername(username);
	if (user.isEmpty()) {
	    throw new UnknownUserException(username);
	} else {
	    return user.get();
	}
    }

    public UserDto getUserDtoByUsername(String username) {
	Optional<User> user = userRepository.findByUsername(username);
	if (user.isEmpty()) {
	    throw new UnknownUserException(username);
	} else {
	    return new UserDto(user.get());
	}
//	String currentemail = getCurrentUsername();
    }

    public UserDto createUser(LoginIdDto dto) {
	User newUser = new User();
	newUser.setUsername(dto.getUsername());
	newUser.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
	return new UserDto(userRepository.save(newUser));
    }

    public List<String> getBuddyList(String username) {
	UserDto userDto = getUserDtoByUsername(username);
	return userDto.getFriendList();
    }

    public FriendshipDto addNewBuddy(String username, String friend) {
	User user = this.getUserEntityByUsername(username);
	User newFriend = this.getUserEntityByUsername(friend);
	if (user.getAllBuddies().contains(newFriend)) {
	    throw new FriendshipAlreadyKnwon(username, friend);
	} else {
	    user.getFriends().add(newFriend);
	    return new FriendshipDto(user.getUsername(), newFriend.getUsername());
	}
    }

    public void sendMoneyToBank(String username, float transfertAmount) {}

    public void receiveMoneyFromBank(String username, float transfertAmount) {}

    public Iterable<UserDto> getAllUser() {
	Set<UserDto> result = new HashSet<UserDto>();
	Iterable<User> allUser = userRepository.findAll();
	for (User u : allUser) {
	    result.add(new UserDto(u));
	}
	return result;
    }

    public Iterable<UserDto> addManyUser() {
	this.createUser(new LoginIdDto("mike@mail.com", "passmike"));
	this.createUser(new LoginIdDto("bob@mail.com", "passbob"));
	this.createUser(new LoginIdDto("julie@mail.com", "passjulie"));
	return null;
    }

}
