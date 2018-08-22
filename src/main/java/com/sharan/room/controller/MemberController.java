package com.sharan.room.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sharan.room.dto.MemberDto;
import com.sharan.room.dto.PasswordDto;
import com.sharan.room.enumeration.Status;
import com.sharan.room.service.MemberService;
import com.sharan.room.util.BaseDto;

@RestController
@RequestMapping("/member")
public class MemberController {

	@Autowired
	MemberService memberService;
	
	@RequestMapping(value="",method=RequestMethod.GET)
	public BaseDto getAll(){
		return memberService.getAll();
	}
	
	@RequestMapping(value="/names",method=RequestMethod.GET)
	public BaseDto getAllNames(){
		return memberService.getAllNames();
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public BaseDto getById(@PathVariable Long id){
		return memberService.getById(id);
	}
	
	@RequestMapping(value="/status/{status}",method=RequestMethod.GET)
	public BaseDto getByStatus(@PathVariable Status status){
		return memberService.getByStatus(status);
	}
	
	@RequestMapping(value="",method=RequestMethod.POST)
	public BaseDto add(@RequestBody MemberDto dto){
		return memberService.add(dto);
	}
	
	@RequestMapping(value="",method=RequestMethod.PUT)
	public BaseDto update(@RequestBody MemberDto dto){
		return memberService.update(dto);
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public BaseDto delete(@PathVariable Long id){
		return memberService.delete(id);
	}
	
	@RequestMapping(value="/changePassword",method=RequestMethod.POST)
	public BaseDto changePassword(@RequestBody PasswordDto dto){
		return memberService.changePassword(dto);
	}
}