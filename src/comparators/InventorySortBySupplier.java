package comparators;

import java.util.Comparator;

import resources.Grocery;

public class InventorySortBySupplier implements Comparator<Grocery>{

	public int compare(Grocery a, Grocery b) {
		return (int) (a.getSupplier().compareTo(b.getSupplier()));
	}
}
