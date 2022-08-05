
package com.paymybuddy.api.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paymybuddy.api.dto.BuddyPaymentDto;
import com.paymybuddy.api.dto.LoginDto;
import com.paymybuddy.api.dto.UserDto;
import com.paymybuddy.api.service.BuddyPaymentService;
import com.paymybuddy.api.service.UserService;
import com.paymybuddy.api.util.GitHubUserUtil;

@RestController
public class UserController {

	private final OAuth2AuthorizedClientService authorizedClientService;

	private Principal principal = null;

	public UserController(OAuth2AuthorizedClientService authorizedClientService) {
		this.authorizedClientService = authorizedClientService;
	}

	@Autowired
	private UserService userService;

	@Autowired
	private BuddyPaymentService buddyPaymentService;

	@Autowired
	private GitHubUserUtil gitHubUserUtil;

	@GetMapping(value = "/")
	public Object getIndexDefaultPage(Authentication auth) throws Throwable {
		return this.getHomePage(auth);
	}

	@GetMapping(value = "/home")
	public Object getHomePage(Authentication auth) throws Throwable {
		ModelAndView mav = new ModelAndView("home");
		if (auth != null) {
			UserDto dto = userService.getUserDtoByUsername(getAuthenticatedUsername(auth));
			mav.addObject("dto", dto);
		}
		mav.setStatus(HttpStatus.OK);
		return mav;
	}

	@GetMapping(value = "/createAccount")
	public Object getCreateAccountPage(@ModelAttribute(value = "logindto") LoginDto dto) {
		ModelAndView mav = new ModelAndView("createAccount");
		mav.setStatus(HttpStatus.OK);
		return mav;
	}

	@PostMapping(path = "/user")
	public Object createUserAccount(@ModelAttribute(value = "logindto") LoginDto dto) throws Throwable {
		if (dto.getUsername() == null || dto.getUsername().length() < 4) {
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Entered value for email is incorrect.");
		} else if (dto.getPassword().length() < 4 || dto.getPassword() == null) {
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Entered value for password is incorrect.");
		} else {
			UserDto result = userService.createUser(dto);
			if (result instanceof UserDto && result.getUsername() == dto.getUsername()) {
				ModelAndView mav = new ModelAndView("newUser");
				mav.setStatus(HttpStatus.CREATED);
				return mav;
			} else {
				ModelAndView mav = new ModelAndView("createAccount");
				mav.setStatus(HttpStatus.BAD_REQUEST);
				return mav;
			}
		}
	}

	@GetMapping(value = "/profile")
	public Object getProfilePage(Authentication auth) throws Throwable {
		ModelAndView mav = new ModelAndView("profile");
		UserDto dto = userService.getUserDtoByUsername(getAuthenticatedUsername(auth));
		mav.addObject("dto", dto);
		mav.setStatus(HttpStatus.OK);
		return mav;
	}

	@GetMapping(value = "/transfert")
	public Object getTransfertPage(Authentication auth) throws Throwable {
		ModelAndView mav = new ModelAndView("transfert");
		UserDto userDto = userService.getUserDtoByUsername(getAuthenticatedUsername(auth));
		mav.addObject("userDto", userDto);
		mav.setStatus(HttpStatus.OK);
		return mav;
	}

	@PostMapping(value = "/transfert/connection")
	public Object addNewBuddy(Authentication auth, @ModelAttribute(value = "buddy") String buddy)
			throws Throwable {
		userService.addNewBuddy(getAuthenticatedUsername(auth), buddy);
		ModelAndView mav = new ModelAndView("transfert");
		UserDto userDto = userService.getUserDtoByUsername(getAuthenticatedUsername(auth));
		mav.addObject("userDto", userDto);
		mav.setStatus(HttpStatus.OK);
		return mav;
	}

	@PostMapping(value = "/transfert/payment")
	public Object postNewBuddy(Authentication auth, @ModelAttribute(value = "buddy") String buddy,
			@ModelAttribute(value = "amount") float amount) throws Throwable {
		BuddyPaymentDto buddyPaymentDto = new BuddyPaymentDto(this.getAuthenticatedUsername(auth),
				buddy,
				amount);
		buddyPaymentService.createABuddyPayment(buddyPaymentDto);
		ModelAndView mav = new ModelAndView("transfert");
		UserDto userDto = userService.getUserDtoByUsername(getAuthenticatedUsername(auth));
		mav.addObject("userDto", userDto);
		mav.setStatus(HttpStatus.OK);
		return mav;
	}

