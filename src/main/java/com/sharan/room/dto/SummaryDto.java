package com.sharan.room.dto;

import lombok.Data;

@Data
public class SummaryDto {

	String member;
	double oldBalance;
	double transactionBalance;
	double amountGiven;
	double depositAmount;
	
}