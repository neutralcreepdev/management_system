package comparators;

import java.util.Comparator;

import resources.Grocery;

public class InventorySortByItemID implements Comparator<Grocery>{

	public int compare(Grocery a, Grocery b) {
		return (int) (a.getItemID() - b.getItemID());
	}
}