	/*
	 * 
	 * 
	 * 
	 * */
	private String getAuthenticatedUsername(Authentication auth) {
		if (auth instanceof OAuth2AuthenticationToken) {
			return ((OAuth2AuthenticationToken) auth).getPrincipal().getAttributes().get("email").toString();
		} else if (auth instanceof UsernamePasswordAuthenticationToken) {
			return auth.getName();
		} else {
			return null;
		}
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * */
	/*
	 * public String getUserInfo() throws Throwable { StringBuffer userInfo = new
	 * StringBuffer(); this.principal =
	 * SecurityContextHolder.getContext().getAuthentication(); Principal user =
	 * this.principal; if (user instanceof UsernamePasswordAuthenticationToken) {
	 * userInfo.append(getUsernamePasswordLoginInfo(user)); return "<br><br>" +
	 * "getUserInfo:user instanceof  UsernamePasswordAuthenticationToken " +
	 * "<br><br>" + "getUsernamePasswordLoginInfo(user) return: " +
	 * userInfo.toString() + "<br><br>" + "fin du return " + "<br><br>" +
	 * "user.toString():" + user.toString() + "<br><br>" + "user.getName(): " +
	 * user.getName() + "<br><br>" +
	 * "SecurityContextHolder.getContext().getAuthentication().getDetails(): " +
	 * SecurityContextHolder.getContext().getAuthentication().getDetails(); } else
	 * if (user instanceof OAuth2AuthenticationToken) {
	 * userInfo.append(getOauth2LoginInfo(user)); return "<br><br>" +
	 * "getUserInfo:user instanceof  OAuth2AuthenticationToken " + "<br><br>" +
	 * "getOauth2LoginInfo(user) return:" + userInfo.toString() + "<br><br>" +
	 * "fin du return " + "<br><br>" + "user.toString():" + user.toString() +
	 * "<br><br>" + "user.getName(): " + user.getName() + "<br><br>" +
	 * "SecurityContextHolder.getContext().getAuthentication().getDetails(): " +
	 * SecurityContextHolder.getContext().getAuthentication().getDetails(); } else {
	 * return "" + "getUserInfo: else... " + "<br><br>" + userInfo.toString() +
	 * "<br><br>" + user.toString(); } }
	 * 
	 * private StringBuffer getUsernamePasswordLoginInfo(Principal user) throws
	 * Throwable { StringBuffer loginInfo = new StringBuffer();
	 * UsernamePasswordAuthenticationToken token =
	 * ((UsernamePasswordAuthenticationToken) user); if (token.isAuthenticated()) {
	 * MyUserDetailsImpl userDetailsImpl = (MyUserDetailsImpl) token.getPrincipal();
	 * loginInfo .append("Welcome, userDetailsImpl.getUsername():" +
	 * userDetailsImpl.getUsername() + "<br><br>" + " OU user.getName():" +
	 * user.getName() + "<br><br>" + " OU token.getName():" + token.getName() +
	 * "<br><br>"); loginInfo.append(" Access Token: " + token + "<br><br>"); //
	 * this.currentUsername = userDetailsImpl.getUsername(); } else {
	 * loginInfo.append("NA"); } return loginInfo; }
	 * 
	 * private StringBuffer getOauth2LoginInfo(Principal user) throws Throwable {
	 * StringBuffer loginInfo = new StringBuffer(); OAuth2AuthenticationToken
	 * authToken = ((OAuth2AuthenticationToken) user); OAuth2AuthorizedClient
	 * authClient = this.authorizedClientService
	 * .loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(),
	 * authToken.getName()); if (authToken.isAuthenticated()) { Map<String, Object>
	 * userAttributes = ((DefaultOAuth2User) authToken.getPrincipal())
	 * .getAttributes(); String userToken =
	 * authClient.getAccessToken().getTokenValue(); loginInfo
	 * .append("Welcome, userAttributes.get(\"name\"): " +
	 * userAttributes.get("name") + "<br><br>" + " OU user.getName():" +
	 * user.getName() + "<br><br>" + " OU authClient.getPrincipalName(): " +
	 * authClient.getPrincipalName() + "<br><br>" +
	 * " OU authClient.getAccessToken(): " + authClient.getAccessToken() +
	 * "<br><br>" + " OU ((OAuth2AuthenticationToken)user): " +
	 * ((OAuth2AuthenticationToken) user) + "<br><br>" + "<br><br>" + "<br><br>");
	 * // GITHUB USER if (userAttributes.containsKey("url") &&
	 * userAttributes.get("url").toString().contains("github.com")) { String email =
	 * gitHubUserUtil .getGitHubUserBaseAndVerifiedEmail((OAuth2AuthorizedClient)
	 * user); // this.currentUsername = email; loginInfo.append("Github mail: " +
	 * email + "<br><br>"); } // GOOGLE USER else { String email = (String)
	 * userAttributes.get("email"); // this.currentUsername = email;
	 * loginInfo.append("Google email: " + email + "<br><br>"); }
	 * loginInfo.append("Access Token: " + userToken + "<br><br>"); //
	 * protectedInfo.append("origin:"+ userAttributes.get() ); } else {
	 * loginInfo.append("NA"); throw new LoginException("LOGGIN Failure"); } return
	 * loginInfo.append("------ fin getOauth2LoginInfo---------"); }
	 * 
	 * @SuppressWarnings("unused") private OidcIdToken getIdToken(OAuth2User
	 * principal) throws Throwable { if (principal instanceof DefaultOidcUser) {
	 * DefaultOidcUser oidcUser = (DefaultOidcUser) principal; return
	 * oidcUser.getIdToken(); } return null; } //
	 * -----------------------------------------------------------------------------
	 * ----------------------------
	 * 
	 * @GetMapping("/user/all") public Iterable<UserDto> getAllUserProfil() throws
	 * Throwable { return userService.getAllUser(); }
	 * 
	 * @GetMapping("/init") public Iterable<UserDto> addDataToDB() throws Throwable
	 * { return userService.addManyUser(); }
	 * 
	 * @GetMapping(path = "/user/buddy") public ResponseEntity<Object>
	 * getUserBuddyList(@RequestParam String username) throws Throwable { return new
	 * ResponseEntity<>(userService.getBuddyList(username), HttpStatus.OK); }
	 * 
	 * @PostMapping(path = "/user/buddy") public ResponseEntity<Object>
	 * addNewBuddy(@RequestParam String username, @RequestParam String friend)
	 * throws Throwable { FriendshipDto result = userService.addNewBuddy(username,
	 * friend); return new ResponseEntity<>(result, HttpStatus.CREATED); }
	 */
}
