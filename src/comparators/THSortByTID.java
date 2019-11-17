package comparators;

import java.util.Comparator;

import resources.TransactionHistory;

public class THSortByTID implements Comparator<TransactionHistory>{

	public int compare(TransactionHistory a, TransactionHistory b) {
		return (a.getTransactionID().compareTo(b.getTransactionID()));
	}
} 