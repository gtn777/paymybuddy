
package com.paymybuddy.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.paymybuddy.api.controller.UserController;
import com.paymybuddy.api.dto.LoginIdDto;
import com.paymybuddy.api.dto.UserDto;
import com.paymybuddy.api.entity.User;
import com.paymybuddy.api.exception.UnknownUserException;
import com.paymybuddy.api.repository.UserRepository;
import com.paymybuddy.api.service.UserService;


@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
//    private static Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    DataSource dataSource;

    @MockBean
    UserRepository userRepository;

    private final String username = "bob@mail.com";

    private final String password = "passbob";

    private User user;

    private LoginIdDto loginIdDto;

    private UserDto userDto;

    @BeforeEach
    public void beforeEach() {
	user = new User();
	user.setUsername(username);
	user.setPassword(password);
	user.setEnabled(true);
	user.setRole("USER");
	loginIdDto = new LoginIdDto();
	loginIdDto.setUsername(username);
	loginIdDto.setPassword(password);
	userDto = new UserDto(user);
    }

    @WithMockUser(username)
    @Test
    public void getUser_thenResponseStatusAndResultOk() throws Exception {
	when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
	when(userService.getUserDtoByUsername(username)).thenReturn(userDto);
	mockMvc.perform(get("/user?username=" + username))
	.andExpect(status().isOk())
	.andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @WithMockUser(username)
    @Test
    public void getUser_withParameterFaulty_thenIsBadRequestStatus() throws Exception {
	mockMvc.perform(get("/user?username")).andExpect(status().isBadRequest());
	mockMvc.perform(get("/user")).andExpect(status().isBadRequest());
	mockMvc.perform(get("/user?")).andExpect(status().isBadRequest());
    }

    @WithMockUser(username)
    @Test
    public void getUser_withUnknwonUserException_thenIsNotFoundStatus() throws Exception {
	when(userService.getUserDtoByUsername(username))
	.thenThrow(new UnknownUserException(username));
	mockMvc.perform(get("/user?username=bob@mail.com")).andExpect(status().isNotFound());
    }

    @WithMockUser(username)
    @Test
    public void postNewUser_httpStatusIsCreated_thenReturnUserDto() throws Exception {
	when(userService.createUser(loginIdDto)).thenReturn(userDto);
	mockMvc
	.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
	.content(new ObjectMapper().writeValueAsString(loginIdDto)))
	.andExpect(status().isCreated())
	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	.andExpect(jsonPath("$.username").value(username));
    }

    @WithMockUser(username)
    @Test
    public void postNewUser_withBodyFaultyBadEmail_thenHttpStatusIsBadRequest() throws Exception {
	loginIdDto.setUsername("a@a");
	mockMvc
	.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
	.content(new ObjectMapper().writeValueAsString(loginIdDto)))
	.andExpect(status().isBadRequest());
    }

    @WithMockUser(username)
    @Test
    public void postNewUser_withBodyFaultyPassword_thenHttpStatusIsBadRequest() throws Exception {
	loginIdDto.setPassword("ae");
	mockMvc
	.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
	.content(new ObjectMapper().writeValueAsString(loginIdDto)))
	.andExpect(status().isBadRequest());
    }

    @WithMockUser(username)
    @Test
    public void addNewFriend_thenStatusIsCreated() throws Exception {
	mockMvc.perform(post("/user/buddy?username=bob@mail.com&friend=franck@mail.com"))
	.andExpect(status().isCreated());
    }

    @WithMockUser(username)
    @Test
    public void getUserBuddiesList_thenResponseStatusAndResultOk() throws Exception {
	List<String> userBuddyList = new ArrayList<String>();
	userBuddyList.add("buddyPaul");
	userBuddyList.add("friend321");
	when(userService.getBuddyList(username)).thenReturn(userBuddyList);
	mockMvc.perform(get("/user/buddy?username=" + username))
	.andExpect(status().isOk())
	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	.andExpect(jsonPath("$").value(userBuddyList));
    }
//    @Test
//    public void postNewUser_withEmailAlreadyUsed_thenHttpStatusIsConflict() throws Exception {
//	when(userService.createUser(loginIdDto)).thenThrow(new SQLIntegrityConstraintViolationException()) ;
//	mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
//		.content(new ObjectMapper().writeValueAsString(loginIdDto)))
//		.andExpect(status().isConflict());
//    }
//
//    @Test
//    public void createUser_withBadRequest_emailIsInvalid() throws Exception {
//	userProfilDto.setEmail("@fr");
//	mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
//		.content(new ObjectMapper().writeValueAsString(userProfilDto)))
//		.andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void createUser_withBadRequest_emailIsEmpty() throws Exception {
//	userProfilDto.setEmail("");
//	mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
//		.content(new ObjectMapper().writeValueAsString(userProfilDto)))
//		.andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void createUser_withBadRequest_passwordIsNull() throws Exception {
//	userProfilDto.setPassword(null);
//	mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
//		.content(new ObjectMapper().writeValueAsString(userProfilDto)))
//		.andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void createUser_withBadRequest_passwordIsInvalid() throws Exception {
//	userProfilDto.setPassword("ab34");
//	mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
//		.content(new ObjectMapper().writeValueAsString(userProfilDto)))
//		.andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void createUser_withBadRequest_passwordIsEmpty() throws Exception {
//	userProfilDto.setPassword("");
//	mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
//		.content(new ObjectMapper().writeValueAsString(userProfilDto)))
//		.andExpect(status().isBadRequest());
//    }

}
