package comparators;

import java.util.Comparator;

import resources.Delivery;

public class DeliverySortByStatus implements Comparator <Delivery> {

	public int compare(Delivery a, Delivery b) {
		return a.getDeliveryStatus().compareTo(b.getDeliveryStatus());
	}

}
