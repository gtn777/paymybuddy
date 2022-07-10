
package com.paymybuddy.api.dto;

import java.io.Serializable;

import lombok.Data;


@Data
public class LoginIdDto implements Serializable {

    private static final long serialVersionUID = -3420817727541693869L;

    public LoginIdDto() { super(); }

    public LoginIdDto(String u, String p) {
	super();
	this.username = u;
	this.password = p;
    }

    private String username;

    private String password;

}
