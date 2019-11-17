package comparators;

import java.util.Comparator;

import resources.Delivery;

public class DeliverySortByDeliveryDate implements Comparator <Delivery> {

	public int compare(Delivery a, Delivery b) {
		return a.getExpectedDate().compareTo(b.getExpectedDate());
	}

}