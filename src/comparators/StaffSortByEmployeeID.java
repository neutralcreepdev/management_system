package comparators;

import java.util.Comparator;

import resources.Grocery;
import resources.Staff;

public class StaffSortByEmployeeID implements Comparator<Staff>{

	public int compare(Staff a, Staff b) {
		return (a.getEmployeeID().compareTo(b.getEmployeeID()));
	}
}
