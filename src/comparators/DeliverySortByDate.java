package comparators;

import java.util.Comparator;

import resources.Delivery;

public class DeliverySortByDate implements Comparator <Delivery> {

	public int compare(Delivery a, Delivery b) {
		return a.getDate().compareTo(b.getDate());
	}

}