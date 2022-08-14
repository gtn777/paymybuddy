
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
import org.springframework.web.servlet.ModelAndView;

import com.paymybuddy.api.dto.BuddyPaymentDto;
import com.paymybuddy.api.dto.UserDto;
import com.paymybuddy.api.service.BuddyPaymentService;
import com.paymybuddy.api.service.UserService;
import com.paymybuddy.api.util.GitHubUserUtil;


@Controller
public class BuddyPaymentController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public BuddyPaymentController(OAuth2AuthorizedClientService authorizedClientService) {
	this.authorizedClientService = authorizedClientService;
    }

    @Autowired
    private UserService userService;

    @Autowired
    private BuddyPaymentService buddyPaymentService;

    @Autowired
    private GitHubUserUtil gitHubUserUtil;

    @GetMapping(value = "/transfer")
    public Object getTransfertPage(Authentication auth) throws Throwable {
	ModelAndView mav = new ModelAndView("transfer");
	UserDto userDto = userService.getUserDtoByUsername(getAuthenticatedUsername(auth));
	mav.addObject("userDto", userDto);
	mav.setStatus(HttpStatus.OK);
	return mav;
    }

    @GetMapping(value = "/addConnection")
    public Object getAddConnectionPage(Authentication auth) {
	ModelAndView mav = new ModelAndView("addConnection");
	UserDto userDto = userService.getUserDtoByUsername(getAuthenticatedUsername(auth));
	mav.addObject("userDto", userDto);
	mav.setStatus(HttpStatus.OK);
	return mav;
    }

    @PostMapping(value = "/addConnection")
    public Object addConnection(Authentication auth,
	@ModelAttribute(value = "newBuddy") String newBuddy)
    throws Throwable {
	userService.addNewBuddy(getAuthenticatedUsername(auth), newBuddy);
	return "redirect:/addConnection";
    }

    @PostMapping(value = "/transfer/payment")
    public Object postNewBuddy(Authentication auth, @ModelAttribute(value = "buddy") String buddy,
	@ModelAttribute(value = "amount") float amount,@ModelAttribute(value="description") String description )
    throws Throwable {
	BuddyPaymentDto buddyPaymentDto = new BuddyPaymentDto(this.getAuthenticatedUsername(auth),
	buddy, amount, description);
	buddyPaymentService.createABuddyPayment(buddyPaymentDto);
	return "redirect:/transfer";
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
