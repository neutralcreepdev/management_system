package tables;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import comparators.THSortByAmount;
import comparators.THSortByCID;
import comparators.THSortByDate;
import comparators.THSortByTID;
import comparators.THSortByType;
import panels.TaskBar;
import resources.TransactionHistory;


//http://www.java2s.com/Tutorial/Java/0240__Swing/CustomizingColumnHeaderswithIcons.htm

//Need to add a searchbar
@SuppressWarnings("serial")
public class TransactionHistoryTable extends JPanel implements MouseListener {

	JTable table;
	JScrollPane scrollPane;
	List<TransactionHistory> data;
	TransactionHistory currTransaction;
	ModelData modelData;
	boolean dID = true, dDate = true, dStatus = true, dEID = true, dCID = true;
	final JTableHeader header;
	TaskBar tb;
	int sizeOfTable = 0;

	public TransactionHistoryTable(TaskBar t) {
		tb = t;
		modelData = new ModelData();
		table = new JTable(modelData);
		currTransaction = new TransactionHistory();

		scrollPane = new JScrollPane(table);

		table.setAutoscrolls(true);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		table.setVisible(true);
		table.addMouseListener(this);
		add(scrollPane);

		DefaultTableCellRenderer middleRenderer = new DefaultTableCellRenderer();
		middleRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		table.getColumn("Transaction ID").setCellRenderer( middleRenderer );
		table.getColumn("Transaction Date").setCellRenderer( middleRenderer );
		table.getColumn("Customer Name").setCellRenderer( middleRenderer );
		table.getColumn("Type").setCellRenderer( middleRenderer);
		table.getColumn("Amount").setCellRenderer(middleRenderer);
		
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	    
		header = table.getTableHeader();

		// Doesn't allow swapping of columns
		header.setReorderingAllowed(false);

		// Adds a mouse listener
		header.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int column = header.columnAtPoint(e.getPoint());
				sort(column);
			}
		}); // End of header's mouseListener
	}

	public double filterByDay(String condition) {
		double totalAmount = 0.00;
		ArrayList<TransactionHistory> sortedByDay = new ArrayList<>();

		int d = Calendar.getInstance().get(Calendar.MONTH)+1;

		String month = "Jan";
		switch(d) {
		
		case 1: month = "Jan"; break;
		case 2: month = "Feb"; break;
		case 3: month = "Mar"; break;
		case 4: month = "Apr"; break;
		case 5: month = "May"; break;
		case 6: month = "Jun"; break;
		case 7: month = "Jul"; break;
		case 8: month = "Aug"; break;
		case 9: month = "Sep"; break;
		case 10: month = "Oct"; break;
		case 11: month = "Nov"; break;
		case 12: month = "Dec"; break;
			
		}
		
		System.out.println("month    " + month);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM");
		
		for (TransactionHistory temp : data) {
			
			if (sdf.format(temp.getTransactionDate()).equalsIgnoreCase(condition + "/" + month) ) {
				sortedByDay.add(temp);
				totalAmount += temp.getAmount();
			}
		}
		
		if (sortedByDay.size() <= 0)
			return -1;
		else { 
			sizeOfTable = sortedByDay.size();
			updateTable(sortedByDay);
			return totalAmount;
		}
	}
	
	public double filterByMonth(String condition) {
		double totalAmount = 0.00;
		ArrayList<TransactionHistory> sortedByMonth = new ArrayList<>();
	
		SimpleDateFormat sdf = new SimpleDateFormat("MMM");
		
		for (TransactionHistory temp : data) {
			
			if (sdf.format(temp.getTransactionDate()).equalsIgnoreCase(condition) ) {
				sortedByMonth.add(temp);
				totalAmount += temp.getAmount();
			}
		}
		
		if (sortedByMonth.size() <= 0)
			return -1;
		else { 
			sizeOfTable = sortedByMonth.size();
			updateTable(sortedByMonth);
			return totalAmount;
		}
	}

	public ArrayList<TransactionHistory> getTableList() {
		return (ArrayList<TransactionHistory>) data;
	}
	
	//"Transaction ID", "Transaction Date", "Customer ID", "Type", "Amount"	
	public void sort(int column) {
		switch (column) {
		case 0 :
			Collections.sort(data, new THSortByTID());
			
			if (dID == false) 
				Collections.reverse(data);
			
			dID = !dID;
			
		break;
		case 1 : 
			Collections.sort(data, new THSortByDate()); 
			
				if (dDate == false)
					Collections.reverse(data);
			dDate = !dDate;
		break;

		case 2:
			Collections.sort(data, new THSortByCID());
			if(dEID)
				Collections.reverse(data);
			dEID = !dEID;
		break;
		case 3: 
			Collections.sort(data, new THSortByType());
			if(dCID)
				Collections.reverse(data);
			dCID = !dCID;
		break;
		
		case 4 : 
			Collections.sort(data, new THSortByAmount());
			if (dStatus) 
				Collections.reverse(data);
			dStatus = !dStatus;
		break;

		}

		//Update the display
		table.revalidate();
		table.repaint();
		System.out.println("Sorted Transaction History Table");

	}
		
	public TransactionHistory getSelectedTransaction() {
		return currTransaction;
	}

	public void mousePressed(MouseEvent e) {
		if(e.getClickCount() % 2 == 0) {
			try {
				
				int row = table.getSelectedRow();
	
				currTransaction = data.get(row);
				
				JFrame frame = new JFrame("Transaction History for #" + currTransaction.getTransactionID());
				frame.setSize(630, 450);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setLayout(null);
				
				CustomerTransactionTable table = new CustomerTransactionTable();
				table.updateTable(currTransaction.getArray());
				
				//Create a table here showing customer profile and details
				JLabel nameLabel, addressLabel, contactNumberLabel, amountLabel, typeLabel, dateOfTransactionLabel, transactionID;
	
				nameLabel = new JLabel("Name: " + currTransaction.getCustomer().getFirstName() + " " + currTransaction.getCustomer().getLastName());
				
				if(currTransaction.getCustomer().getUnit().isEmpty()) 
					addressLabel = new JLabel("Address: " + currTransaction.getCustomer().getStreet() + " S" + currTransaction.getCustomer().getPostal());
				else 
					addressLabel = new JLabel("Address: " + currTransaction.getCustomer().getStreet() + " #" + currTransaction.getCustomer().getUnit() + " S" + currTransaction.getCustomer().getPostal());
				
				
				contactNumberLabel = new JLabel("Contact No. : " + currTransaction.getCustomer().getContactNumber());
				typeLabel = new JLabel("Type: " + currTransaction.getType());
				dateOfTransactionLabel = new JLabel("Transaction Date: " + parseDateFormal(currTransaction.getTransactionDate()));
				amountLabel = new JLabel("Amount: " + NumberFormat.getCurrencyInstance().format(currTransaction.getAmount()));
				transactionID = new JLabel("ID: " + currTransaction.getTransactionID());
									
				nameLabel.setBounds		(35, 10, 300, 20);			dateOfTransactionLabel.setBounds	(330, 10, 300, 20);			
				addressLabel.setBounds	(35, 40, 300, 20);			contactNumberLabel.setBounds		(330, 40, 300, 20);			
				amountLabel.setBounds	(35, 70, 300, 20);
				
				table.setBounds(5, 150, 570, 240);
				
				frame.add(nameLabel);
				frame.add(addressLabel);

				frame.add(contactNumberLabel);
				frame.add(typeLabel);
				frame.add(dateOfTransactionLabel);
				frame.add(amountLabel);
				frame.add(transactionID);
				frame.add(table);
				
				frame.setVisible(true);
	
			} catch (ArrayIndexOutOfBoundsException e1) {}
		}
	}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}

	public void updateTable(List<TransactionHistory> d) {

		data = d;
		table.revalidate();
		table.repaint();
    
		System.out.println("Updated Transaction History Table");
	}
	
	public int getTableSize() { 
		return sizeOfTable;
	}
	
	//Edit table column width
	class ModelData extends AbstractTableModel {

		String colNames[] = { "Transaction ID", "Transaction Date", "Customer Name", "Type", "Amount"};
		Class<?> colClasses[] = { String.class, Date.class, String.class, String.class, double.class };

		// Constructor
		public ModelData() {
			data = new ArrayList<TransactionHistory>();
		}

		public int getRowCount() {
			int value = 0;
			try {
				 value = data.size();
			} catch (NullPointerException e) {
				return 0;
			}
			return value;
		}

		public int getColumnCount() {
			return colNames.length;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			
			switch(columnIndex) {
				case 0: return data.get(rowIndex).getTransactionID();
				case 1: return parseDateSimple(data.get(rowIndex).getTransactionDate());
				case 2: return data.get(rowIndex).getCustomer().getFirstName() + " " + data.get(rowIndex).getCustomer().getLastName();
				case 3: return data.get(rowIndex).getType();
				case 4: return NumberFormat.getCurrencyInstance().format(data.get(rowIndex).getAmount());
				default: return null;
			}
			
		}
		public String getColumnName(int columnIndex) {
			return colNames[columnIndex];
		}

		public Class<?> getColumnClass(int columnIndex) {
			return colClasses[columnIndex];
		}

	}
	
	public String parseDateSimple(Date d) {
		return new SimpleDateFormat("dd/MM/yyyy").format(d);
	}
	public String parseDateFormal(Date d) {
		return new SimpleDateFormat("dd/MM/yyyy hh:mm a").format(d);
	}

	class JComponentTableCellRenderer implements TableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			return (JComponent) value;
		}
	}
	
	
	
}
