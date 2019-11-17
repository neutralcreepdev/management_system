package tables;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

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

import resources.JRoundedTextField;

public class ViewReplenishmentTable extends JPanel {

		JTable table;
		JScrollPane scrollPane;
	    List<String> data;

	    ModelData modelData;
	    final JTableHeader header;
	    JRoundedTextField name, supplier, itemID, qty, price, description; 
	    boolean iID = false, dID = false;
	    
		public ViewReplenishmentTable() {
			modelData = new ModelData();
			table = new JTable(modelData);

			scrollPane = new JScrollPane(table);
			table.setPreferredScrollableViewportSize(new Dimension(522, 420));
			
			scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			table.setOpaque(false);
			table.setVisible(true);
			add(scrollPane);
			
			
			DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
			leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

			table.setBackground(new Color(0xdaddd8));
			scrollPane.getViewport().setBackground(new Color(0xf2f5f6));
			
			table.getColumn("Name").setCellRenderer( leftRenderer );
			table.getColumn("Quantity").setCellRenderer( leftRenderer );
			table.getColumn("Price").setCellRenderer(leftRenderer);
			table.getColumn("Supplier").setCellRenderer(leftRenderer);
			
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			
	        header = table.getTableHeader();
	        
	        //Doesn't allow swapping of columns
	        header.setReorderingAllowed(false);
	        
		}
		
		public void updateTable(ArrayList<String> g) {
			data = g;
			table.revalidate();
			table.repaint();
	    	
	    	System.out.println("Update ViewReplenishmentTable");
		}
		
		//Edit table column width
		class ModelData extends AbstractTableModel {

			String colNames[] = {"Name", "Quantity", "Price", "Supplier"};
		    Class<?> colClasses[] = { String.class, Long.class, Long.class, String.class };

		    //Constructor
		    public ModelData() {
		    	data = new ArrayList<String>();
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

		    //Yet to test with more 
		    public Object getValueAt(int rowIndex, int columnIndex) {

		    	for (int i = 0; i < data.size(); i++) {
		    		String[] temp = data.get(i).split("\\|");
		    	
		    		if (i == rowIndex)
		    			return temp[columnIndex];
		    	}
		    	
		      return null;
		    }

		    public String getColumnName(int columnIndex) {
		        return colNames[columnIndex];
		    }

		    public Class<?> getColumnClass(int columnIndex) {
		        return colClasses[columnIndex];
		    }
		}

		class JComponentTableCellRenderer implements TableCellRenderer {
			  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			      boolean hasFocus, int row, int column) {
			    return (JComponent) value;
			  }
			}

}

