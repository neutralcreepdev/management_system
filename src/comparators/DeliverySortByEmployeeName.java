package comparators;

import java.util.Comparator;

import resources.Delivery;

public class DeliverySortByEmployeeName implements Comparator <Delivery> {

	public int compare(Delivery a, Delivery b) {
		return a.getEmployeeName().compareTo(b.getEmployeeName());
	}

}