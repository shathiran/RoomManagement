package com.sharan.room.dto;

import lombok.Data;

@Data
public class PasswordDto {
	
	Long memberId;
	String oldPassword;
	String newPassword;

}