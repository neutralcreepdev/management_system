package resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.cloud.Timestamp;

public class Delivery {
	
	private String transactionID, customerID, employeeID, status;
	private String custName, eName, type;
	private String actualDate, expectedDate;
	private String actualTime, expectedTime;
	private String address;
	private double amount;
	private Object itemList;
	Date transactionDate;
	
	public Delivery() {
		transactionID = "";
		customerID = "";
		employeeID = "";
		status = "";
		custName = "<Not Set>";
		type = "";
		eName = "<Not Set>";
		transactionDate = new Date();
		address = "<Not Set>";
		amount = 0.0;
	}
	//Past Delivery Constructor [Update with the Time fields inside firebase]
	//Assigned Pending Deliveries Constructor
	
	public Delivery(String tID, String cID, String eID, String newStatus, Timestamp transactionDate, String custName, String type, Map<String, String> address, double totalAmount, Object itemList) {
		transactionID = tID;
		customerID = cID;
		employeeID = eID;
		status = newStatus;
		this.transactionDate = transactionDate.toDate();
		this.custName = custName;
		this.type = type;
		eName = "<Not Set>";
		setAddress(address);
		amount = totalAmount;
		this.itemList = itemList;
		actualDate = "";
		actualTime = "";
		expectedDate = "";
		expectedTime = "";
	}
	
	public void setDeliveryStatus(String newStatus) {
		status = newStatus;
	}
	public String getDeliveryStatus() {
		return status;
	}
	public void setTransactionDate(Date date) {
		transactionDate = date;
	}
	public Date getTransactionDate() {
		return transactionDate;
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
	public String getCustomerName() {
		return custName;
	}
	public String getType() {
		return type;
	}
	public void setEmployeeName(String name) {
		this.eName = name;
	}
	public String getEmployeeName() {
		return eName;
	}
	public String getExpectedDate() {
		return expectedDate;
	}
	public String getExpectedTime() {
		return expectedTime;
	}
	public String getActualDate() {
		return actualDate;
	}
	public String getActualTime() {
		return actualTime;
	}
	@SuppressWarnings("unchecked")
	public void setFormattedDateToOrdinaryDateObject(Map<Object, Object> dateObject, String type) {
		String input = "";
		Date d;
		
		//Remove the first layer of Map<Object, Object> to Map<String, String>
		Map<String, String> javaDateObject = (Map<String, String>) dateObject.get("date");	
		input = javaDateObject.get("day") + javaDateObject.get("month") + javaDateObject.get("year");
		
		try {
			d = new SimpleDateFormat("ddMMyyyy").parse(input);
			
			if ( type.equals ("actualTime")) {
				transactionDate = d;
				actualDate = new SimpleDateFormat("dd/MM/yyyy").format(d);
				actualTime = (String)dateObject.get("time");
			} else if (type.equals("expectedTime")) {
				expectedDate = new SimpleDateFormat("dd/MM/yyyy").format(d);
				expectedTime = (String)dateObject.get("time");	
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void setAddress(Map<String, String> map) {
		address = map.get("street") +  " "  + map.get("unit") + " S" + map.get("postalCode");
	}	
	
	public String getAddress() {
		return address;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getAmount() {
		return amount;
	}
	public List<Object> getItemList(){
		return Arrays.asList(itemList);
	}
	
	public String toString() {
		return "CustomerID: " + customerID + " | EmployeeID: " + employeeID + " | TransactionID: " + transactionID + " | Status: " + status;
	}
	
}
