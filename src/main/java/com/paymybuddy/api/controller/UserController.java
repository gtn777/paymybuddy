
package com.paymybuddy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
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

@Controller
public class UserController extends ControllerClass {

	public UserController(OAuth2AuthorizedClientService authorizedClientService) {
		super(authorizedClientService);
	}

	@Autowired
	private UserService userService;

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
	public Object createUserPasswordAccount(@ModelAttribute(value = "logindto") LoginDto dto) throws Throwable {
		userService.createPasswordAccount(dto);
		return "redirect:/newUser";
	}

	@GetMapping(value = "/newUser")
	public Object GetWelcomeNewUser() {
		ModelAndView mav = new ModelAndView("newUser");
		mav.setStatus(HttpStatus.CREATED);
		return mav;
	}

	@GetMapping(value = "/profile")
	public Object getProfilePage(Authentication auth) throws Throwable {
		ModelAndView mav = new ModelAndView("profile");
		UserDto dto = userService.getUserDtoByUsername(getAuthenticatedUsername(auth));
		mav.addObject("dto", dto);
		mav.setStatus(HttpStatus.OK);
		return mav;
	}

}
