
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

import com.paymybuddy.api.dto.UserDto;
import com.paymybuddy.api.service.BankAccountService;
import com.paymybuddy.api.service.UserService;

@Controller
public class BankAccountController extends ControllerClass {

	public BankAccountController(OAuth2AuthorizedClientService authorizedClientService) {
		super(authorizedClientService);
	}

	@Autowired
	BankAccountService bankAccountService;

	@Autowired
	UserService userService;

	@GetMapping(value = "/profile/addBankAccount")
	public Object getAddBankAccountPage(Authentication auth) throws Throwable {
		ModelAndView mav = new ModelAndView("addBankAccount");
		UserDto dto = userService.getUserDtoByUsername(getAuthenticatedUsername(auth));
		mav.addObject("dto", dto);
		mav.setStatus(HttpStatus.OK);
		return mav;
	}

	@PostMapping(value = "/profile/addBankAccount")
	public Object addBankAccount(Authentication auth, @ModelAttribute(value = "bankName") String bankName,
			@ModelAttribute(value = "accountNumber") Long accountNumber) throws Throwable {
		bankAccountService.addBankAccount(getAuthenticatedUsername(auth), bankName, accountNumber);
		return "redirect:/profile";
	}

	@PostMapping(value = "/profile/deleteBankAccount")
	public Object deleteBankAccount(Authentication auth) throws Throwable {
		bankAccountService.deleteBankAccount(getAuthenticatedUsername(auth));
		return "redirect:/profile";
	}

	@PostMapping(value = "/profile/fromBank")
	public Object receiveMoneyFromBank(Authentication auth, @ModelAttribute(value = "amount") int amount)
			throws Throwable {
		bankAccountService.receiveMoneyFromBank(getAuthenticatedUsername(auth), amount);
		return "redirect:/profile";
	}

	@PostMapping(value = "/profile/toBank")
	public Object sendMoneyToBank(Authentication auth, @ModelAttribute(value = "amount") int amount) throws Throwable {
		bankAccountService.sendMoneyToBank(getAuthenticatedUsername(auth), amount);
		return "redirect:/profile";
	}

}