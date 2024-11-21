package com.acs560.bills_analyzer.models;

import com.acs560.bills_analyzer.entities.CompanyEntity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Company implements Comparable<Company> {

	private int id;
	
	@Setter
	private String name;
	
	public Company(CompanyEntity ce) {
		this(ce.getId(), ce.getName());
	}

	@Override
	public int compareTo(Company o) {
		return name.compareTo(o.name);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
