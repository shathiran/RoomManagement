package com.sharan.room.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sharan.room.dto.ReportRequestDto;
import com.sharan.room.dto.TransactionDto;
import com.sharan.room.service.TransactionService;
import com.sharan.room.util.BaseDto;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

	@Autowired
	TransactionService transactionService;
	
	@RequestMapping(value="",method=RequestMethod.GET)
	public BaseDto getAll(){
		return transactionService.getAll();
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public BaseDto getById(@PathVariable Long id){
		return transactionService.getById(id);
	}
	
	@RequestMapping(value="",method=RequestMethod.POST)
	public BaseDto add(@RequestBody TransactionDto dto){
		return transactionService.add(dto);
	}
	
	@RequestMapping(value="",method=RequestMethod.PUT)
	public BaseDto update(@RequestBody TransactionDto dto){
		return transactionService.update(dto);
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public BaseDto delete(@PathVariable Long id){
		return transactionService.delete(id);
	}
	
	@RequestMapping(value="/getReport",method=RequestMethod.POST)
	public BaseDto getReport(@RequestBody ReportRequestDto reportRequestDto){
		return transactionService.getReport(reportRequestDto);
	}
}
