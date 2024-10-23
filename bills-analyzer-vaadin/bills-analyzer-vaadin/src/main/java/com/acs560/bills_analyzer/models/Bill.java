package com.acs560.bills_analyzer.models;

import java.util.Comparator;
import java.util.Objects;

import com.acs560.bills_analyzer.entities.BillEntity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data encapsulation class of the bills data.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Bill implements Comparable<Bill> {

	@Setter
	@NotNull(message="Month is required")
	@Min(value=1, message="Month must be between 1 and 12")
	@Max(value=12, message="Month must be between 1 and 12")
	private Integer month;
	
	@Setter
	@NotNull(message="Year is required")
	@Min(value=1000, message="Year must be 4 digits")
	@Max(value=9999, message="Year must be 4 digits")
	private Integer year;
	
	@Setter
	@NotNull(message="Company is required")
	private Company company;
	
	@Setter
	@NotNull(message="Amount is required")
	private Double amount;
	
	@Override
	public int hashCode() {
		return Objects.hash(month, year, company);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bill other = (Bill) obj;
		return Objects.equals(month,  other.month) && Objects.equals(year, other.year) &&
				Objects.equals(company, other.company);
	}
	
	public Bill(BillEntity be) {
		this(be.getId().getBillingMonth(),
				be.getId().getBillingYear(),
				new Company(be.getCompany()),
				be.getAmount());
	}

	@Override
	public int compareTo(Bill o) {
		return Comparator.comparing(Bill::getYear)
		.thenComparing(Bill::getMonth)
		.thenComparing(b-> b.getCompany().compareTo(o.getCompany()))
		.compare(this, o);
	}
}
