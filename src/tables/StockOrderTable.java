package tables;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.text.NumberFormat;
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

import resources.Grocery;
import resources.JRoundedTextField;

//http://www.java2s.com/Tutorial/Java/0240__Swing/CustomizingColumnHeaderswithIcons.htm

public class StockOrderTable extends JPanel {
	JTable table;
	JScrollPane scrollPane;
    List<Grocery> data;
    Grocery currGrocery;
    ModelData modelData;
    final JTableHeader header;
    boolean gID = true, gName = true, gSupplier = true, gQty = true, gPrice = true, gDesc = true;
    JRoundedTextField name, supplier, itemID, qty, price, description; 
    
	public StockOrderTable(){
		modelData = new ModelData();
		table = new JTable(modelData);
		currGrocery = new Grocery();

		scrollPane = new JScrollPane(table);
		table.setPreferredScrollableViewportSize(new Dimension(800, 380));
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		table.setOpaque(false);
		table.setVisible(true);
		add(scrollPane);
		
		DefaultTableCellRenderer middleColorRenderer = new DefaultTableCellRenderer();
		DefaultTableCellRenderer middleRenderer = new DefaultTableCellRenderer();
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();

		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		middleColorRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		middleRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		middleColorRenderer.setBackground(new Color(0xf5f5f5));

		table.setBackground(new Color(0xdaddd8));
		scrollPane.getViewport().setBackground(new Color(0xf2f5f6));
		
		table.getColumn("Item ID").setCellRenderer( leftRenderer );
		table.getColumn("Name").setCellRenderer( leftRenderer );
		table.getColumn("Current Qty").setCellRenderer(middleRenderer);
		table.getColumn("Ordering Qty").setCellRenderer( middleColorRenderer );
		table.getColumn("Price").setCellRenderer( middleRenderer);
		table.getColumn("Supplier Name").setCellRenderer( leftRenderer );
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
        header = table.getTableHeader();
        
        //Doesn't allow swapping of columns
        header.setReorderingAllowed(false);
	}
	
	public Grocery getSelectedGrocery() { 
		return currGrocery; 
	}  
	
	public List<Grocery> getOrderList() {
		return data;
	}

	public void updateOrderListGrocery(ArrayList<Grocery> g) {
		data = g;
		table.revalidate();
		table.repaint();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    	System.out.println("Update Stock Order Table");
	}
	
	public void checkAll() {
		for(Grocery g: data) {
			g.setReplenishmentStatus(true);
		}
		table.revalidate();
		table.repaint();
	}
	
	public void uncheckAll() {
		for(Grocery g: data) {
			g.setReplenishmentStatus(false);
		}
		table.revalidate();
		table.repaint();
	}
	
	//Edit table column width
	class ModelData extends AbstractTableModel {

		String colNames[] = { "Item ID", "Name", "Supplier Name", "Price", "Current Qty", "Ordering Qty", "Selected for Order"};
	    Class<?> colClasses[] = { String.class, String.class, String.class, Double.class, Long.class , Long.class, Boolean.class };


	    //Constructor
	    public ModelData() {
	    	data = new ArrayList<Grocery>();
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
	    	case 0: return data.get(rowIndex).getItemID();
	    	case 1: return data.get(rowIndex).getName();
	    	case 2: return data.get(rowIndex).getSupplier();
	    	case 3: return NumberFormat.getCurrencyInstance().format(data.get(rowIndex).getPrice());
	    	case 4: return data.get(rowIndex).getQuantity();
	    	case 5: return data.get(rowIndex).getOrderingQty();
	    	case 6: return data.get(rowIndex).getReplenishmentStatus();
	    	default:   return null;
	        }
	      
	    }

	    public String getColumnName(int columnIndex) {
	        return colNames[columnIndex];
	    }

	    public Class<?> getColumnClass(int columnIndex) {
	        return colClasses[columnIndex];
	    }
	    
	    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		    if (columnIndex == 5) {
		    	data.get(rowIndex).setOrderingQty((Long) aValue);
		    	fireTableCellUpdated(rowIndex, columnIndex);
		    } else if ( columnIndex == 6) {
		    	data.get(rowIndex).setReplenishmentStatus(!data.get(rowIndex).getReplenishmentStatus());
		    	fireTableCellUpdated(rowIndex, columnIndex);
		    }
	    }
	    public boolean isCellEditable(int rowIndex, int columnIndex){
	    	if(columnIndex == 5 || columnIndex == 6)
	    		return true;
	    	else
	    		return false;
	    }
	 	    
	}
	
	class JComponentTableCellRenderer implements TableCellRenderer {
		  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		      boolean hasFocus, int row, int column) {
		    return (JComponent) value;
		  }
		}
}
