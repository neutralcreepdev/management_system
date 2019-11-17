package panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;


//http://www.java2s.com/Tutorial/Java/0240__Swing/CustomizingColumnHeaderswithIcons.htm

//Need to add a searchbar
public class TransactionHistoryTable extends JPanel implements MouseListener {

	JTable table;
	JScrollPane scrollPane;
	List<Transaction> data;
	List<Transaction> tempData;
	Transaction currTransaction;
	ModelData modelData;
	boolean dID = true, dDate = true, dStatus = true, dEID = true, dCID = true;
	final JTableHeader header;
	TaskBar tb;

	public TransactionHistoryTable(TaskBar t) {
		tb = t;
		modelData = new ModelData();
		table = new JTable(modelData);
		currTransaction = new Transaction();

		scrollPane = new JScrollPane(table);
		table.setPreferredScrollableViewportSize(new Dimension(500, 400));

		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		table.setVisible(true);
		table.addMouseListener(this);
		add(scrollPane);

		
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

		table.getColumn("Transaction ID").setCellRenderer( leftRenderer );
		table.getColumn("Transaction Date").setCellRenderer( leftRenderer );
		table.getColumn("Employee ID").setCellRenderer( leftRenderer);
		table.getColumn("Customer ID").setCellRenderer( leftRenderer );
		
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

	
	public void sort(int column) {
		switch (column) {
		case 0 :
			Collections.sort(data, new DeliverySortByOrderNumber());
			
			if (dID == false) 
				Collections.reverse(data);
			
			dID = !dID;
			
		break;
		case 1 : 
			Collections.sort(data, new DeliverySortByDate()); 
			
				if (dDate == false)
					Collections.reverse(data);
			dDate = !dDate;
		break;
		
		case 2 : 
			Collections.sort(data, new DeliverySortByStatus());
			if (dStatus) 
				Collections.reverse(data);
			dStatus = !dStatus;
		break;
		
		case 3:
			Collections.sort(data, new DeliverySortByEmployeeID());
			if(dEID)
				Collections.reverse(data);
			dEID = !dEID;
		break;
		case 4: 
			Collections.sort(data, new DeliverySortByCustomerID());
			if(dCID)
				Collections.reverse(data);
			dCID = !dCID;
		break;
		}

		//Update the display
		table.revalidate();
		table.repaint();
		System.out.println("Sorted Delivery Table");

	}

	//When click on 1 filter, just extract the values matching the filter conditions
	public void filter(String condition) {

			List<Transaction> temp = new ArrayList<>();
			condition = condition.toLowerCase();
			System.out.println("Condition recieved -> " + condition);
			Collections.sort(data, new TransactionSortByStatus());
				
			for ( Transaction d : data) {
				System.out.println("Condition of -> " + d.getDeliveryStatus());
				if(d.getDeliveryStatus().toLowerCase().equals(condition) == true)
					temp.add(d);
			}
			
			if (temp.size() < 1)
				JOptionPane.showMessageDialog(null, "No results found!", null, 2, null);
			else {
				
				data = temp;
				table.revalidate();
				table.repaint();
		    	System.out.println("Filtered Table");
			}
		}
		
	public Transaction getSelectedDelivery() {
		return currTransaction;
	}

	public void mousePressed(MouseEvent e) {
		

		try {
			int row = table.getSelectedRow();

			currTransaction = data.get(row);
			System.out.println(currTransaction.toString());

			// System.out.println("Row " + row + " Column " + column);
		} catch (ArrayIndexOutOfBoundsException e1) {

		}
	}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}

	public void updateTableDelivery(List<Transaction> d) {

		data = d;
		table.revalidate();
		table.repaint();
		
	    //table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	    
		System.out.println("Updated Delivery Table");
	}
	
	//Edit table column width
	class ModelData extends AbstractTableModel {

		String colNames[] = { "Transaction ID", "Transaction Date", "Employee ID", "Customer ID"};
		Class<?> colClasses[] = { String.class, Date.class, String.class, String.class };

		// Constructor
		public ModelData() {
			data = new ArrayList<Transaction>();
		}

		public int getRowCount() {
			return data.size();
		}

		public int getColumnCount() {
			return colNames.length;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			
			switch(columnIndex) {
				/*case 0: return data.get(rowIndex).getDeliveryID();
				case 1: return data.get(rowIndex).getDeliveryDate(); 
				case 2: return data.get(rowIndex).getDeliveryStatus();
				case 3: return data.get(rowIndex).getEmployeeID();*/
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

	class JComponentTableCellRenderer implements TableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			return (JComponent) value;
		}
	}
}
