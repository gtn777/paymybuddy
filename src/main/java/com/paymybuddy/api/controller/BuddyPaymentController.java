
package com.paymybuddy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.paymybuddy.api.dto.BuddyPaymentDto;
import com.paymybuddy.api.dto.UserDto;
import com.paymybuddy.api.service.BuddyPaymentService;
import com.paymybuddy.api.service.UserService;

@Controller
public class BuddyPaymentController extends ControllerClass {

	public BuddyPaymentController(OAuth2AuthorizedClientService authorizedClientService) {
		super(authorizedClientService);
	}

	@Autowired
	private UserService userService;

	@Autowired
	private BuddyPaymentService buddyPaymentService;

	@GetMapping(value = "/transfer")
	public Object getTransfertPage(Authentication auth) throws Throwable {
		ModelAndView mav = new ModelAndView("transfer");
		UserDto userDto = userService.getUserDtoByUsername(getAuthenticatedUsername(auth));
		mav.addObject("userDto", userDto);
		mav.setStatus(HttpStatus.OK);
		return mav;
	}

	@PostMapping(value = "/transfer/payment")
	public Object postNewBuddy(Authentication auth, @ModelAttribute(value = "buddy") String buddy,
			@ModelAttribute(value = "amount") float amount,
			@ModelAttribute(value = "description") String description) throws Throwable {
		BuddyPaymentDto buddyPaymentDto = new BuddyPaymentDto(this.getAuthenticatedUsername(auth), buddy,
				amount, description);
		buddyPaymentService.createABuddyPayment(buddyPaymentDto);
		return "redirect:/transfer";
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
	public Object addConnection(Authentication auth, @ModelAttribute(value = "newBuddy") String newBuddy)
			throws Throwable {
		userService.addNewBuddy(getAuthenticatedUsername(auth), newBuddy);
		return "redirect:/addConnection";
	}
}
