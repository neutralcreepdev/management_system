package resources;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionHistory {

	String transactionID, custID, status, type;
	double amount;
	Date transactionDate;
	Customer cProfile;
	Object listOfItems;
	
	public TransactionHistory() {
		transactionID = "";
		status = "";
		custID = "";
		type = "";
		amount = 0.0;
		cProfile = new Customer();
	}
	
	public TransactionHistory(String transactionID, String customerID, String status, String type, double amt, Date d) {
		this.transactionID = transactionID;
		custID = customerID;
		this.status = status;
		this.type = type;
		amount = amt;
		transactionDate = d;
		cProfile = new Customer();
	}

	public String getTransactionID() {
		return transactionID;
	}
	
	public String getCustomerID() {
		return custID;
	}

	public String getStatus() {
		return status;
	}

	public String getType() {
		return type;
	}

	public double getAmount() {
		return amount;
	}
	public List<Object> getArray() { 
		return (List<Object>) Arrays.asList(listOfItems);
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	
	public Customer getCustomer() {
		return cProfile;
	}

	public void setCustomerID(String customerID) {
		this.custID = customerID;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public void setCustomer(Customer c) {
		this.cProfile = c;
	}
	
	public void setArray(Object listOfItems) {
		this.listOfItems = listOfItems;
	}
	
	public boolean isToday() {
		Calendar now = Calendar.getInstance();
		System.out.println("Date today: " + now.toString());
		
		if (now.getTime().compareTo(transactionDate) == 0 ) {
			System.out.println("isToday == true");
			return true;
		} 
		
		return false;
	}
	
	
	
}
