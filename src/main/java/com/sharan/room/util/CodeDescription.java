package com.sharan.room.util;

public enum CodeDescription {
	
	REQUEST_EMPTY(1000,"Request empty"),
	PARAMETER_MISSING(1001,"Parameter missing"),
	
	LOGIN_SUCCESS(1050,"Login Success"),
	LOGIN_FAILED(1051,"Login Failed"),
	LOGOUT_SUCCESS(1052,"Logout Success"),
	INVALID_CREDENTIAL(1053,"Invalid email or password."),
	
	MEMBER_NOT_EXIST(1100,"Member not exist"),
	MEMBER_FETCHED(1101,"Member fetched"),
	MEMBER_SAVED(1102,"Member Saved"),
	MEMBER_UPDATED(1103,"Member Updated"),
	MEMBER_DELETED(1104,"Member Deleted"),

	TRANSACTION_NOT_EXIST(1150,"Transaction not exist"),
	TRANSACTION_FETCHED(1151,"Transaction fetched"),
	TRANSACTION_SAVED(1152,"Transaction Saved"),
	TRANSACTION_UPDATED(1153,"Transaction Updated"),
	TRANSACTION_DELETED(1154,"Transaction Deleted"),

	PASSWORD_NOT_MISMATCH(1154,"Wrong password"),
	OLD_NEW_PASSWORD_MISMATCH(1154,"Password mismatch"),
	PASSWORD_CHANGE_SUCCESS(1154,"Password changed"),
	
	;
	
	public Integer code;
	public String description;
	
	CodeDescription(int code, String description){
		this.code = code;
		this.description = description;
	}
	
	public Integer getCode(){
		return code;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setCode(Integer code){
		this.code = code;
	}
	
	public void setDescription(String description){
		this.description = description ;
	}
}