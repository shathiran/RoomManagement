package com.sharan.room.exception;

import java.util.Collection;

import org.springframework.util.StringUtils;

import com.sharan.room.util.CodeDescription;

public class Validate {

	public static void notNull(Object object, CodeDescription codeDescription){
		if(object == null || StringUtils.isEmpty(object)){
			throw new BadRequestException(codeDescription);
		}
	}
	
	public static void notNull(Object object, CodeDescription codeDescription,String fieldName){
		if(object == null || StringUtils.isEmpty(object)){
			throw new BadRequestException(codeDescription,fieldName);
		}
	}
	
	public static void assertTrue(boolean flag,CodeDescription codeDescription){
		throw new BadRequestException(codeDescription);
	}
	
	public static void isEmpty(Collection<?> object, CodeDescription codeDescription,String fieldName){
		if(object == null || object.isEmpty()){
			throw new BadRequestException(codeDescription,fieldName);
		}
	}
}