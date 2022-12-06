
package com.paymybuddy.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.api.controller.BuddyPaymentController;
import com.paymybuddy.api.dto.ConnectionDto;
import com.paymybuddy.api.dto.LoginDto;
import com.paymybuddy.api.dto.UserDto;
import com.paymybuddy.api.entity.User;
import com.paymybuddy.api.repository.UserRepository;
import com.paymybuddy.api.service.BuddyPaymentService;
import com.paymybuddy.api.service.UserService;
import com.paymybuddy.api.util.GitHubUserUtil;


@WebMvcTest(controllers = BuddyPaymentController.class)
public class BuddyPaymentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    DataSource dataSource;

    @MockBean
    private BuddyPaymentService buddyPaymentService;

    @MockBean
    GitHubUserUtil gitHubUserUtil;

    private final String username = "junit@mail.com";

    private final String newBuddy = "buddytest@mail.com";

    private final String password = "junit";

    private User user;

    private LoginDto loginIdDto;

    private ConnectionDto connectionDto;

    private UserDto userDto;

    @BeforeEach
    public void beforeEach() {
	user = new User();
	user.setUsername(username);
	user.setPassword(password);
	user.setEnabled(true);
	user.setRole("USER");
	loginIdDto = new LoginDto();
	loginIdDto.setUsername(username);
	loginIdDto.setPassword(password);
	userDto = new UserDto(user);
	connectionDto = new ConnectionDto(username, newBuddy);
    }

    @Test
    @WithMockUser(username)
    public void getTransferPage_thenStatusIsOk() throws Exception {
	when(userService.getUserDtoByUsername(username)).thenReturn(userDto);
	mockMvc.perform(get("/transfer")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username)
    public void addNewFriend_thenStatusIsCreated() throws Exception {
	when(userService.addNewBuddy(username, newBuddy)).thenReturn(connectionDto);
	mockMvc
	.perform(post("/addConnection").contentType(MediaType.APPLICATION_JSON)
	.content(new ObjectMapper().writeValueAsString(newBuddy)))
	.andExpect(status().is3xxRedirection())
	.andExpect(redirectedUrl("/addConnection"));
    }

}
