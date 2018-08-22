package com.sharan.room.exception;

import com.sharan.room.util.CodeDescription;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 85583224758803531L;

	Integer code;
	String description;
	
	public BadRequestException(){}
	
	public BadRequestException(CodeDescription codeDescription){
		this.code = codeDescription.code;
		this.description = codeDescription.description;
	}
	
	public BadRequestException(CodeDescription codeDescription,String parameter){
		this.code = codeDescription.code;
		this.description = codeDescription.description+" :'"+parameter+"'";
	}
}