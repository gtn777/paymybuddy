package com.paymybuddy.api.dto;

import lombok.Data;

@Data
public class FriendshipDto {
    
    
    
    public FriendshipDto() { super();}

    public FriendshipDto(String username, String newFriend) {
	super();
	this.friendship = username + " is now friends with "+newFriend;
    }

    private String friendship = "";
    
}
