package com.example.limitservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.limitservices.bean.LimitConfiguration;
import com.example.limitservices.config.Configuration;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class LimitsConfigurationController {
	
	@Autowired
	private Configuration configuration;

	@GetMapping("/limits")
	public LimitConfiguration retrievalLimitsFromConfigurations() {
		
		return new LimitConfiguration(configuration.getMaximum(),configuration.getMinimum());
	}
	
	@GetMapping("/fault-tolerance-example")
	@HystrixCommand(fallbackMethod="fallbackRetrieveConfiguration")
	public LimitConfiguration retrieveConfiguration() {
		
		throw new RuntimeException("not available");
	}
	
	public LimitConfiguration fallbackRetrieveConfiguration() {
		
		return new LimitConfiguration(9,999);
	}
}
