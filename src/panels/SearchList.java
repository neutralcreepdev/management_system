package panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import resources.Delivery;
import resources.Grocery;

//http://www.java2s.com/Tutorial/Java/0240__Swing/CustomizingColumnHeaderswithIcons.htm

public class SearchList extends JPanel implements MouseListener {
	JTable table;
	JScrollPane scrollPane;
    List<Grocery> data;
    List<Delivery> dataDelivery;
    Grocery currGrocery;
    ModelData modelData;
    ModelDelivery modelDelivery;
    final JTableHeader header;
    
	public SearchList(boolean check) {
		ArrayList<Grocery> temp;
		if(check  == true) {
			modelData = new ModelData();
			table = new JTable(modelData);
			currGrocery = new Grocery();
		}
		else {
			modelDelivery = new ModelDelivery();
			table = new JTable(modelDelivery);
		}
		
	
		scrollPane = new JScrollPane(table);
		table.setPreferredScrollableViewportSize(new Dimension(500, 400));
		
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		table.setVisible(true);
		table.addMouseListener(this);
		add(scrollPane);
		
        header = table.getTableHeader();
        
        //Doesn't allow swapping of columns
        header.setReorderingAllowed(false);
        
        //Adds a mouse listener
        header.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int column = header.columnAtPoint(e.getPoint());
                System.out.println("Sorting column " + column);
                }
        }); // End of header's mouseListener
	}
	
	public Grocery getSelectedGrocery() { 
		return currGrocery; 
	}  
	
	public void mousePressed(MouseEvent e) {
		//int column = -1;
		try {
		int row = table.getSelectedRow();
		
		currGrocery = data.get(row);
		System.out.println(currGrocery.toString());
		
		//System.out.println("Row " + row + " Column " + column);
		} catch ( ArrayIndexOutOfBoundsException e1) {
			
		}
	  
	}
	    
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e)  {}
	public void mouseExited(MouseEvent e)   {}
	public void mouseClicked(MouseEvent e)  {}
	
	
	
	public void updateTableGrocery(ArrayList<Grocery> g) {
		
		data = g;
		table.revalidate();
		table.repaint();
    	table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    	
    	System.out.println("Update Table");
	}
	
	public void updateTableDelivery(ArrayList<Delivery> d) {
		
		dataDelivery = d;
		table.revalidate();
		table.repaint();
    	table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    	
    	System.out.println("Update Table");
	}
	
	//Edit table column width
	class ModelData extends AbstractTableModel {

		String colNames[] = { "INSERT ROWS OF DATA HERE" };
	    Class<?> colClasses[] = { String.class };

	    //Constructor
	    public ModelData() {
	    	data = new ArrayList<Grocery>();
	    }

	    public int getRowCount() {
	        return data.size();
	    }

	    public int getColumnCount() {
	        return colNames.length;
	    }

	    public Object getValueAt(int rowIndex, int columnIndex) {
	        if (columnIndex == 0) {
	            return data.get(rowIndex).toString();
	        }
	        return null;
	    }

	    public String getColumnName(int columnIndex) {
	        return colNames[columnIndex];
	    }

	    public Class<?> getColumnClass(int columnIndex) {
	        return colClasses[columnIndex];
	    }

	    /* This is for editing the data
	    public boolean isCellEditable(int rowIndex, int columnIndex) {
	        return false;
	    }

	    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	        if (columnIndex == 0) {
	            data.get(rowIndex).setGene((String) aValue);
	        }
	        if (columnIndex == 1) {
	            data.get(rowIndex).setChromosome((String) aValue);
	        }
	        if (columnIndex == 2) {
	            data.get(rowIndex).setCoordinate((String) aValue);
	        }
	        if (columnIndex == 3) {
	        	data.get(rowIndex).setCounter((int) aValue);
	        }
	        fireTableCellUpdated(rowIndex, columnIndex);
	    }
	     */
	}
	
	class ModelDelivery extends AbstractTableModel {

		String colNames[] = { "INSERT ROWS OF DATA HERE" };
	    Class<?> colClasses[] = { String.class };

	    //Constructor
	    public ModelDelivery() {
	    	dataDelivery = new ArrayList<Delivery>();
	    }

	    public int getRowCount() {
	        return dataDelivery.size();
	    }

	    public int getColumnCount() {
	        return colNames.length;
	    }

	    public Object getValueAt(int rowIndex, int columnIndex) {
	        if (columnIndex == 0) {
	            return dataDelivery.get(rowIndex).toString();
	        }
	        return null;
	    }

	    public String getColumnName(int columnIndex) {
	        return colNames[columnIndex];
	    }

	    public Class<?> getColumnClass(int columnIndex) {
	        return colClasses[columnIndex];
	    }

	    /* This is for editing the data
	    public boolean isCellEditable(int rowIndex, int columnIndex) {
	        return false;
	    }

	    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	        if (columnIndex == 0) {
	            data.get(rowIndex).setGene((String) aValue);
	        }
	        if (columnIndex == 1) {
	            data.get(rowIndex).setChromosome((String) aValue);
	        }
	        if (columnIndex == 2) {
	            data.get(rowIndex).setCoordinate((String) aValue);
	        }
	        if (columnIndex == 3) {
	        	data.get(rowIndex).setCounter((int) aValue);
	        }
	        fireTableCellUpdated(rowIndex, columnIndex);
	    }
	     */
	}
	
	class JComponentTableCellRenderer implements TableCellRenderer {
		  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		      boolean hasFocus, int row, int column) {
		    return (JComponent) value;
		  }
		}
}
