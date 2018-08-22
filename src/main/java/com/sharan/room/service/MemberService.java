package com.sharan.room.service;

import java.util.Date;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sharan.room.dto.MemberDto;
import com.sharan.room.dto.PasswordDto;
import com.sharan.room.enumeration.Status;
import com.sharan.room.enumeration.UserType;
import com.sharan.room.exception.Validate;
import com.sharan.room.model.Member;
import com.sharan.room.repository.MemberRepository;
import com.sharan.room.util.BaseDto;
import com.sharan.room.util.CodeDescription;

@Service
@Log4j
public class MemberService {

	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	LoginService loginService;
	
	@Value("${md5Salt}")
	String md5Salt;
	
	public BaseDto getAll() {
		log.info("MemberService getAll");
		return new BaseDto(true,CodeDescription.MEMBER_FETCHED,MemberDto.toDtos(memberRepository.findAll()));
	}

	public BaseDto getAllNames() {
		log.info("MemberService getAllNames");
		return new BaseDto(true,CodeDescription.MEMBER_FETCHED,memberRepository.findAllNameAsc());
	}
	
	public BaseDto getById(Long id) {
		log.info("MemberService getById : "+id);
		
		MemberDto dto = null;
		
		Member member = memberRepository.findById(id);
		if(member != null)
			dto = new MemberDto(member);
		
		return new BaseDto(true,CodeDescription.MEMBER_FETCHED,dto);
	}
	
	public BaseDto getByStatus(Status status) {
		log.info("MemberService getByStatus : "+status);
		return new BaseDto(true,CodeDescription.MEMBER_FETCHED,MemberDto.toDtos(memberRepository.findByStatus(status)));
	}

	public BaseDto add(MemberDto dto) {
		log.info("MemberService add : "+dto);
		Validate.notNull(dto.getName(), CodeDescription.PARAMETER_MISSING,"name");
		Validate.notNull(dto.getMobileNumber(), CodeDescription.PARAMETER_MISSING,"mobile number");
		Validate.notNull(dto.getEmail(), CodeDescription.PARAMETER_MISSING,"email");
		
		dto.setName(dto.getName().trim().replaceAll(" ", "_"));
		
		Member member = new Member(dto);
		member.setUserType(UserType.MEMBER);
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		member.setPassword(encoder.encodePassword(member.getMobileNumber(), md5Salt));
		member.setCreatedDate(new Date());
		member.setCreatedBy(new Member(loginService.getCurrentUser().getId()));
		memberRepository.save(member);
		return new BaseDto(true,CodeDescription.MEMBER_SAVED,"");
	}

	public BaseDto update(MemberDto dto) {
		log.info("MemberService update : "+dto);
		Validate.notNull(dto.getName(), CodeDescription.PARAMETER_MISSING,"name");
		Validate.notNull(dto.getMobileNumber(), CodeDescription.PARAMETER_MISSING,"mobile number");
		Validate.notNull(dto.getEmail(), CodeDescription.PARAMETER_MISSING,"email");
		
		Member tempMember = memberRepository.findById(dto.getId());
		Validate.notNull(tempMember, CodeDescription.MEMBER_NOT_EXIST);
		
		dto.setName(dto.getName().trim().replaceAll(" ", "_"));
		
		Member member = new Member(dto);
		member.setUserType(UserType.MEMBER);
		member.setPassword(tempMember.getPassword());
		member.setCreatedBy(tempMember.getCreatedBy());
		member.setCreatedDate(tempMember.getCreatedDate());
		member.setModifiedDate(new Date());
		member.setModifiedBy(new Member(loginService.getCurrentUser().getId()));
		memberRepository.save(member);
		return new BaseDto(true,CodeDescription.MEMBER_UPDATED,"");
	}

	public BaseDto delete(Long id) {
		log.info("MemberService delete : "+id);
		Validate.notNull(id, CodeDescription.PARAMETER_MISSING,"id");
		
		Member user = memberRepository.findById(id);
		Validate.notNull(id, CodeDescription.MEMBER_NOT_EXIST);
		
		memberRepository.delete(user);
		return new BaseDto(true,CodeDescription.MEMBER_DELETED,"");
	}
	
	public BaseDto changePassword(PasswordDto dto) {
		Validate.notNull(dto.getMemberId(), CodeDescription.PARAMETER_MISSING,"memberId");
		Validate.notNull(dto.getOldPassword(), CodeDescription.PARAMETER_MISSING,"oldPassword");
		Validate.notNull(dto.getNewPassword(), CodeDescription.PARAMETER_MISSING,"newPassword");
		
		Member member = memberRepository.findById(dto.getMemberId());
		Validate.notNull(member, CodeDescription.MEMBER_NOT_EXIST);
		
		Md5PasswordEncoder encode = new Md5PasswordEncoder();
		boolean valid = encode.isPasswordValid(member.getPassword(), dto.getOldPassword(), md5Salt);
		
		if(!valid)
			return new BaseDto(false,CodeDescription.PASSWORD_NOT_MISMATCH,"");
		else
			member.setPassword(encode.encodePassword(dto.getNewPassword(), md5Salt));

		memberRepository.save(member);
		return new BaseDto(true,CodeDescription.PASSWORD_CHANGE_SUCCESS,"");
	}
}


//select t.id,t.date,t.amount,t.amount_given_by_id given,tm.participated_members_id as participant from transaction t, transaction_participated_members tm where t.id = tm.transaction_id;