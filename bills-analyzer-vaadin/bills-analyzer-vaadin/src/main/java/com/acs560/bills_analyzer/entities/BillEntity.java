package com.acs560.bills_analyzer.entities;

import com.acs560.bills_analyzer.models.Bill;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "bills")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BillEntity {
 
    @EmbeddedId
    private BillEntityId id;
    
    @ManyToOne
    @MapsId("companyId")
    private CompanyEntity company;
    
    private double amount;
    

 
    public BillEntityId getId() {
        return id;
    }
    
    public BillEntity(Bill bill) {
    	this(new BillEntityId(bill.getMonth(), bill.getYear(), bill.getCompany().getId()), 
    			new CompanyEntity(bill.getCompany()), 
    		bill.getAmount());
    }

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}