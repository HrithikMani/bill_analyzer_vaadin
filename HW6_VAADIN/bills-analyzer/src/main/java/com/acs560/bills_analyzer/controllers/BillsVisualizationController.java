package com.acs560.bills_analyzer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acs560.bills_analyzer.services.BillsVisualizationService;

import lombok.NoArgsConstructor;

/**
 * The rest controller for handling bills visualization data
 * This has end-points for visualizing bills data 
 * 
 */
@RestController()
@RequestMapping("/api/v1/bills")
@NoArgsConstructor
public class BillsVisualizationController {

		//The bills analysis service that is injected through autowire
		@Autowired
		private BillsVisualizationService billsVisualizationService;
		
		/**
		 * Constructs a new instance of BillsVisualizationController
		 */
		public BillsVisualizationController(BillsVisualizationService billsVisualizationService) {
			this.billsVisualizationService = billsVisualizationService;
		}
		
		/**
		 * Get an overall visualization of bills for expenditure name
		 * 	
		 * @param name - the name of the expenditure
		 * @return A bar plot of top 10 cities expenditure as a png image
		 */
		@GetMapping("/visualize/id/{id}")
	    public ResponseEntity<byte[]> getBillsVisualization(@PathVariable int id) {
	        byte[] chartImage = billsVisualizationService.plotSpending(id);
	        
	        if (chartImage == null || chartImage.length == 0) {
	            return ResponseEntity.status(500).body(null);  // Handle error case
	        }

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.IMAGE_JPEG);

	        return ResponseEntity.ok()
	                .headers(headers)
	                .body(chartImage);
	    }
}
