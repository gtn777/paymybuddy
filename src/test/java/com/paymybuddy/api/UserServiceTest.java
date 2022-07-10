
package com.paymybuddy.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paymybuddy.api.dto.UserDto;
import com.paymybuddy.api.entity.User;
import com.paymybuddy.api.repository.UserRepository;
import com.paymybuddy.api.service.UserService;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private User user;

    private UserDto userDto;

    private final String username = "test@mail.com";

    private final String password = "passtest";

    @BeforeEach
    public void beforeEach() {
	user = new User();
	user.setUsername(username);
	user.setPassword(password);
	userDto = new UserDto(user);
    }

    @AfterEach
    public void afterEach() {
	
    }
    
    
    

}
