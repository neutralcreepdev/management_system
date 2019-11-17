package resources;

import java.io.Serializable;
import java.util.Map;

public class Staff implements Serializable {
	String email, UID, firstName, lastName, gender, contactNumber, role, docID;
	String dd, mm, yy;
	String street, postal, unit;
	public Staff() {}
	
	public Staff(String fName, String lName, Map<String, String> address, String email, String role, String gender, Map<String, String> dob, String cNumber, String docID, String UID) {
		this.firstName = fName;
		this.lastName = lName;
		this.email = email;
		this.UID = UID;
		this.gender = gender;
		this.role = role;
		this.contactNumber = cNumber;
		this.docID = docID;
		dd = "";
		mm = "";
		yy = "";
		street = "";
		postal = "";
		unit = "";
		mapSplitDOB(dob);
		mapSplitAddress(address);
	}
	
	public Staff(String fName, String lName, String email, String role, String gender, String dob, String cNumber, String docID, String UID) {
		this.firstName = fName;
		this.lastName = lName;
		this.email = email;
		this.UID = UID;
		this.gender = gender;
		this.role = role;
		this.contactNumber = cNumber;
		this.docID = docID;
		dd = "";
		mm = "";
		yy = "";
		street = "";
		postal = "";
		unit = "";
		try {
			seperateDOB(dob);
		} catch (NullPointerException e) {
			dd = "";
			mm = "";
			yy = "";
			System.out.println("Nothing set to DOB - DDMMYY");
		}
	}
	
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getEmail() {
		return email;
	}
	public String getUID() {
		return UID;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public String getGender() {
		return gender;
	}
	public String getRole() {
		return role;
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
	
	public void setFirstName(String input) {
		firstName = input;
	}
	public void setLastName(String input) {
		lastName  = input;
	}
	public void setEmail(String input) {
		email = input;
	}
	public void setUID(String input) {
		UID = input;
	}
	public void setContactNumber(String input) {
		contactNumber = input;
	}
	public void setGender(String input) {
		gender = input;
	}
	public void setRole(String input) {
		role = input;
	}
	public void setDocID(String input) {
		docID = input;
	}
	public void setDD(String input) {
		dd = input;
	}
	public void setMM(String input) {
		mm = input;
	}
	public void setYY(String input) {
		yy = input;
	}
	public void setStreet(String input) {
		street = input;
	}
	public void setPostal(String input) {
		postal = input;
	}
	public void setUnit(String input) {
		unit = input;
	}
	public void setAddress(Map<String, String> input) {
		street = input.get("street");
		postal = input.get("postalCode");
		unit = input.get("unit");
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
	
	public String toString() {
		return firstName + "" + lastName + "" + email + "" + UID + "" + gender + "" +"" +role;
	}
}
