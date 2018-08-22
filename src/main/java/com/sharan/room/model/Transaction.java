package com.sharan.room.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

import com.sharan.room.dto.GenericDto;
import com.sharan.room.dto.TransactionDto;
import com.sharan.room.enumeration.TransactionType;

@Entity
@Data
public class Transaction {

	@Id
	@GeneratedValue
	Long id;
	
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	Date date;

	@Column(nullable=false,columnDefinition="enum ('BREAKFAST','LUNCH','DINNER','SNACKS','ROOM_EXPENSE','DEPOSIT','CELEBRATION')")
	@Enumerated(EnumType.STRING)
	TransactionType transactionType;
	
	@Column(nullable=false)
	Double amount;
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="FK_TRANSACTION_AMOUNT_GIVEN_BY"))
	Member amountGivenBy;
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="FK_TRANSACTION_COOKED_BY"))
	Member cookedBy;
	
	String recipe;
	
	String note;
	
	@ManyToMany
	List<Member> participatedMembers;
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="FK_TRANSACTION_CREATED_BY"))
	Member createdBy;

	@Column(nullable=false)
	Date createdDate;
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="FK_TRANSACTION_MODIFIED_BY"))
	Member modifiedBy;
	Date modifiedDate;
	
	public Transaction(){}
			
	public Transaction(TransactionDto transaction){
		this.id = transaction.getId();
		this.date = transaction.getDate();
		this.transactionType = transaction.getTransactionType();
		this.amount = transaction.getAmount();
		this.recipe = transaction.getRecipe();
		this.note = transaction.getNote();
		this.createdDate = transaction.getCreatedDate();
		this.modifiedDate = transaction.getModifiedDate();
		
		if(transaction.getAmountGivenBy() != null)
			this.amountGivenBy = new Member(transaction.getAmountGivenBy().getId());
		if(transaction.getCookedBy() != null)
			this.cookedBy = new Member(transaction.getCookedBy().getId());
		if(transaction.getCreatedBy() != null)
			this.createdBy = new Member(transaction.getCreatedBy().getId());
		if(transaction.getModifiedBy() != null)
			this.modifiedBy = new Member(transaction.getModifiedBy().getId());
		
		if(transaction.getParticipatedMembers() != null){
			participatedMembers = new ArrayList<Member>();
			for(GenericDto it : transaction.getParticipatedMembers()){
				participatedMembers.add(new Member(it.getId()));	
			}
		}
	}
}