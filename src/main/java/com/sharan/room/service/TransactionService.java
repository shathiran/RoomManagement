package com.sharan.room.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.sharan.room.dto.ReportRequestDto;
import com.sharan.room.dto.ReportResponseDto;
import com.sharan.room.dto.SummaryDto;
import com.sharan.room.dto.TransactionDto;
import com.sharan.room.enumeration.TransactionType;
import com.sharan.room.exception.Validate;
import com.sharan.room.model.Member;
import com.sharan.room.model.Transaction;
import com.sharan.room.repository.MemberRepository;
import com.sharan.room.repository.TransactionRepository;
import com.sharan.room.util.BaseDto;
import com.sharan.room.util.CodeDescription;

@Service
@Log4j
public class TransactionService {

	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public BaseDto getAll() {
		log.info("TransactionService getAll");
		return new BaseDto(true,CodeDescription.TRANSACTION_FETCHED,TransactionDto.toDtos(transactionRepository.findAll()));
	}

	public BaseDto getById(Long id) {
		log.info("MemberService getById : "+id);
		
		TransactionDto dto = null;
		
		Transaction member = transactionRepository.findById(id);
		if(member != null)
			dto = new TransactionDto(member);
		
		return new BaseDto(true,CodeDescription.TRANSACTION_FETCHED,dto);
	}

	public BaseDto add(TransactionDto dto) {
		log.info("TransactionService add : "+dto);
		Validate.notNull(dto.getDate(), CodeDescription.PARAMETER_MISSING,"date");
		Validate.notNull(dto.getAmount(), CodeDescription.PARAMETER_MISSING,"mobile amount");
		Validate.notNull(dto.getTransactionType(), CodeDescription.PARAMETER_MISSING,"TransactionType");
		if(!dto.getTransactionType().equals(TransactionType.DEPOSIT)){
			Validate.isEmpty(dto.getParticipatedMembers(), CodeDescription.PARAMETER_MISSING,"participants");
		}
		if(dto.getTransactionType().equals(TransactionType.ROOM_EXPENSE)){
			dto.setCookedBy(null);
			dto.setRecipe(null);
		}
		if(dto.getTransactionType().equals(TransactionType.DEPOSIT)){
			dto.setCookedBy(null);
			dto.setRecipe(null);
			dto.setParticipatedMembers(null);
		}
		Transaction member = new Transaction(dto);
		member.setCreatedDate(new Date());
		member.setCreatedBy(new Member(loginService.getCurrentUser().getId()));
		transactionRepository.save(member);
		return new BaseDto(true,CodeDescription.TRANSACTION_SAVED,"");
	}

	public BaseDto update(TransactionDto dto) {
		log.info("TransactionService update : "+dto);
		Validate.notNull(dto.getDate(), CodeDescription.PARAMETER_MISSING,"date");
		Validate.notNull(dto.getAmount(), CodeDescription.PARAMETER_MISSING,"mobile amount");
		Validate.notNull(dto.getTransactionType(), CodeDescription.PARAMETER_MISSING,"TransactionType");
		
		Transaction tempTransaction = transactionRepository.findById(dto.getId());
		Validate.notNull(tempTransaction, CodeDescription.TRANSACTION_NOT_EXIST);
		
		Transaction transaction = new Transaction(dto);
		transaction.setCreatedBy(tempTransaction.getCreatedBy());
		transaction.setCreatedDate(tempTransaction.getCreatedDate());
		transaction.setModifiedDate(new Date());
		transaction.setModifiedBy(new Member(loginService.getCurrentUser().getId()));
		transactionRepository.save(transaction);
		return new BaseDto(true,CodeDescription.TRANSACTION_UPDATED,"");
	}

	public BaseDto delete(Long id) {
		log.info("TransactionService delete : "+id);
		Validate.notNull(id, CodeDescription.PARAMETER_MISSING,"id");
		
		Transaction user = transactionRepository.findById(id);
		Validate.notNull(id, CodeDescription.TRANSACTION_NOT_EXIST);
		
		transactionRepository.delete(user);
		return new BaseDto(true,CodeDescription.TRANSACTION_DELETED,"");
	}
	
