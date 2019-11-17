package resources;

import java.util.HashMap;
import java.util.Map;

public class Customer {

	String UID, firstName, lastName, gender, contactNumber, docID;
	String dd, mm, yy; 		//DOB
	String street, postal, unit; //ADDRESS
	public Customer() {}
	
	public Customer(String fName, String lName, String docID, HashMap<String, String> dob, HashMap<String, String> address, String contactNumber) {
		this.firstName = fName;
		this.lastName = lName;
		this.docID = docID;
		this.UID = docID;
		this.contactNumber = contactNumber;
		try {
			mapSplitDOB(dob);
			mapSplitAddress(address);
		} catch(Exception e) {
			dd = "";
			mm = "";
			yy = "";
			street = "";
			postal = "";
			unit = "";
		}
	}

	public String getUID() {
		return UID;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getGender() {
		return gender;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public String getDocID() {
		return docID;
	}

	public String getDD() {
		return dd;
	}

	public String getMM() {
		return mm;
	}

	public String getYY() {
		return yy;
	}

	public String getStreet() {
		return street;
	}

	public String getPostal() {
		return postal;
	}

	public String getUnit() {
		return unit;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public void setDocID(String docID) {
		this.docID = docID;
	}

	public void setDD(String dd) {
		this.dd = dd;
	}

	public void setMM(String mm) {
		this.mm = mm;
	}

	public void setYY(String yy) {
		this.yy = yy;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public void seperateDOB(String dob) {
		String [] a = dob.split("/");
		dd = a[0];
		mm = a[1];
		yy = a[2];
	}
	public void mapSplitDOB(Map<String, String> map) {
		dd = map.get("day");
		mm = map.get("month");
		yy = map.get("year");
	}
	public void mapSplitAddress(Map<String, String> map) {
		postal = map.get("postalCode");
		street = map.get("street");
		unit = map.get("unit");
		
	}
	
	public String getDOB() {
		return dd+"/"+mm+"/"+yy;
	}
}
