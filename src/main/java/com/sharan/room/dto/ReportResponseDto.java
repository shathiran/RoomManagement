package com.sharan.room.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ReportResponseDto {

	List<Map<String, Object>> transactions = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> deposits = new ArrayList<Map<String, Object>>();
	List<SummaryDto> summaryList = new ArrayList<SummaryDto>();
	
}