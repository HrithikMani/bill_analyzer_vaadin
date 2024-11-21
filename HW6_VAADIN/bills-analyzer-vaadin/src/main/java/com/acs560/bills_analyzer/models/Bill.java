package com.acs560.bills_analyzer.models;

import java.util.Comparator;
import java.util.Objects;

import com.acs560.bills_analyzer.entities.BillEntity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Bill implements Comparable<Bill> {
    
	@NotNull(message="Month is required")
	@Min(value=1, message="Month must be between 1 and 12")
	@Max(value=12, message="Month must be between 1 and 12")
    private int month;
	
	@NotNull(message="Year is required")
	@Min(value=1000, message="Year must be 4 digits")
	@Max(value=9999, message="Year must be 4 digits")
    private int year;
	
	@NotNull(message="Company is required")
    private Company company;
    
	/** If theres a string based non-pk field they need to add 
	*	@Size to validate it and display meaningful messages in the textbox.
	*/
	@NotNull(message="City is required")
	@Size(min = 2, max = 50, message = "City name must be between 2 and 50 characters")
	private String city;
	
	@NotNull(message="Amount is required")
    private double amount;
    
    
    /**
     * Returns the hash code of the Bill object.
     * The hash code is computed based on the fields of the Bill.
     *
     * @return the hash code of the Bill object
     */
    @Override
    public int hashCode() {
        return Objects.hash(month, year, company);
    }

    /**
     * Compares this Bill object with another object for equality.
     * Two Bill objects are considered equal if they have the same
     * values for all their fields.
     *
     * @param obj - the object to be compared
     * @return true if this Bill is equal to the other object, false otherwise
     */
    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bill other = (Bill) obj;
		return month == other.month && year == other.year &&
				Objects.equals(company, other.company);
	}
    
    /**
     * Constructs a new Bill object based on the provided BillEntity.
     *
     * @param be - the BillEntity object of the bill 
     */
    public Bill(BillEntity be) {
		this(be.getId().getBillingMonth(),
				be.getId().getBillingYear(),
				new Company(be.getCompany()),
				be.getCity(),
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
