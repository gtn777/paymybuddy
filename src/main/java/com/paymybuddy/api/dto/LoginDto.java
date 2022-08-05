
package com.paymybuddy.api.dto;

import java.io.Serializable;

import lombok.Data;


@Data
public class LoginDto implements Serializable {

    private static final long serialVersionUID = -3420817727541693869L;

    public LoginDto() { super(); }

    public LoginDto(String u, String p) {
	super();
	this.username = u;
	this.password = p;
    }

    private String username;

    private String password;

}
