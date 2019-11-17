package comparators;

import java.util.Comparator;

import resources.Staff;

public class StaffSortByName implements Comparator<Staff> {

	public int firstOrLast = 0;
	
	public StaffSortByName(int a) {
		
		firstOrLast = a;
		
	}
	public int compare(Staff a, Staff b) {
		
		if ( firstOrLast == 0 )
			return (a.getFirstName().compareTo(b.getFirstName()));
		else 
			return (a.getLastName().compareTo(b.getLastName()));
	}
}
