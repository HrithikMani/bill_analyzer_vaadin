package com.acs560.bills_analyzer.models;

import com.acs560.bills_analyzer.entities.CompanyEntity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Company implements Comparable<Company> {

	@NotNull
	private int id;
	
	@NotEmpty
	@NotNull
	private String name;
	
	/**
     * Constructs a Company object from a CompanyEntity.
     * 
     * @param ce the CompanyEntity used to initialize the Company object
     */
	public Company(CompanyEntity ce) {
		this(ce.getId(), ce.getName());
	}

	/**
     * Compares this company with another company based on the name.
     * 
     * @param o the Company to be compared
     * @return a negative integer, zero, or a positive integer as this company
     *         is less than, equal to, or greater than the specified company
     */
	@Override
	public int compareTo(Company o) {
		return name.compareTo(o.name);
	}
}
