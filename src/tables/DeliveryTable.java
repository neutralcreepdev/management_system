package tables;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import comparators.DeliverySortByCustomerName;
import comparators.DeliverySortByDeliveryDate;
import comparators.DeliverySortByEmployeeName;
import comparators.DeliverySortByStatus;
import resources.Delivery;

//http://www.java2s.com/Tutorial/Java/0240__Swing/CustomizingColumnHeaderswithIcons.htm

public class DeliveryTable extends JPanel implements MouseListener {

	JTable table;
	JScrollPane scrollPane;
	List<Delivery> data;
	List<Delivery> tempData;
	Delivery currDelivery;
	ModelData modelData;
	JPanel mainPanel;
	boolean eName = true, dDate = true, dStatus = true, cName = true;
	final JTableHeader header;

	DefaultTableCellRenderer middleRenderer = new DefaultTableCellRenderer();
	DefaultTableCellRenderer redRenderer = new DefaultTableCellRenderer();

	public DeliveryTable(JPanel mainPanel) {
		this.mainPanel = mainPanel;
		modelData = new ModelData();
		table = new JTable(modelData);
		currDelivery = new Delivery();

		scrollPane = new JScrollPane(table);
		table.setPreferredScrollableViewportSize(new Dimension(500, 420));
		table.setBackground(new Color(0xdaddd8));
		scrollPane.getViewport().setBackground(new Color(0xf2f5f6));
		
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		table.setVisible(true);
		table.addMouseListener(this);
		add(scrollPane);

		middleRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		redRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		redRenderer.setBackground(Color.red);

		table.getColumn("Customer Name").setCellRenderer( middleRenderer );
		table.getColumn("Delivery Date").setCellRenderer( middleRenderer );
		table.getColumn("Status").setCellRenderer( middleRenderer );
		table.getColumn("Employee Name").setCellRenderer( middleRenderer);
		
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

	// "Delivery Date", "Customer Name", "Status", "Employee Name"};
	public void sort(int column) {
		switch (column) {
		case 0 :
			Collections.sort(data, new DeliverySortByDeliveryDate()); 
			
			if (dDate == false)
				Collections.reverse(data);
		dDate = !dDate;

			
		break;
		case 1 : 
				Collections.sort(data, new DeliverySortByCustomerName());
				if (cName == false)
					Collections.reverse(data);
			cName = !cName;

		break;
		
		case 2 : 
			Collections.sort(data, new DeliverySortByStatus());
			if (dStatus) 
				Collections.reverse(data);
			dStatus = !dStatus; 

		break;
		
		case 3:
			Collections.sort(data, new DeliverySortByEmployeeName());
			if (eName == false)
				Collections.reverse(data);
			eName = !eName;

		break;

		}

		//Update the display
		table.revalidate();
		table.repaint();
		System.out.println("Sorted Delivery Table");

	}
	
	public void reset() {
		updateTableDelivery(tempData);
	}
	//When click on 1 filter, just extract the values matching the filter conditions
	public void filter(String condition) {

			data = tempData;
			List<Delivery> temp = new ArrayList<>();

			Collections.sort(data, new DeliverySortByStatus());
				
			for ( Delivery d : data) {
				if(d.getDeliveryStatus().equalsIgnoreCase(condition) == true)
					temp.add(d);
			}
			
			if (temp.size() < 1)
				JOptionPane.showMessageDialog(mainPanel, "No results found!", "Filter Results", 2, null);
			else {
				
				data = temp;
				table.revalidate();
				table.repaint();
		    	System.out.println("Filtered DeliveryTable");
			}
		}
		
	public Delivery getSelectedDelivery() {
		return currDelivery;
	}

	public void mousePressed(MouseEvent e) {
		if(e.getClickCount() % 2 == 0) {
			try {
				//Might need to add a boolean to determine if the delivery information is enough
				// E.g. Pending unSelected deliveries, they dont have certain information like staff names etc
				currDelivery = data.get(table.getSelectedRow());
				
				//This method shall print the details of the delivery set
				JFrame frame = new JFrame("Delivery Information #" + currDelivery.getTransactionID());
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setSize(630, 450);
				frame.setLayout(null);
				
				JLabel customerName, address, status, expectedTime, actualTime, transactionDate, amountSpent;
				JLabel employeeName; 
				
				customerName = new JLabel("Customer Name: " + currDelivery.getCustomerName());
				address = new JLabel("Address: " + currDelivery.getAddress());
				status = new JLabel("Status: " + currDelivery.getDeliveryStatus());
				expectedTime = new JLabel("Time Expected: " + currDelivery.getExpectedDate() + " " + parseTimeString(currDelivery.getExpectedTime()));
				actualTime = new JLabel("Time Arrived: " + currDelivery.getActualDate() + " " + parseTimeString(currDelivery.getActualTime()));
				transactionDate = new JLabel("Transaction Date: " + parseDateFormal(currDelivery.getTransactionDate()));
				amountSpent = new JLabel("Amount: " + NumberFormat.getCurrencyInstance().format(currDelivery.getAmount()));
				employeeName = new JLabel("Employee Name: " + currDelivery.getEmployeeName());
				
				 
				CustomerTransactionTable thTable = new CustomerTransactionTable();
				thTable.updateTable(currDelivery.getItemList());
				
				thTable.setBounds(20, 150, 570, 240);
				
				customerName.setBounds		(20, 10, 250, 20);		transactionDate.setBounds	(380, 10, 250, 20);	 		
				address.setBounds			(20, 40, 250, 20);		status.setBounds			(380, 40, 250, 20);		
				expectedTime.setBounds		(20, 70, 250, 20); 		actualTime.setBounds		(380, 70, 250, 20);
				employeeName.setBounds		(20, 100, 250, 20);		amountSpent.setBounds		(380, 100, 250, 20);					
				
				frame.add(customerName);
				frame.add(address);
				frame.add(status);
				frame.add(expectedTime);
				frame.add(actualTime);
				frame.add(transactionDate);
				frame.add(amountSpent);
				frame.add(employeeName);
				frame.add(thTable);
	
				frame.setVisible(true);
				
			
			} catch (ArrayIndexOutOfBoundsException e1) {}
		}
	}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}

