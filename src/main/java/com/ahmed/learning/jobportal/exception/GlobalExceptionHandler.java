package com.ahmed.learning.jobportal.exception;

import com.ahmed.learning.jobportal.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	ResponseEntity<ErrorResponseDto> handleException(Exception exception,
	                                                 WebRequest webRequest) {
		ErrorResponseDto errorResponseDto = new ErrorResponseDto(
						webRequest.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR
						, exception.getMessage(), LocalDateTime.now()
		);

		return new ResponseEntity<>(errorResponseDto,
						HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(NullPointerException.class)
	ResponseEntity<ErrorResponseDto> handleNullException(Exception exception,
	                                                     WebRequest webRequest) {
		ErrorResponseDto errorResponseDto = new ErrorResponseDto(
						webRequest.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR
						,
						"A NullPointerException occurred due to: " + exception.getMessage(),
						LocalDateTime.now()
		);

		return new ResponseEntity<>(errorResponseDto,
						HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
