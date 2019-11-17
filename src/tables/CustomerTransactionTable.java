package tables;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import resources.TransactionHistory;


//http://www.java2s.com/Tutorial/Java/0240__Swing/CustomizingColumnHeaderswithIcons.htm

//Need to add a searchbar
public class CustomerTransactionTable extends JPanel {

	JTable table;
	JScrollPane scrollPane;
	List<Object> data;
	TransactionHistory currTransaction;
	ModelData modelData;
	final JTableHeader header;


	public CustomerTransactionTable() {
		modelData = new ModelData();
		table = new JTable(modelData);
		currTransaction = new TransactionHistory();

		scrollPane = new JScrollPane(table);
		table.setPreferredScrollableViewportSize(new Dimension(540, 200));

		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		table.setVisible(true);
		add(scrollPane);

		
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

		DefaultTableCellRenderer middleRenderer = new DefaultTableCellRenderer();

		middleRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		table.getColumn("Item ID").setCellRenderer( middleRenderer );
		table.getColumn("Item Name").setCellRenderer( leftRenderer );
		table.getColumn("Quantity").setCellRenderer( middleRenderer );
		table.getColumn("Price").setCellRenderer( middleRenderer );
		
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	    
		header = table.getTableHeader();

		// Doesn't allow swapping of columns
		header.setReorderingAllowed(false);
	}
	public TransactionHistory getSelectedTransaction() {
		return currTransaction;
	}
	
	public void updateTable(List<Object> d) {

		data = d;
		table.revalidate();
		table.repaint();

		System.out.println("Updated Customer History Table");
	}
	
	//Edit table column width
	class ModelData extends AbstractTableModel {

		String colNames[] = { "Item ID", "Item Name", "Quantity", "Price"};
		Class<?> colClasses[] = { String.class, String.class, Long.class };

		// Constructor
		public ModelData() {
			data = new ArrayList<Object>();
		}

		@SuppressWarnings("unchecked")
		public int getRowCount() {
			int value = 0;
			try {
				 value =  ((ArrayList<Object>) data.get(0)).size();
			} catch (Exception e) {
				return 0;
			}
			return value;
		}

		public int getColumnCount() {
			return colNames.length;
		}
		@SuppressWarnings("unchecked")
		public Object getValueAt(int rowIndex, int columnIndex) {
			
			try {

				ArrayList<Object> temp =  (ArrayList<Object>) data.get(0);
				Map<String, Object> t = (Map<String, Object>) temp.get(rowIndex);
				
				switch(columnIndex) {
					case 0: return t.get("id");
					case 1: return t.get("name");
					case 2: return t.get("quantity");
					case 3: return NumberFormat.getCurrencyInstance().format(t.get("cost"));

					default: return "";
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "";
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
