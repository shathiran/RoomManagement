package com.sharan.room.util;

import lombok.Data;

@Data
public class BaseDto {
	
	Boolean status;
	Integer code;
	String description;
	Object content;
	
	public BaseDto(){}
	
	public BaseDto(Boolean status,CodeDescription codeDescription,Object content){
		this.status = status;
		this.code = codeDescription.getCode();
		this.description = codeDescription.getDescription();
		this.content = content;
	}
}
