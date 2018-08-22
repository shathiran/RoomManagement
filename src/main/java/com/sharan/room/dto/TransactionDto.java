package com.sharan.room.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

import com.sharan.room.enumeration.TransactionType;
import com.sharan.room.model.Member;
import com.sharan.room.model.Transaction;

@Data
public class TransactionDto {

	Long id;
	Date date;
	TransactionType transactionType;
	Double amount;
	GenericDto amountGivenBy;
	GenericDto cookedBy;
	String recipe;
	String note;
	GenericDto createdBy;
	Date createdDate;
	GenericDto modifiedBy;
	Date modifiedDate;
	List<GenericDto> participatedMembers;
	
	public TransactionDto(){}
	
	public TransactionDto(Transaction transaction){
		this.id = transaction.getId();
		this.date = transaction.getDate();
		this.transactionType = transaction.getTransactionType();
		this.amount = transaction.getAmount();
		this.recipe = transaction.getRecipe();
		this.note = transaction.getNote();
		this.createdDate = transaction.getCreatedDate();
		this.modifiedDate = transaction.getModifiedDate();
		
		if(transaction.getAmountGivenBy() != null)
			this.amountGivenBy = new GenericDto(transaction.getAmountGivenBy().getId(),transaction.getAmountGivenBy().getName());
		if(transaction.getCookedBy() != null)
			this.cookedBy = new GenericDto(transaction.getCookedBy().getId(),transaction.getCookedBy().getName());
		if(transaction.getCreatedBy() != null)
			this.createdBy = new GenericDto(transaction.getCreatedBy().getId(),transaction.getCreatedBy().getName());
		if(transaction.getModifiedBy() != null)
			this.modifiedBy = new GenericDto(transaction.getModifiedBy().getId(),transaction.getModifiedBy().getName());
		if(transaction.getParticipatedMembers() != null){
			participatedMembers = new ArrayList<GenericDto>();
			for(Member it : transaction.getParticipatedMembers()){
				participatedMembers.add(new GenericDto(it.getId(),it.getName()));	
			}
		}
	}
	
	public static List<TransactionDto> toDtos(List<Transaction> transactions) {
		List<TransactionDto> dtos = null;
		
		if(transactions != null && transactions.size()>0){
			dtos = new ArrayList<TransactionDto>();
			for(Transaction it : transactions){
				dtos.add(new TransactionDto(it));
			}
		}
		return dtos;
	}
}