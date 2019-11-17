package comparators;

import java.util.Comparator;

import resources.Invoice;

public class InvoiceSortByDate implements Comparator <Invoice> {

	public int compare(Invoice a, Invoice b) {
		return a.getDate().compareTo(b.getDate());
	}

}
