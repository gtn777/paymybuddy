
package com.paymybuddy.api.util;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.api.dto.GitHubUserEmailDto;


@Service
public class GitHubApiUtil {

    public String getGitUserEmail(OAuth2AuthorizedClient client) {
	String getEmailUrl = "https://api.github.com/user/emails";
	// send HTTP request to get user emails and get the primary email
	RestTemplate restTemplate = new RestTemplate();
	HttpHeaders headers = new HttpHeaders();
	headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());
	HttpEntity<String> entity = new HttpEntity<String>("", headers);
	ResponseEntity<String> response = restTemplate
	    .exchange(getEmailUrl, HttpMethod.GET, entity, String.class);
	String json = response.getBody();
	final ObjectMapper objectMapper = new ObjectMapper();
	try {
	    List<GitHubUserEmailDto> emailDtoList = objectMapper
		.readValue(json, new TypeReference<List<GitHubUserEmailDto>>() {
		});
	    for (GitHubUserEmailDto emailDto : emailDtoList) {
		if (emailDto.isPrimary() == true && emailDto.isVerified() == true) {
		    return emailDto.getEmail();
		}
	    }
	} catch (JsonMappingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (JsonProcessingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }

}
