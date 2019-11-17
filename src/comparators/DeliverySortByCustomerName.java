package comparators;

import java.util.Comparator;

import resources.Delivery;

public class DeliverySortByCustomerName implements Comparator<Delivery> {
	public int compare(Delivery a, Delivery b) {
		return a.getCustomerName().compareTo(b.getCustomerName());
		
	}
}
