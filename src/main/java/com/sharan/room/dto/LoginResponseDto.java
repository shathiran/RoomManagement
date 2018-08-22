package com.sharan.room.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoginResponseDto implements Serializable {
	
	private static final long serialVersionUID = 405456012643472727L;
	
	MemberDto member;
	String sessionId;
}
