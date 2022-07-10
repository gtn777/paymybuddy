
package com.paymybuddy.api.exception;

public class FriendshipAlreadyKnwon extends RuntimeException {

    private static final long serialVersionUID = -119898345903734825L;

    public FriendshipAlreadyKnwon(String user, String friend) {
	super(user + " and " + friend + " are already known as friends.");
    }

}
