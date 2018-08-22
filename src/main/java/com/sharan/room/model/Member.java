package com.sharan.room.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

import com.sharan.room.dto.MemberDto;
import com.sharan.room.enumeration.Days;
import com.sharan.room.enumeration.Status;
import com.sharan.room.enumeration.UserType;

@Entity
@Data
public class Member {
	
	@Id
	@GeneratedValue
	Long id;
	
	@Column(nullable=false,unique=true)
	String name;
	
	String nickName;
	
	@Column(nullable=false,unique=true)
	String mobileNumber;
	
	@Column(nullable=false,unique=true)
	String email;
	
	String password;
	
	@Temporal(TemporalType.DATE)
	Date dateOfBirth;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false,columnDefinition="enum ('ACTIVE','INACTIVE')")	
	Status status;
	
	@Temporal(TemporalType.DATE)
	Date joiningDate;
	
	@Column(columnDefinition="enum ('SUNDAY','MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY')")
	@Enumerated(EnumType.STRING)
	Days cookingDay;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false,columnDefinition="enum ('ROOM','MEMBER')")
	UserType userType;
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="FK_MEBMER_CREATED_BY"))
	Member createdBy;
	Date createdDate;
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="FK_MEBMER_MODIFIED_BY"))
	Member modifiedBy;
	Date modifiedDate;
	
	public Member() {}
	
	public Member(Long id) {
		this.id = id;
	}
	
	public Member(MemberDto memberDto) {
		this.id = memberDto.getId();
		this.name = memberDto.getName();
		this.nickName = memberDto.getNickName();
		this.mobileNumber = memberDto.getMobileNumber();
		this.email = memberDto.getEmail();
		this.dateOfBirth = memberDto.getDateOfBirth();
		this.status = memberDto.getStatus();
		this.joiningDate = memberDto.getJoiningDate();
		this.cookingDay = memberDto.getCookingDay();
	}
}