package com.acs560.bills_analyzer.services.impl;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MonthRangeUtilTest {
	
	@Test
	public void testGetRangeAugustZeroMonth_shouldReturnCorrectValues() {
		Set<Integer> expected = Set.of(8);
		var actual = MonthRangeUtil.getRange(8, 0);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	public void testGetRangeAugustOneMonth_shouldReturnCorrectValues() {
		Set<Integer> expected = Set.of(7, 8, 9);
		var actual = MonthRangeUtil.getRange(8, 1);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	public void testGetRangeAugustTwoMonths_shouldReturnCorrectValues() {
		Set<Integer> expected = Set.of(6, 7, 8, 9, 10);
		var actual = MonthRangeUtil.getRange(8, 2);
		
		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void testGetRangeJanuaryOneMonth_shouldReturnCorrectValues() {
		Set<Integer> expected = Set.of(12, 1, 2);
		var actual = MonthRangeUtil.getRange(1, 1);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	public void testGetRangeJanuaryTwoMonths_shouldReturnCorrectValues() {
		Set<Integer> expected = Set.of(11, 12, 1, 2, 3);
		var actual = MonthRangeUtil.getRange(1, 2);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	public void testGetRangeJanuaryZeroMonth_shouldReturnCorrectValues() {
		Set<Integer> expected = Set.of(1);
		var actual = MonthRangeUtil.getRange(1, 0);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	public void testGetRangeDecemberOneMonth_shouldReturnCorrectValues() {
		Set<Integer> expected = Set.of(11, 12, 1);
		var actual = MonthRangeUtil.getRange(12, 1);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	public void testGetRangeDecemberTwoMonths_shouldReturnCorrectValues() {
		Set<Integer> expected = Set.of(10, 11, 12, 1, 2);
		var actual = MonthRangeUtil.getRange(12, 2);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	public void testGetRangeDecemberZeroMonth_shouldReturnCorrectValues() {
		Set<Integer> expected = Set.of(12);
		var actual = MonthRangeUtil.getRange(12, 0);
		
		Assertions.assertEquals(expected, actual);
	}
}
