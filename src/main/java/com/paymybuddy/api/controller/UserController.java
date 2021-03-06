
package com.paymybuddy.api.controller;

import java.security.Principal;
import java.util.Map;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paymybuddy.api.configuration.MyUserDetailsImpl;
import com.paymybuddy.api.dto.FriendshipDto;
import com.paymybuddy.api.dto.LoginIdDto;
import com.paymybuddy.api.dto.UserDto;
import com.paymybuddy.api.service.UserService;
import com.paymybuddy.api.util.GitHubApiUtil;


@RestController
public class UserController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    private Principal principal = null;

    private String currentUsername = null;

    public UserController(OAuth2AuthorizedClientService authorizedClientService) {
	this.authorizedClientService = authorizedClientService;
    }

    @Autowired
    private UserService userService;

    @Autowired
    private GitHubApiUtil gitHubApiUtil;

    @GetMapping(value = "/home")
    public Object getHome() throws Throwable {
	ModelAndView mav = new ModelAndView("profile");
	UserDto dto = userService.getUserDtoByUsername(currentUsername);
	mav.addObject("dto", dto);
	mav.setStatus(HttpStatus.OK);
	return mav;
    }

    @GetMapping(value = "/profile")
    public String getUserInfo() throws Throwable {
	StringBuffer userInfo = new StringBuffer();
	this.principal = SecurityContextHolder.getContext().getAuthentication();
	Principal user = this.principal;
	if (user instanceof UsernamePasswordAuthenticationToken) {
	    userInfo.append(getUsernamePasswordLoginInfo(user));
	    return "getUserInfo:user instanceof  UsernamePasswordAuthenticationToken " + "<br><br>"
		+ "getUsernamePasswordLoginInfo(user) return: " + userInfo.toString() + "<br><br>"
		+ "fin du return " + "<br><br>" + "user.toString():" + user.toString() + "<br><br>"
		+ "user.getName(): " + user.getName() + "<br><br>"
		+ "SecurityContextHolder.getContext().getAuthentication().getDetails(): "
		+ SecurityContextHolder.getContext().getAuthentication().getDetails();
	} else if (user instanceof OAuth2AuthenticationToken) {
	    userInfo.append(getOauth2LoginInfo(user));
	    return "getUserInfo:user instanceof  OAuth2AuthenticationToken " + "<br><br>"
		+ "getOauth2LoginInfo(user) return:" + userInfo.toString() + "<br><br>"
		+ "fin du return " + "<br><br>" + "user.toString():" + user.toString() + "<br><br>"
		+ "user.getName(): " + user.getName() + "<br><br>"
		+ "SecurityContextHolder.getContext().getAuthentication().getDetails(): "
		+ SecurityContextHolder.getContext().getAuthentication().getDetails();
	} else {
	    return "getUserInfo: else... " + "<br><br>" + userInfo.toString() + "<br><br>"
		+ user.toString();
	}
    }

    private StringBuffer getUsernamePasswordLoginInfo(Principal user) throws Throwable {
	StringBuffer loginInfo = new StringBuffer();
	UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) user);
	if (token.isAuthenticated()) {
	    MyUserDetailsImpl userDetailsImpl = (MyUserDetailsImpl) token.getPrincipal();
	    loginInfo.append(
		"Welcome, userDetailsImpl.getUsername():" + userDetailsImpl.getUsername()
		    + "<br><br>" + " OU user.getName():" + user.getName() + "<br><br>"
		    + " OU token.getName():" + token.getName() + "<br><br>");
	    loginInfo.append(" Access Token: " + token + "<br><br>");
	    this.currentUsername = userDetailsImpl.getUsername();
	} else {
	    loginInfo.append("NA");
	}
	return loginInfo;
    }

    private StringBuffer getOauth2LoginInfo(Principal user) throws Throwable {
	StringBuffer loginInfo = new StringBuffer();
	OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) user);
	OAuth2AuthorizedClient authClient = this.authorizedClientService.loadAuthorizedClient(
	    authToken.getAuthorizedClientRegistrationId(),
	    authToken.getName());
	if (authToken.isAuthenticated()) {
	    Map<String, Object> userAttributes = ((DefaultOAuth2User) authToken.getPrincipal())
		.getAttributes();
	    String userToken = authClient.getAccessToken().getTokenValue();
	    loginInfo.append(
		"Welcome, userAttributes.get(\"name\"): " + userAttributes.get("name") + "<br><br>"
		    + " OU user.getName():" + user.getName() + "<br><br>"
		    + " OU authClient.getPrincipalName():" + authClient.getPrincipalName()
		    + "<br><br>");
	    // -- if user sign in with github, need to retrieve user account email with api
	    // request
	    if (userAttributes.containsKey("url")
		&& userAttributes.get("url").toString().contains("github.com")) {
		String email = gitHubApiUtil.getGitUserEmail(authClient);
		this.currentUsername = email;
		loginInfo.append("Github mail: " + email + "<br><br>");
	    } else {
		String email = (String) userAttributes.get("email");
		this.currentUsername = email;
		loginInfo.append("Google email: " + email + "<br><br>");
	    }
	    loginInfo.append("Access Token: " + userToken + "<br><br>");
//	    protectedInfo.append("origin:"+ userAttributes.get() );
	} else {
	    loginInfo.append("NA");
	    throw new LoginException("LOGGIN Failure");
	}
	return loginInfo;
    }

    @SuppressWarnings("unused")
    private OidcIdToken getIdToken(OAuth2User principal) throws Throwable {
	if (principal instanceof DefaultOidcUser) {
	    DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
	    return oidcUser.getIdToken();
	}
	return null;
    }
    // ---------------------------------------------------------------------------------------------------------

    @GetMapping("/user/all")
    public Iterable<UserDto> getAllUserProfil() throws Throwable {
	return userService.getAllUser();
    }

    @GetMapping("/init")
    public Iterable<UserDto> addDataToDB() throws Throwable { return userService.addManyUser(); }

    @PostMapping(path = "/user")
    public ResponseEntity<Object> createUser(@RequestBody LoginIdDto dto) throws Throwable {
	if (dto.getUsername() == null || dto.getUsername().length() < 4) {
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		.body("Entered value for email is incorrect.");
	} else if (dto.getPassword().length() < 5 || dto.getPassword() == null) {
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		.body("Entered value for password is incorrect.");
	} else {
	    UserDto result = userService.createUser(dto);
	    return new ResponseEntity<>(result, HttpStatus.CREATED);
	}
    }

    @GetMapping(path = "/user/buddy")
    public ResponseEntity<Object> getUserBuddyList(@RequestParam String username) throws Throwable {
	return new ResponseEntity<>(userService.getBuddyList(username), HttpStatus.OK);
    }

    @PostMapping(path = "/user/buddy")
    public ResponseEntity<Object> addNewBuddy(@RequestParam String username,
	@RequestParam String friend) throws Throwable {
	FriendshipDto result = userService.addNewBuddy(username, friend);
	return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

}
