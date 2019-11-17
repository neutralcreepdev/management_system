package comparators;

import java.util.Comparator;

import resources.Grocery;

public class InventorySortByPrice implements Comparator<Grocery>{

	public int compare(Grocery a, Grocery b) {
		
		double result = a.getPrice() - b.getPrice();
		
		if ( result > 0.01 )
			return 1;
		else 
			return -1;
	}
}
