package com.acs560.bills_analyzer.entities;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BillEntityId implements Serializable {
 
	private static final long serialVersionUID = 7252327203288421943L;
    private int billingMonth;
    private int billingYear;
    private int companyId;
 
}