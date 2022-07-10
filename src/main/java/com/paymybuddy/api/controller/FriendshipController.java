//
//package com.paymybuddy.api.controller;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.paymybuddy.api.dto.UserDto;
//import com.paymybuddy.api.service.UserService;
//
//
////@RestController
//public class FriendshipController {
//
//    @Autowired
//    UserService userService;
//
//    private static Logger logger = LoggerFactory.getLogger(Controller.class);
//
//    @GetMapping("/user/buddies")
//    public ResponseEntity<Object> getMyBuddies(@RequestParam String email) {
//	logger.info("GET getMyBuddies request param email: " + email);
//	if (email.length() < 4 || email == null) {
//	    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//		    .body("Entered value for email is incorrect.");
//	}
//	UserDto result = userService.readUserProfil(email);
//	return ResponseEntity.status(HttpStatus.OK).body(result);
//    }
//
//    @PostMapping(path = "/user/friend")
//    public ResponseEntity<Object> addNewBuddy(@RequestParam String email) {
//	logger.info("POST /user/friend request add buddy with email: " + email);
//	if (email == null || email.length() < 4) {
//	    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//		    .body("Entered value for email is incorrect.");
//	} else {
//	    UserDto result = userService.createUser(email);
//	    return new ResponseEntity<>(result, HttpStatus.CREATED);
//	}
//    }
//
//}
