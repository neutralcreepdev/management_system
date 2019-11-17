package comparators;

import java.util.Comparator;

import resources.TransactionHistory;

public class THSortByType implements Comparator<TransactionHistory>{

	public int compare(TransactionHistory a, TransactionHistory b) {
		return (a.getType().compareTo(b.getType()));
	}
} 
