package comparators;

import java.util.Comparator;

import resources.Delivery;

public class DeliverySortByOrderNumber implements Comparator<Delivery> {

	public int compare(Delivery a, Delivery b) {
		return a.getDeliveryID().compareTo(b.getDeliveryID());
	}
}
