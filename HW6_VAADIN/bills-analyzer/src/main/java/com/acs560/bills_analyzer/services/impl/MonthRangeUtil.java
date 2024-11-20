package com.acs560.bills_analyzer.services.impl;

import java.util.HashSet;
import java.util.Set;

public class MonthRangeUtil {
	
	/**
	 * Calculates a set of month numbers within a specified range of a given month.
	 *
	 * @param month the base month (1-12) for which to calculate the range
	 * @param range the range of months to include (0-5)
	 * @return a set of unique integers representing the month numbers within the specified range
	 */
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
	
	/**
	 * Converts a month companyId to its corresponding integer
	 *
	 * @param month - the month.
	 * @return the integer representation of the month (1-12)
	 */
	public static int getMonthNumber(String month) {
        switch (month.toLowerCase()) {
            case "january":
                return 1;
            case "february":
                return 2;
            case "march":
                return 3;
            case "april":
                return 4;
            case "may":
                return 5;
            case "june":
                return 6;
            case "july":
                return 7;
            case "august":
                return 8;
            case "september":
                return 9;
            case "october":
                return 10;
            case "november":
                return 11;
            case "december":
                return 12;
            default:

                throw new IllegalArgumentException("Invalid month: " + month);

        }
    }
}
