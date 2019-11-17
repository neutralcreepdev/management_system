package resources;

import java.util.Date;

import com.google.cloud.Timestamp;

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
	
	public Delivery(String tID, String cID, String eID, String dID, String newStatus, Timestamp a) {
		transactionID = tID;
		customerID = cID;
		employeeID = eID;
		deliveryID = dID;
		status = newStatus;
		deliveryDate = a.toDate() ;
	}
	
	public void setDeliveryStatus(String newStatus) {
		status = newStatus;
	}
	public String getDeliveryStatus() {
		return status;
	}
	public void setDeliveryDate(Date date) {
		deliveryDate = date;
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
	public Date getDate() {
		return deliveryDate;
	}
	
	public String toString() {
		return "Delivery ID: " + deliveryID + " | CustomerID: " + customerID + " | EmployeeID: " + employeeID + " | TransactionID: " + transactionID + " | Status: " + status;
	}
	
}
