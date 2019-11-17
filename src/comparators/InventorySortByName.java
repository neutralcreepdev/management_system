package comparators;

import java.util.Comparator;

import resources.Grocery;

public class InventorySortByName implements Comparator<Grocery> {

	public int compare(Grocery a, Grocery b) {
		return (a.getName().compareTo(b.getName()));
	}
}
