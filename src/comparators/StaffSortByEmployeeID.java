package comparators;

import java.util.Comparator;

import resources.Staff;

public class StaffSortByEmployeeID implements Comparator<Staff>{

	public int compare(Staff a, Staff b) {
		return (a.getUID().compareTo(b.getUID()));
	}
}
