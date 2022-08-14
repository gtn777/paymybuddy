
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

import com.paymybuddy.api.dto.UserDto;
import com.paymybuddy.api.service.BankAccountService;
import com.paymybuddy.api.service.UserService;
import com.paymybuddy.api.util.GitHubUserUtil;


@Controller
public class BankAccountController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public BankAccountController(OAuth2AuthorizedClientService authorizedClientService) {
	this.authorizedClientService = authorizedClientService;
    }

    @Autowired
    BankAccountService bankAccountService;

    @Autowired
    UserService userService;

    @Autowired
    private GitHubUserUtil gitHubUserUtil;

    @GetMapping(value = "/profile/addBankAccount")
    public Object getAddBankAccountPage(Authentication auth) throws Throwable {
	ModelAndView mav = new ModelAndView("addBankAccount");
	UserDto dto = userService.getUserDtoByUsername(getAuthenticatedUsername(auth));
	mav.addObject("dto", dto);
	mav.setStatus(HttpStatus.OK);
	return mav;
    }

    @PostMapping(value = "/profile/addBankAccount")
    public Object addBankAccount(Authentication auth,
	@ModelAttribute(value = "bankName") String bankName,
	@ModelAttribute(value = "accountNumber") Long accountNumber)
    throws Throwable {
	bankAccountService.addBankAccount(getAuthenticatedUsername(auth), bankName, accountNumber);
	return "redirect:/profile";
    }

    @PostMapping(value = "/profile/deleteBankAccount")
    public Object deleteBankAccount(Authentication auth) throws Throwable {
	bankAccountService.deleteBankAccount(getAuthenticatedUsername(auth));
	return "redirect:/profile";
    }

    @PostMapping(value = "/profile/fromBank")
    public Object receiveMoneyFromBank(Authentication auth,
	@ModelAttribute(value = "amount") int amount)
    throws Throwable {
	bankAccountService.receiveMoneyFromBank(getAuthenticatedUsername(auth), amount);
	return "redirect:/profile";
    }

    @PostMapping(value = "/profile/toBank")
    public Object sendMoneyToBank(Authentication auth, @ModelAttribute(value = "amount") int amount)
    throws Throwable {
	bankAccountService.sendMoneyToBank(getAuthenticatedUsername(auth), amount);
	return "redirect:/profile";
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
