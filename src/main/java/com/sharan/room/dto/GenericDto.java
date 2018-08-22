package com.sharan.room.dto;

import lombok.Data;

@Data
public class GenericDto {

	Long id;
	String name;
	
	public GenericDto(){}
	
	public GenericDto(Long id, String name) {
		this.id = id;
		this.name = name;
	}
}
