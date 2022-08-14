
package com.paymybuddy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paymybuddy.api.dto.LoginDto;
import com.paymybuddy.api.dto.UserDto;
import com.paymybuddy.api.service.UserService;
import com.paymybuddy.api.util.GitHubUserUtil;


@Controller
public class UserController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public UserController(OAuth2AuthorizedClientService authorizedClientService) {
	this.authorizedClientService = authorizedClientService;
    }

    @Autowired
    private UserService userService;

    @Autowired
    private GitHubUserUtil gitHubUserUtil;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object getIndexDefaultPage(RedirectAttributes r) throws Throwable {
	return "redirect:/home";
    }

    @GetMapping(value = "/home")
    public Object getHomePage(Authentication auth) throws Throwable {
	if (auth != null) {
	    if (userService.isKnownUser(getAuthenticatedUsername(auth))) {
		ModelAndView mav = new ModelAndView("home");
		UserDto dto = userService.getUserDtoByUsername(getAuthenticatedUsername(auth));
		mav.addObject("dto", dto);
		mav.setStatus(HttpStatus.OK);
		return mav;
	    } else {
		userService.createSocialNetworkUserAccount(getAuthenticatedUsername(auth));
		return "redirect:/home";
	    }
	} else {
	    ModelAndView mav = new ModelAndView("home");
	    mav.setStatus(HttpStatus.OK);
	    return mav;
	}
    }

    @GetMapping(value = "/createAccount")
    public Object getCreateAccountPage(@ModelAttribute(value = "logindto") LoginDto dto) {
	ModelAndView mav = new ModelAndView("createAccount");
	mav.setStatus(HttpStatus.OK);
	return mav;
    }

    @PostMapping(path = "/createAccount")
    public Object createUserPasswordAccount(@ModelAttribute(value = "logindto") LoginDto dto)
    throws Throwable {
	userService.createPasswordAccount(dto);
	return "redirect:/newUser";
    }

    @GetMapping(value = "/newUser")
    public Object GetWelcomeNewUser() {
	ModelAndView mav = new ModelAndView("newUser");
	mav.setStatus(HttpStatus.CREATED);
	return mav;
    }

    @ResponseBody
    @GetMapping(value = "/profile")
    public Object getProfilePage(Authentication auth) throws Throwable {
	ModelAndView mav = new ModelAndView("profile");
	UserDto dto = userService.getUserDtoByUsername(getAuthenticatedUsername(auth));
	mav.addObject("dto", dto);
	mav.setStatus(HttpStatus.OK);
	return mav;
    }

    private String getAuthenticatedUsername(Authentication auth) {
	if (auth instanceof OAuth2AuthenticationToken) {
	    OAuth2User authUser = ((OAuth2AuthenticationToken) auth).getPrincipal();
	    OAuth2AuthorizedClient authClient = this.authorizedClientService.loadAuthorizedClient(
	    ((OAuth2AuthenticationToken) auth).getAuthorizedClientRegistrationId(),
	    authUser.getName());
	    if (authUser.getAttributes().containsKey("url")
	    && authUser.getAttributes().get("url").toString().contains("github.com")) {
		return gitHubUserUtil.getGitHubUserBaseAndVerifiedEmail(authClient);
	    } else {
		return authUser.getAttributes().get("email").toString();
	    }
	} else if (auth instanceof UsernamePasswordAuthenticationToken) {
	    return auth.getName();
	} else {
	    return null;
	}
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
//    public String getUserInfo() throws Throwable {
//	StringBuffer userInfo = new StringBuffer();
//	this.principal = SecurityContextHolder.getContext().getAuthentication();
//	Principal user = this.principal;
//	if (user instanceof UsernamePasswordAuthenticationToken) {
//	    userInfo.append(getUsernamePasswordLoginInfo(user));
//	    return "<br><br>" + "getUserInfo:user instanceof  UsernamePasswordAuthenticationToken "
//	    + "<br><br>" + "getUsernamePasswordLoginInfo(user) return: " + userInfo.toString()
//	    + "<br><br>" + "fin du return " + "<br><br>" + "user.toString():" + user.toString()
//	    + "<br><br>" + "user.getName(): " + user.getName() + "<br><br>"
//	    + "SecurityContextHolder.getContext().getAuthentication().getDetails(): "
//	    + SecurityContextHolder.getContext().getAuthentication().getDetails();
//	} else if (user instanceof OAuth2AuthenticationToken) {
//	    userInfo.append(getOauth2LoginInfo(user));
//	    return "<br><br>" + "getUserInfo:user instanceof  OAuth2AuthenticationToken "
//	    + "<br><br>" + "getOauth2LoginInfo(user) return:" + userInfo.toString() + "<br><br>"
//	    + "fin du return " + "<br><br>" + "user.toString():" + user.toString() + "<br><br>"
//	    + "user.getName(): " + user.getName() + "<br><br>"
//	    + "SecurityContextHolder.getContext().getAuthentication().getDetails(): "
//	    + SecurityContextHolder.getContext().getAuthentication().getDetails();
//	} else {
//	    return "" + "getUserInfo: else... " + "<br><br>" + userInfo.toString() + "<br><br>"
//	    + user.toString();
//	}
//    }
//    private StringBuffer getUsernamePasswordLoginInfo(Principal user) throws Throwable {
//	StringBuffer loginInfo = new StringBuffer();
//	UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) user);
//	if (token.isAuthenticated()) {
//	    MyUserDetailsImpl userDetailsImpl = (MyUserDetailsImpl) token.getPrincipal();
//	    loginInfo.append("Welcome, userDetailsImpl.getUsername():"
//	    + userDetailsImpl.getUsername() + "<br><br>" + " OU user.getName():" + user.getName()
//	    + "<br><br>" + " OU token.getName():" + token.getName() + "<br><br>");
//	    loginInfo.append(" Access Token: " + token + "<br><br>"); //
//	    this.currentUsername = userDetailsImpl.getUsername();
//	} else {
//	    loginInfo.append("NA");
//	}
//	return loginInfo;
//    }
//    private StringBuffer getOauth2LoginInfo(Principal user) throws Throwable {
//      StringBuffer loginInfo = new StringBuffer(); OAuth2AuthenticationToken authToken =
//      ((OAuth2AuthenticationToken) user); OAuth2AuthorizedClient authClient =
//      this.authorizedClientService
//      .loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(),
//      authToken.getName());
//      if (authToken.isAuthenticated()) { Map<String, Object>
//      userAttributes = ((DefaultOAuth2User) authToken.getPrincipal()) .getAttributes();
//      String userToken = authClient.getAccessToken().getTokenValue(); loginInfo
//      .append("Welcome, userAttributes.get(\"name\"): " + userAttributes.get("name") +
//      "<br><br>" + " OU user.getName():" + user.getName() + "<br><br>" +
//      " OU authClient.getPrincipalName(): " + authClient.getPrincipalName() + "<br><br>"
//      + " OU authClient.getAccessToken(): " + authClient.getAccessToken() + "<br><br>" +
//      " OU ((OAuth2AuthenticationToken)user): " + ((OAuth2AuthenticationToken) user) +
//      "<br><br>" + "<br><br>" + "<br><br>"); // GITHUB USER if
//      (userAttributes.containsKey("url") &&
//      userAttributes.get("url").toString().contains("github.com")) { String email =
//      gitHubUserUtil .getGitHubUserBaseAndVerifiedEmail((OAuth2AuthorizedClient) user);
//      // this.currentUsername = email; loginInfo.append("Github mail: " + email +
//      "<br><br>"); } // GOOGLE USER else { String email = (String)
//      userAttributes.get("email"); // this.currentUsername = email;
//      loginInfo.append("Google email: " + email + "<br><br>"); }
//      loginInfo.append("Access Token: " + userToken + "<br><br>"); //
//      protectedInfo.append("origin:"+ userAttributes.get() ); }else
//
//    {
//	loginInfo.append("NA");
//	throw new LoginException("LOGGIN Failure");
//    }return loginInfo.append("------ fin getOauth2LoginInfo---------");
//
//}}
