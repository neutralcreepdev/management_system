package comparators;

import java.util.Comparator;

import resources.Staff;

public class StaffSortByRole implements Comparator<Staff>{

	public int compare(Staff a, Staff b) {
		return (a.getRole().compareTo(b.getRole()));
	}
}