	public BaseDto getReport(ReportRequestDto dto) {

		Validate.notNull(dto.getFromDate(), CodeDescription.PARAMETER_MISSING,"fromDate");
		Validate.notNull(dto.getToDate(), CodeDescription.PARAMETER_MISSING,"toDate");
		
		List<String> memberNames = memberRepository.findAllNameAsc();
		Map<String, SummaryDto> summaryMap = new HashMap<String, SummaryDto>();
		
		String nameQuery = "";
		
		for(int i=0;i<memberNames.size();i++){
			String name = memberNames.get(i).trim().replace(" ", "_");
			nameQuery = nameQuery + " sum(case when participatedName='"+name+"' then perHead else 0 end) "+name;
			if(i != memberNames.size()-1){
				nameQuery = nameQuery + ",";
			}else{
				nameQuery = "," + nameQuery;
			}
			SummaryDto summaryDto = new SummaryDto();
			summaryDto.setMember(name);
			summaryMap.put(name, summaryDto);
		}
		
		log.info("nameQuery : "+nameQuery);
		
		String transactionsQuery =  " select date,transactionType,givenName,amount "+nameQuery+
									" from (select t1.id,t1.date,t1.transactionType, t1.amount,t1.given,t1.givenName, t1.participant, t1.participatedName, t3.perHead from (select  t.id,t.date,t.transaction_type as transactionType, t.amount,t.amount_given_by_id given,tm.participated_members_id as participant,m.name givenName,m1.name participatedName from transaction t, transaction_participated_members tm,member m, member m1 where t.id = tm.transaction_id and m.id = t.amount_given_by_id and m1.id = tm.participated_members_id and t.date >= ':fromDate' and t.date <= ':toDate') t1 left join (select t2.id as id, ROUND(amount/t2.members,2) as perHead from (select t.id id,t.amount amount, count(t.id) as members from transaction t, transaction_participated_members tm where t.id = tm.transaction_id  and t.date >= ':fromDate' and t.date <= ':toDate' group by t.id) t2) t3 on t1.id = t3.id) t4 group by id";
		
		transactionsQuery = transactionsQuery.replaceAll(":fromDate", sdf.format(dto.getFromDate()));
		transactionsQuery = transactionsQuery.replaceAll(":toDate", sdf.format(dto.getToDate()));
		
		log.info("transactionsQuery : "+transactionsQuery);
		
		List<Map<String, Object>> transactionWiseData = jdbcTemplate.queryForList(transactionsQuery);
		log.info("transactionWiseData : "+transactionWiseData);
		 
		for(Map<String, Object> map: transactionWiseData){
			for(String key : map.keySet()){
				if(key.equals("givenName")){
					log.info("summaryMap : "+summaryMap);
					log.info("map.get('givenName') : "+map.get("givenName"));
					if(map.get(map.get("givenName")) != null){
						SummaryDto summaryDto = summaryMap.get(map.get("givenName"));
						summaryDto.setAmountGiven(summaryDto.getAmountGiven() + (double) map.get("amount"));
						summaryMap.put((String)map.get("givenName"), summaryDto);
					}
				}else if(summaryMap.keySet().contains(key)){
					SummaryDto summaryDto = summaryMap.get(key);
					summaryDto.setTransactionBalance(summaryMap.get(key).getTransactionBalance() + (double) map.get(key));
					summaryMap.put(key, summaryDto);
				}
			}
		}
		
		String depositQuery = "select t.date as date, m.name as name,t.amount as amount from transaction t,member m where t.amount_given_by_id = m.id and transaction_type = 'DEPOSIT' and t.date >= ':fromDate' and t.date <= ':toDate'";
		
		depositQuery = depositQuery.replaceAll(":fromDate", sdf.format(dto.getFromDate()));
		depositQuery = depositQuery.replaceAll(":toDate", sdf.format(dto.getToDate()));
		log.info("depositQuery : "+depositQuery);
		
		List<Map<String, Object>> depositData = jdbcTemplate.queryForList(depositQuery);
		log.info("depositData : "+depositData);
		
		for(Map<String, Object> map: depositData){
			for(String key : map.keySet()){
				if(key.equals("name")){
					SummaryDto summaryDto = summaryMap.get(map.get(key));
					summaryDto.setDepositAmount(summaryDto.getDepositAmount() + (double) map.get("amount"));
					summaryMap.put((String)map.get(key), summaryDto);
				}
			}
		}
		
		
		ReportResponseDto reportResponseDto = new ReportResponseDto();
		reportResponseDto.setTransactions(transactionWiseData);
		reportResponseDto.setDeposits(depositData);
		
		log.info("summaryMap : "+summaryMap);
		
		for(Entry<String, SummaryDto> it : summaryMap.entrySet()){
			reportResponseDto.getSummaryList().add(it.getValue());
		}
		
		return new BaseDto(true,CodeDescription.TRANSACTION_FETCHED,reportResponseDto);
		
	}
}