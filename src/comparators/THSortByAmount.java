package comparators;

import java.util.Comparator;

import resources.TransactionHistory;

public class THSortByAmount implements Comparator<TransactionHistory>{

	public int compare(TransactionHistory a, TransactionHistory b) {

		double result = a.getAmount() - b.getAmount();
		
		if ( result > 0.01 )
			return 1;
		else 
			return -1;
	}
} 
