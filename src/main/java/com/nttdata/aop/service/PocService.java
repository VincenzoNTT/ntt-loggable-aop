package com.nttdata.aop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.nttdata.aop.Loggable;

@Component
public class PocService {

	private static final Logger logger = LoggerFactory.getLogger(PocService.class);
	
	int res = Integer.MIN_VALUE;
	
	@Loggable
	public int getCall() {
		return res;
	}
	
	@Loggable
	public Integer multiply(int a, int b){
		int res = a*b;
		return res;
	}
	
	@Loggable(message = "displayBalance fired")
	public void displayBalance(String accNum) {
				
		if(accNum.equals("12345")) {
			logger.debug("Total balance in the given account number is= XXXXX");
		} else {
			logger.debug("Account number mismatch.");
		}
		
	}
}