package com.sharan.room.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoginRequestDto implements Serializable {
	
	private static final long serialVersionUID = -1825301779360839858L;
	
	String email;
	String password;
}
