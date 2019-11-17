package comparators;

import java.util.Comparator;

import resources.Delivery;

public class DeliverySortByEmployeeID implements Comparator <Delivery> {

	public int compare(Delivery a, Delivery b) {
		return a.getEmployeeID().compareTo(b.getEmployeeID());
	}

}