	public void updateTableDelivery(List<Delivery> d) {

		data = d;
		tempData = data;

		table.revalidate();
		table.repaint();
    
		System.out.println("Updated Delivery Table");
	}
	
	//Edit table column width
	class ModelData extends AbstractTableModel {


		String colNames[] = {  "Delivery Date", "Customer Name", "Status", "Employee Name"};
		Class<?> colClasses[] = { String.class, String.class, String.class, String.class };

		// Constructor
		public ModelData() {
			data = new ArrayList<Delivery>();
			tempData = new ArrayList<Delivery>();
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
				case 0: return data.get(rowIndex).getExpectedDate();
				case 1: return data.get(rowIndex).getCustomerName(); 
				case 2: 
					
					/*if( data.get(rowIndex).getDeliveryStatus().contains("Late")) {
						table.getColumn("Status").setCellRenderer(redRenderer);
						return data.get(rowIndex).getDeliveryStatus();
					} else 
						table.getColumn("Status").setCellRenderer(middleRenderer);*/
						return data.get(rowIndex).getDeliveryStatus();
				case 3: return data.get(rowIndex).getEmployeeName();
					//Leave until Monday 04112019 to settle the testing and integration with the other apps
				default: return null;
			}
			
		}

		public String getColumnName(int columnIndex) {
			return colNames[columnIndex];
		}

		public Class<?> getColumnClass(int columnIndex) {
			return colClasses[columnIndex];
		}

		/*
		 * This is for editing the data public 
		 * 
		 * boolean isCellEditable(int rowIndex, int columnIndex) { 
		 * return false; 
		 * }
		 * 
		 * public void setValueAt(Object aValue, int rowIndex, int columnIndex) { 
		   if (columnIndex == 0)
		  	data.get(rowIndex).setGene((String) aValue); 
		   if (columnIndex == 1)
		 	data.get(rowIndex).setChromosome((String) aValue); 
		   if (columnIndex == 2)
		  	data.get(rowIndex).setCoordinate((String) aValue); 
		  if (columnIndex == 3)
		  	data.get(rowIndex).setCounter((int) aValue);
		  
		  	fireTableCellUpdated(rowIndex, columnIndex); 
		  }
		 */
	}
	
	public String parseDateSimple(Date d) {
		return new SimpleDateFormat("dd/MM/yyyy").format(d);
	}
	public String parseDateFormal(Date d) {
		return new SimpleDateFormat("dd/MM/yyyy hh:mm a").format(d);
	}
	public String parseTimeString(String d) {
		try {
			Date temp = new SimpleDateFormat("hh:mm").parse(d);
			return new SimpleDateFormat("hh:mm a").format(temp);
		} catch (ParseException | NullPointerException e) {
			return "<Waiting for Pickup>";
		}
	}

	class JComponentTableCellRenderer implements TableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			return (JComponent) value;
		}
	}
}
