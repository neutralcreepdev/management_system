package comparators;

import java.util.Comparator;

import resources.TransactionHistory;

public class THSortByCID implements Comparator<TransactionHistory>{

	public int compare(TransactionHistory a, TransactionHistory b) {
		return (a.getCustomerID().compareTo(b.getCustomerID()));
	}
} 
