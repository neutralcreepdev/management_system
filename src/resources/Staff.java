package resources;

import java.io.Serializable;

public class Staff implements Serializable {
	String address, dob, email, employeeID, firstName, lastName, gender, contactNumber, role, docID;
	
	public Staff() {}
	public Staff(String fName, String lName, String address, String email, String employeeID, String role, String gender, String dob, String cNumber, String docID) {
		firstName = fName;
		lastName = lName;
		this.address = address;
		this.email = email;
		this.employeeID = employeeID;
		this.gender = gender;
		this.dob = dob;
		this.role = role;
		contactNumber = cNumber;
		this.docID = docID;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getAddress() {
		return address;
	}
	public String getEmail() {
		return email;
	}
	public String getEmployeeID() {
		return employeeID;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public String getGender() {
		return gender;
	}
	public String getDOB() {
		return dob;
	}
	public String getRole() {
		return role;
	}
	public String getDocID() {
		return docID;
	}
	public void setFirstName(String input) {
		firstName = input;
	}
	public void setLastName(String input) {
		lastName  = input;
	}
	public void setAddress(String input) {
		address = input;
	}
	public void setEmail(String input) {
		email = input;
	}
	public void setEmployeeID(String input) {
		employeeID = input;
	}
	public void setContactNumber(String input) {
		contactNumber = input;
	}
	public void setGender(String input) {
		gender = input;
	}
	public void setDOB(String input) {
		dob = input;
	}
	public void setRole(String input) {
		role = input;
	}
	public void setDocID(String input) {
		docID = input;
	}
	
	
	public String toString() {
		return firstName + "" + lastName + "" + address + "" + email + "" + employeeID + "" + gender + "" + dob + "" +role;
	}
}
