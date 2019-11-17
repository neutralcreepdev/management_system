package resources;

import java.io.Serializable;

public class Staff implements Serializable {
	String address, dob, email, employeeID, firstName, lastName, gender, contactNumber, role;
	
	public Staff() {}
	public Staff(String fName, String lName, String address, String email, String employeeID, String role, String gender, String dob) {
		firstName = fName;
		lastName = lName;
		this.address = address;
		this.email = email;
		this.employeeID = employeeID;
		this.gender = gender;
		this.dob = dob;
		this.role = role;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getAddressName() {
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
	public String toString() {
		return firstName + "" + lastName + "" + address + "" + email + "" + employeeID + "" + gender + "" + dob + "" +role;
	}
}
