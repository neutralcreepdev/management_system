package comparators;

import java.util.Comparator;

import resources.Invoice;

public class InvoiceSortByID implements Comparator <Invoice> {

	public int compare(Invoice a, Invoice b) {
		return a.getInvoiceNumber().compareTo(b.getInvoiceNumber());
	}

}
