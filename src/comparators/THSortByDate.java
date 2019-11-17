package comparators;

import java.util.Comparator;

import resources.TransactionHistory;

public class THSortByDate implements Comparator <TransactionHistory> {

	public int compare(TransactionHistory a, TransactionHistory b) {
		return a.getTransactionDate().compareTo(b.getTransactionDate());
	}

}