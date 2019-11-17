package resources;

import java.util.ArrayList;
import java.util.Date;

import com.google.cloud.Timestamp;

public class Invoice {
	String invoiceNumber, status, docID;
	ArrayList<String> replenishmentList;
	Date date;
	String deliveryType;
	public Invoice() {
		replenishmentList = new ArrayList<>();
		invoiceNumber = "0000000000";
		status = "Unknown";
		date = new Date();
		docID = "";
		deliveryType = "";
	}
	
	@SuppressWarnings("unchecked")
	public Invoice(String invoiceNo, String stat, Object list, Timestamp date, String docID, String deliveryType) {
		replenishmentList = ( ArrayList<String> ) list;
		invoiceNumber = invoiceNo;
		status = stat;
		this.date = date.toDate();
		this.docID = docID;
		this.deliveryType = deliveryType;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public String getStatus() {
		return status;
	}

	public ArrayList<String> getReplenishmentList() {
		return replenishmentList;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setReplenishmentList(ArrayList<String> replenishmentList) {
		this.replenishmentList = replenishmentList;
	}
	public Date getDate() {
		return date;
	}
	public String getDocID() {
		return docID;
	}
	
	public String getDeliveryType() {
		return deliveryType;
	}
	
	
}
