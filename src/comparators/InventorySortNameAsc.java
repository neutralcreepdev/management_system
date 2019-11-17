package comparators;

import java.util.Comparator;

import resources.Delivery;
import resources.Grocery;

public class InventorySortNameAsc implements Comparator<Grocery> {

	public int compare(Grocery a, Grocery b) {
		return (a.toString().compareTo(b.toString()));
	}
}
