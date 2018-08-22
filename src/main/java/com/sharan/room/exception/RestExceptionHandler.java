package com.sharan.room.exception;

import java.nio.file.AccessDeniedException;

import lombok.extern.log4j.Log4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sharan.room.util.BaseDto;

@ControllerAdvice
@Log4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody BaseDto handleException(final BadRequestException ex){
		BaseDto base = new BaseDto();
		base.setCode(ex.code);
		base.setDescription(ex.description);
		return base;
	}
	
	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ResponseBody BaseDto handleException(final AccessDeniedException ex){
		BaseDto base = new BaseDto();
		base.setCode(401);
		base.setDescription("Access Denied");
		return base;
	}
	
	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody BaseDto handleException(final Exception ex){
		BaseDto base = new BaseDto();
		base.setCode(500);
		base.setDescription("Internal Server Error");
		log.info("Internal Server Error \n",ex);
		return base;
	}
}