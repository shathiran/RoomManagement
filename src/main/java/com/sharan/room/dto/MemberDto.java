package com.sharan.room.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

import com.sharan.room.enumeration.Days;
import com.sharan.room.enumeration.Status;
import com.sharan.room.model.Member;

@Data
public class MemberDto {
	
	Long id;
	
	String name;
	
	String nickName;
	
	String mobileNumber;
	
	String email;
	
	Date dateOfBirth;

	Status status;
	
	Date joiningDate;
	
	Days cookingDay;
	
	GenericDto createdBy;
	
	Date createdDate;
	
	GenericDto modifiedBy;
	
	Date modifiedDate;
	
	public MemberDto(){}
			
	public MemberDto(Member member) {
		this.id = member.getId();
		this.name = member.getName();
		this.nickName = member.getNickName();
		this.mobileNumber = member.getMobileNumber();
		this.email = member.getEmail();
		this.dateOfBirth = member.getDateOfBirth();
		this.status = member.getStatus();
		this.joiningDate = member.getJoiningDate();
		this.cookingDay = member.getCookingDay();
		if(member.getCreatedBy() != null)
			this.createdBy = new GenericDto(member.getCreatedBy().getId(),member.getCreatedBy().getName());
		this.createdDate = member.getCreatedDate();
		if(member.getModifiedBy() != null)
			this.modifiedBy = new GenericDto(member.getModifiedBy().getId(),member.getModifiedBy().getName());
		this.modifiedDate = member.getModifiedDate();
	}

	public static List<MemberDto> toDtos(List<Member> members) {
		List<MemberDto> dtos = null;
		
		if(members != null && members.size()>0){
			dtos = new ArrayList<MemberDto>();
			for(Member it : members){
				dtos.add(new MemberDto(it));
			}
		}
		return dtos;
	}
}