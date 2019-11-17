package comparators;

import java.util.Comparator;

import resources.Delivery;
import resources.Grocery;

public class InventorySortByQuantity implements Comparator<Grocery>{

	public int compare(Grocery a, Grocery b) {
		return (int) (a.getQuantity() - b.getQuantity());
	}
}
