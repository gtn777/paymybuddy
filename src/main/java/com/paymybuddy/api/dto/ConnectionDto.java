package com.paymybuddy.api.dto;

import lombok.Data;

@Data
public class ConnectionDto {

	public ConnectionDto() {
		super();
	}

	public ConnectionDto(String username, String buddy) {
		super();
		this.username = username;
		this.buddy = buddy;
	}

	private String username;
	private String buddy;
}
