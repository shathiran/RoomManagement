package com.sharan.room.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sharan.room.dto.LoginRequestDto;
import com.sharan.room.dto.LoginResponseDto;
import com.sharan.room.dto.MemberDto;
import com.sharan.room.exception.Validate;
import com.sharan.room.model.Member;
import com.sharan.room.repository.MemberRepository;
import com.sharan.room.util.BaseDto;
import com.sharan.room.util.CodeDescription;

@Service
@Log4j
public class LoginService {

	@Value("${md5Salt}")
	String md5Salt;
	
	@Autowired
	MemberRepository memberRepository;
	
	public Object login(LoginRequestDto dto,HttpServletRequest request,HttpServletResponse response) {
		log.info("LoginServicve login");
		Validate.notNull(dto, CodeDescription.REQUEST_EMPTY);
		Validate.notNull(dto.getEmail(), CodeDescription.PARAMETER_MISSING,"email");
		Validate.notNull(dto.getPassword(), CodeDescription.PARAMETER_MISSING,"password");
		
		Md5PasswordEncoder encode = new Md5PasswordEncoder();
		dto.setPassword(encode.encodePassword(dto.getPassword(), md5Salt));
		
		Member user = memberRepository.findByEmailAndPassword(dto.getEmail(), dto.getPassword());
		if(user == null)
			return new BaseDto(false, CodeDescription.INVALID_CREDENTIAL, null);
		
		MemberDto memberDto = new MemberDto(user);
		
		HttpSession session = request.getSession(true);
		
		if(session != null){
			try{
				String sessionId = session.getId();
				log.info("Session created sessionId : "+sessionId);
				List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
				authorities.add(new SimpleGrantedAuthority("ROLE_"));
				Authentication authentication = new UsernamePasswordAuthenticationToken(memberDto, sessionId,authorities);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				LoginResponseDto loginResponseDto = new LoginResponseDto();
				loginResponseDto.setSessionId(sessionId);
				loginResponseDto.setMember(memberDto);
				return new BaseDto(true, CodeDescription.LOGIN_SUCCESS, loginResponseDto);
			}catch(Exception e){
				log.info("Excetion while login : "+e);
				return new BaseDto(false, CodeDescription.LOGIN_FAILED, null);
			}
		}
		
		return new BaseDto(true, CodeDescription.LOGIN_SUCCESS, "");
	}

	public Object logout(HttpServletRequest request,HttpServletResponse response) {
		log.info("LoginServicve logout");
		try {
			request.logout();
		} catch (ServletException e) {
			e.printStackTrace();
		}
		return new BaseDto(true, CodeDescription.LOGOUT_SUCCESS, null);
	}
	
	public MemberDto getCurrentUser(){
		return (MemberDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}