package com.acs560.bills_analyzer.services.impl;

import com.acs560.bills_analyzer.models.Bill;
import com.acs560.bills_analyzer.services.BillsService;
import com.acs560.bills_analyzer.services.BillsVisualizationService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BillsVisualizationServiceImpl implements BillsVisualizationService {
	
	@Autowired
	BillsService bs;
	
	/**
	 * Generate a bar plot of bills for a given expenditure name
	 * 	
	 * @param name - the name of the expenditure
	 * @return A bar plot of top 10 cities expenditure as a png image
	 */
	@Override
    public byte[] plotSpending(int companyId) {
		// Get all bills for a given expenditure name
		List<Bill> bills = bs.getBillsByCompany(companyId);
        
		// Group data by city and sum the amount spent
        Map<String, Double> spendingData = bills
        								   .stream()
                						   .collect(Collectors.groupingBy(Bill::getCity, 
                								    Collectors.summingDouble(Bill::getAmount)));
        
        // Get the top 10 cities by spending
        List<Map.Entry<String, Double>> topCities = spendingData.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
        
        // Create a dataset for the bar chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Double> entry : topCities) {
            dataset.addValue(entry.getValue(), entry.getKey(), entry.getKey());
        }

        // Create a bar chart
        JFreeChart barChart = ChartFactory.createBarChart(
                "Top 10 city-wise spending for " + companyId,
                "City",
                "Amount",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        
        // Convert the chart to a BufferedImage
        BufferedImage chartImage = barChart.createBufferedImage(800, 600);

        // Save the image to the file system
        try {
            File outputFile = new File("./plots/bills_travel_spending_chart.jpeg");
            ImageIO.write(chartImage, "png", outputFile);
            System.out.println("Chart saved to: " + outputFile.getAbsolutePath());
            System.out.println("Chart saved to: " + chartImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert the image to a byte array for API response
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(chartImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
