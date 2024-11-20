package com.acs560.bills_analyzer.exception;

import java.util.NoSuchElementException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.validation.ConstraintViolationException;

/**
 * The bills analyzer common exception handler
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BillsAnalyzerExceptionHandler {

	/**
	 * Handler for NoSuchElementException
	 * @return Response entity
	 */
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<String> handle(NoSuchElementException ex){
		return ResponseEntity.notFound().build();
	}
	
	/**
	 * Handler for IllegalArgumentException
	 * 
	 * @return Response entity 
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handle(IllegalArgumentException ex){
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
	/**
	 * Custom Handler for BillsRepositoryManagementException
	 * 
	 * @return Response entity 
	 */
	@ExceptionHandler(BillsRepositoryManagementException.class)
	public ResponseEntity<String> handle(BillsRepositoryManagementException ex){
		return ResponseEntity.internalServerError().body(ex.getMessage());
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex){
		System.out.println("ConstraintViolationException");
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Handler for NoResourceFoundException
	 * 
	 * @return Response entity 
	 */
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<String> handle(NoResourceFoundException ex){
		ex.printStackTrace();
		return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("Endpoint does not exist");
	}
	
	/**
	 * Handler for all other exceptions
	 * 
	 * @return Response entity 
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handle(Exception ex){
		ex.printStackTrace();
		return ResponseEntity.internalServerError().body("We're sorry...but we will fix it:(");
	}
	
	/**
	 * Handler for Method Argument Not Valid Exception
	 * 
	 * @return Response entity 
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		var errors = ex.getBindingResult().getAllErrors();
		String response = errors.size() > 0 ? errors.get(0).getDefaultMessage() : "Invalid value";
		System.out.println("MethodArgumentNotValidException");
		return ResponseEntity.badRequest().body(response);
	}
	
	/**
	 * Handler for Handler Method Validation Exception
	 * 
	 * @return Response entity 
	 */
	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<String> handleHandlerMethodValidationException(HandlerMethodValidationException ex){
		var errors = ex.getAllErrors();
		String response = errors.size() > 0 ? errors.get(0).getDefaultMessage() : "Invalid value";
		System.out.println("HandlerMethodValidationException");
		return ResponseEntity.badRequest().body(response);
	}
	
}
