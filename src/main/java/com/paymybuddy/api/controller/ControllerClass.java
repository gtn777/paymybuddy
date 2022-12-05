package com.paymybuddy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.paymybuddy.api.util.GitHubUserUtil;

public class ControllerClass {

	@Autowired
	private GitHubUserUtil gitHubUserUtil;

	private final OAuth2AuthorizedClientService authorizedClientService;

	public ControllerClass(OAuth2AuthorizedClientService authorizedClientService) {
		this.authorizedClientService = authorizedClientService;
	}

	protected String getAuthenticatedUsername(Authentication auth) {
		if (auth instanceof OAuth2AuthenticationToken) {
			OAuth2User authUser = ((OAuth2AuthenticationToken) auth).getPrincipal();
			OAuth2AuthorizedClient authClient = this.authorizedClientService.loadAuthorizedClient(
					((OAuth2AuthenticationToken) auth).getAuthorizedClientRegistrationId(), authUser.getName());
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
