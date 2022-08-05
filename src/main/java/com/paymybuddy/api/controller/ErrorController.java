
package com.paymybuddy.api.controller;

import java.sql.SQLDataException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.paymybuddy.api.exception.EmailAlreadyUsedException;
import com.paymybuddy.api.exception.FriendshipAlreadyKnwon;
import com.paymybuddy.api.exception.NotEnoughMoneyException;
import com.paymybuddy.api.exception.UnknownUserException;

@ControllerAdvice
public class ErrorController {

	private static Logger logger = LoggerFactory.getLogger(ErrorController.class);

	@ExceptionHandler(value = { SQLDataException.class })
	public ResponseEntity<String> handleUnknownException(Exception e) {
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(value = { NumberFormatException.class })
	public ResponseEntity<String> handleNumberFormatException(NumberFormatException e) {
		logger.error(e.getMessage());
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body("Bad Request parameter, number is expected.");
	}

	@ExceptionHandler(value = { EmailAlreadyUsedException.class })
	public ResponseEntity<String> handleEmailAlreadyUsedException(Exception e) {
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}

	@ExceptionHandler(value = { UnknownUserException.class })
	public ResponseEntity<String> handleUnknownUserException(Exception e) {
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(value = { LoginException.class })
	public ResponseEntity<String> handleLoginException(Exception e) {
		logger.error(e.getMessage());
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(e.getMessage() + "LoginException " + e.getClass());
	}

	@ExceptionHandler(value = { SQLIntegrityConstraintViolationException.class })
	public ResponseEntity<String> handleSqlIntegrityException(SQLIntegrityConstraintViolationException e) {
		logger.error(e.getMessage());
		String message = "";
		if (e.getMessage().contains("uplicate") && e.getMessage().contains("username")) {
			message = "Email entered as username is already used.";
		} else {
			message = e.getMessage();
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
	}

	@ExceptionHandler(value = { FriendshipAlreadyKnwon.class })
	public ResponseEntity<String> handleFriendshipAlreadyKnwon(FriendshipAlreadyKnwon e) {
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}

	@ExceptionHandler(value = { NotEnoughMoneyException.class })
	public ResponseEntity<String> handleNotEnoughMoneyException(NotEnoughMoneyException e) {
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String exception(final Throwable throwable, final Model model) {
		logger.error("Exception during execution of SpringSecurity application", throwable);
		String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
		model.addAttribute("errorMessage", errorMessage);
		return "error interne au serveur";
	}

}
