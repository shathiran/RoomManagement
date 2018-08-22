package com.sharan.room.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sharan.room.dto.LoginRequestDto;
import com.sharan.room.service.LoginService;

@Log4j
@RestController
@RequestMapping(value="/auth")
public class LoginController {

	@Autowired
	LoginService loginService;
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public Object login(@RequestBody LoginRequestDto dto,HttpServletRequest request,HttpServletResponse response){
		log.info("LoginController login");
		return loginService.login(dto,request,response);
	}
	
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public Object logout(HttpServletRequest request,HttpServletResponse response){
		log.info("LoginController logout");
		return loginService.logout(request,response);
	}
}
