package com.acs560.bills_analyzer.services.impl;

import java.util.HashSet;
import java.util.Set;

public class MonthRangeUtil {

	public static Set<Integer> getRange(int month, int range) {
		
		if (month < 1 || month > 12) {
			throw new IllegalArgumentException("Month must be between 1 and 12");
		} else if (range < 0 || range > 5) {
			throw new IllegalArgumentException("Range must be between 0 and 5");
		}
		
		int lowerRange = month - range;
		int upperRange = month + range;
		
		Set<Integer> months = new HashSet<>();
		
		for (int i = lowerRange; i <= upperRange; i++) {
			int monthToAdd;
			
			if (i <= 0) {
				monthToAdd = i + 12;
			} else if (i > 12){
				monthToAdd = i - 12;
			} else {
				monthToAdd = i;
			}
			
			months.add(monthToAdd);
		}
		
		return months;
	}
}
