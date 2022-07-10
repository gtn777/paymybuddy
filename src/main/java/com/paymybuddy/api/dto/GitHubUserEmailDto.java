
package com.paymybuddy.api.dto;

import java.io.Serializable;

import lombok.Data;


@Data
public class GitHubUserEmailDto implements Serializable {

    private static final long serialVersionUID = -2992624353426754535L;

    private String email;

    private boolean primary;

    private boolean verified;

    private String visibility;

}
