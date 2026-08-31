package com.cws.shop.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import com.cws.shop.exception.DuplicateRatingException;
import com.cws.shop.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;

import com.cws.shop.dto.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {

		ErrorResponse response = new ErrorResponse(
				false,
				ex.getMessage(),
				null,
				LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> handleUnauthorizedAction(UnauthorizedException ex) {

		ErrorResponse response = new ErrorResponse(
				false,
				ex.getMessage(),
				null,
				LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}

	@ExceptionHandler(NotificationNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotificationNotFound(
			NotificationNotFoundException ex) {

		ErrorResponse response = new ErrorResponse(
				false,
				ex.getMessage(),
				null,
				LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(response);
	}

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleProductNotFound(
			ProductNotFoundException ex) {

		ErrorResponse response = new ErrorResponse(
				false,
				ex.getMessage(),
				null,
				LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(response);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDenied(
			AccessDeniedException ex) {

		ErrorResponse response = new ErrorResponse(
				false,
				"Access Denied",
				null,
				LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(
			MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

		// If there's exactly one field error, surface that specific message as the top-level
		// error message so clients which show the main message (instead of the errors map)
		// present a clear, specific validation message to the user.
		String topLevelMessage;
		if (errors.size() == 1) {
			topLevelMessage = errors.values().iterator().next();
		} else if (errors.isEmpty()) {
			topLevelMessage = "Validation failed";
		} else {
			topLevelMessage = "Validation Failed";
		}

		ErrorResponse response = new ErrorResponse(
				false,
				topLevelMessage,
				errors,
				LocalDateTime.now());

		return ResponseEntity.badRequest().body(response);
	}

	// Ganesh Added
	@ExceptionHandler(ResetLimitExceededException.class)
	public ResponseEntity<ErrorResponse> handleResetLimitException(
			ResetLimitExceededException ex) {

		ErrorResponse response = new ErrorResponse(
				false,
				ex.getMessage(),
				null,
				LocalDateTime.now());

		return ResponseEntity
				.badRequest()
				.body(response);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
		ErrorResponse response = new ErrorResponse(
				false,
				ex.getMessage(),
				null,
				LocalDateTime.now());

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	// Added — handles wrong file type or file too large (from FileStorageService
	// validation)
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
		ErrorResponse response = new ErrorResponse(false, ex.getMessage(), null, LocalDateTime.now());
		return ResponseEntity.badRequest().body(response);
	}

	// Added — handles Spring's multipart size limit from application.properties
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorResponse> handleMaxUploadSize(MaxUploadSizeExceededException ex) {
		ErrorResponse response = new ErrorResponse(
				false,
				"File is too large. Please check the maximum allowed size for this file type.",
				null,
				LocalDateTime.now());
		return ResponseEntity.badRequest().body(response);
	}

	// Rahul's Update Code
	@ExceptionHandler(DuplicateRatingException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateRating(DuplicateRatingException ex) {

		ErrorResponse response = new ErrorResponse(
				false,
				ex.getMessage(),
				null,
				LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		// HTTP 409 Conflict — user already submitted a rating for this product
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {

		ErrorResponse response = new ErrorResponse(
				false,
				ex.getMessage(),
				null,
				LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

		ErrorResponse response = new ErrorResponse(
				false,
				ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : "Data integrity violation",
				null,
				LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

}
