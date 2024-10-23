package com.acs560.bills_analyzer.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.acs560.bills_analyzer.models.Bill;
import com.acs560.bills_analyzer.services.BillsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class BillsControllerTest {

	private static final String BILLS_URL = "/api/v1/bills";

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private BillsService bs;
	
	@Test
	public void testGetBills_shouldReturnOKStatus() throws Exception {
		Mockito.when(bs.getBills()).thenReturn(List.of(new Bill()));
		
		var result = mockMvc.perform(
			MockMvcRequestBuilders.get(BILLS_URL)).andReturn();
		
		Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		Assertions.assertTrue(result.getResponse().getContentAsString().length() > 0);
	}
	
	@Test
	public void testGetBillsForNonexistingCompany_shouldReturnNotFoundStatus() throws Exception {
		Mockito.when(bs.getBillsByCompany(Mockito.anyInt())).thenThrow(NoSuchElementException.class);
		
		var result = mockMvc.perform(
			MockMvcRequestBuilders.get(BILLS_URL + "/company/888")).andReturn();
		
		Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
	}
	
	@Test
	public void testGetBillsForNonexistingMonth_shouldReturnNotFoundStatus() throws Exception {
		Mockito.when(bs.getBillsByMonth(Mockito.anyInt())).thenThrow(NoSuchElementException.class);
		
		var result = mockMvc.perform(
			MockMvcRequestBuilders.get(BILLS_URL + "/month/8")).andReturn();
		
		Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
	}
	
	@Test
	public void testGetBillThatDoesntExist_shouldReturnNotFoundStatus() throws Exception {
		Mockito.when(bs.getBills(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()))
			.thenThrow(NoSuchElementException.class);
		
		var result = mockMvc.perform(
			MockMvcRequestBuilders.get(BILLS_URL + "/company/888/month/1/year/2023")).andReturn();
		
		Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
	}
	
	//TODO mcp test add new bill returns CREATED status
	
	@Test
	public void testAddBillThatExists_shouldReturnBadStatus() throws Exception {
		Mockito.doThrow(IllegalArgumentException.class).when(bs).add(Mockito.any(Bill.class));
		
		ObjectMapper om = new ObjectMapper();
		
		var result = mockMvc
					.perform(MockMvcRequestBuilders.post(BILLS_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(om.writeValueAsString(new Bill())))
				.andReturn();
			
			Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
	}
	
	//TODO mcp test update bill returns OK status
	
	@Test
	public void testUpdateBillThatDoesntExist_shouldReturnBadStatus() throws Exception {
		Mockito.doThrow(IllegalArgumentException.class).when(bs).update(Mockito.any(Bill.class));
		
		ObjectMapper om = new ObjectMapper();
		
		var result = mockMvc
					.perform(MockMvcRequestBuilders.put(BILLS_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(om.writeValueAsString(new Bill())))
				.andReturn();
			
			Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
	}
	
	//TODO MCP test delete bill returns OK status
	
	@Test
	public void testDeleteBillThatDoesntExist_shouldReturnBadStatus() throws Exception {
		Mockito.doThrow(IllegalArgumentException.class).when(bs).delete(Mockito.any(Bill.class));

		ObjectMapper om = new ObjectMapper();
		
		var result = mockMvc
					.perform(MockMvcRequestBuilders.delete(BILLS_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(om.writeValueAsString(new Bill())))
				.andReturn();
			
			Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
	}
}
