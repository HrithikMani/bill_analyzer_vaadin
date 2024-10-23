package com.acs560.bills_analyzer.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acs560.bills_analyzer.entities.BillEntity;
import com.acs560.bills_analyzer.entities.BillEntityId;
import com.acs560.bills_analyzer.models.Bill;
import com.acs560.bills_analyzer.repositories.BillsRepository;
import com.acs560.bills_analyzer.services.BillsService;

import jakarta.validation.Valid;

/**
 * The bills service implementation
 */
@Service
public class BillsServiceImpl implements BillsService {
	
	private BillsRepository br;
	
	@Autowired
	public BillsServiceImpl(BillsRepository br) {
		this.br = br;
	}
	
	@Override
	public Optional<Bill> getBill(int companyId, int month, int year) {
		BillEntityId id = new BillEntityId(month, year, companyId);
		var be = br.findById(id);
		Optional<Bill> result = be.isPresent() ? Optional.of(new Bill(be.get())) : Optional.empty(); 
		
		return result;
	}

	@Override
	public List<Bill> getBills(){
		var billEntities = ((List<BillEntity>) br.findAll());
		return from(billEntities);
	}

	@Override
	public List<Bill> getBillsByCompany(int companyId) {
		var billEntities = br.findAllByIdCompanyId(companyId);
		return from(billEntities);
	}
	
	@Override
	public List<Bill> getBillsByCompanyAndMonth(int companyId, int month) {
		var billEntities = br.findAllByIdBillingMonthAndIdCompanyId(month, companyId);
		return from(billEntities);
	}
	
	@Override
	public List<Bill> getBillsByMonth(int month) {
		var billEntities = br.findAllByIdBillingMonth(month);
		return from(billEntities);
	}
	
	@Override
	public List<Bill> getBillsByMonthAndRange(int month, int range) {
		var months = MonthRangeUtil.getRange(month, range);
		var billEntities = br.findAllByIdBillingMonthIn(months);
		
		return from(billEntities);
	}
	
	@Override
	public List<Bill> getBills(int companyId, int month, int range) {
		var months = MonthRangeUtil.getRange(month, range);
		var billEntities = br.findAllByIdCompanyIdAndIdBillingMonthIn(companyId, months);
		
		return from(billEntities);
	}

	@Override
	public Bill add(@Valid Bill bill) {
		Bill addedBill = null;
		
		BillEntity billToAdd = new BillEntity(bill);
		
		if (!br.existsById(billToAdd.getId())) {
			var billEntity = br.save(billToAdd);
			addedBill = new Bill(billEntity);
		}

		return addedBill;
	}

	@Override
	public boolean delete(@Valid Bill bill) {
		boolean isDeleted = false;
		BillEntity billToDelete = new BillEntity(bill);
		
		if (br.existsById(billToDelete.getId())) {
			br.delete(new BillEntity(bill));
			isDeleted = true;
		}
		return isDeleted;
	}

	@Override
	public Bill update(@Valid Bill bill) {
		Bill updatedBill = null;
		
		BillEntity billToUpdate = new BillEntity(bill);
		
		if (br.existsById(billToUpdate.getId())) {
			var updatedBillEntity = br.save(new BillEntity(bill));
			updatedBill = new Bill(updatedBillEntity);
		}

		return updatedBill;
	}

	private List<Bill> from(List<BillEntity> billEntities){
		var bills = billEntities.stream()
				.map(be -> new Bill(be))
				.collect(Collectors.toList());

		if (bills.isEmpty()) {
			throw new NoSuchElementException();
		}
		
		Collections.sort(bills);
		
		return bills;
	}

}
