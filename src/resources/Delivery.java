package resources;

import java.util.Date;

public class Delivery {
	
	private String deliveryID, transactionID, customerID, employeeID, status;
	Date deliveryDate;
	
	public Delivery() {
		transactionID = "";
		customerID = "";
		employeeID = "";
		deliveryID = "";
		status = "";
		deliveryDate = new Date();
	}
	
	public Delivery(String tID, String cID, String eID, String dID) {
		transactionID = tID;
		customerID = cID;
		employeeID = eID;
		deliveryID = dID;
		status = "Waiting";
		deliveryDate = new Date();
	}
	
	public void setDeliveryStatus(String newStatus) {
		status = newStatus;
	}
	public String getDeliveryStatus() {
		return status;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public String getTransactionID() {
		return transactionID;
	}
	public String getCustomerID() {
		return customerID;
	}
	public String getEmployeeID() {
		return employeeID;
	}
	public String getDeliveryID() {
		return deliveryID;
	}
	
	public String toString() {
		return "Delivery ID: " + deliveryID + " | CustomerID: " + customerID + " | EmployeeID: " + employeeID + " | TransactionID: " + transactionID + " | Status: " + status;
	}
	
}
