package com.acs560.bills_analyzer.entities;

import com.acs560.bills_analyzer.models.Company;
import com.acs560.bills_analyzer.requests.CompanyRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "companies")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CompanyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique=true)
	private String name;
	
	public CompanyEntity (CompanyRequest c) {
		this.name = c.getName();
	}
	
	public CompanyEntity(Company c) {
		this(c.getId(), c.getName());
	}
}
