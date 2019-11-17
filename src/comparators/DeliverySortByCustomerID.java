package comparators;

import java.util.Comparator;

import resources.Delivery;

public class DeliverySortByCustomerID implements Comparator <Delivery> {

	public int compare(Delivery a, Delivery b) {
		return a.getCustomerID().compareTo(b.getCustomerID());
	}

}