package panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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

import comparators.InventorySortByItemID;
import comparators.InventorySortByName;
import comparators.InventorySortByQuantity;
import comparators.InventorySortBySupplier;
import resources.Grocery;
import resources.JRoundedTextField;

//http://www.java2s.com/Tutorial/Java/0240__Swing/CustomizingColumnHeaderswithIcons.htm

public class SearchTable extends JPanel implements MouseListener {
	JTable table;
	JScrollPane scrollPane;
    List<Grocery> data;
    List<Grocery> tempData;
    Grocery currGrocery;
    ModelData modelData;
    SearchPanel sp;
    final JTableHeader header;
    boolean gID = true, gName = true, gSupplier = true, gQty = true;
    JRoundedTextField name, supplier, itemID, qty, price; 
    
	public SearchTable(SearchPanel s ){
		sp = s;
		modelData = new ModelData();
		table = new JTable(modelData);
		currGrocery = new Grocery();

		scrollPane = new JScrollPane(table);
		table.setPreferredScrollableViewportSize(new Dimension(500, 400));
		
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		table.setVisible(true);
		table.addMouseListener(this);
		add(scrollPane);
		
		
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

		table.getColumn("Item ID").setCellRenderer( leftRenderer );
		table.getColumn("Name").setCellRenderer( leftRenderer );
		table.getColumn("Quantity").setCellRenderer( leftRenderer );
		table.getColumn("Price").setCellRenderer( leftRenderer);
		table.getColumn("Supplier Name").setCellRenderer( leftRenderer );
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
        header = table.getTableHeader();
        
        //Doesn't allow swapping of columns
        header.setReorderingAllowed(false);
        
        //Adds a mouse listener
        header.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int column = header.columnAtPoint(e.getPoint());
                sort(column);
                }
        }); // End of header's mouseListener
	}
	
	public Grocery getSelectedGrocery() { 
		return currGrocery; 
	}  
	
	public void sort(int column) {
		switch (column) {
		case 0 :
			Collections.sort(data, new InventorySortByItemID());
			
			if (gID == false) 
				Collections.reverse(data);
			
			gID = !gID;
			
		break;
		case 1 : 
			Collections.sort(data, new InventorySortByName()); 
			
				if (gName == false)
					Collections.reverse(data);
			gName = !gName;
		break;
		
		case 2: 
			Collections.sort(data, new InventorySortByQuantity());
			if (gQty) 
				Collections.reverse(data);
			gQty = !gQty;
		break;
		
		
		case 4 : 
			Collections.sort(data, new InventorySortBySupplier());
			if (gSupplier) 
				Collections.reverse(data);
			gSupplier = !gSupplier;
		break;
		
		default: ;
		}

		//Update the display
		table.revalidate();
		table.repaint();
		System.out.println("Sorted Search Table");

	}
	
	//When click on 1 filter, just extract the values matching the filter conditions
	public void filter(String condition, String value) {

		data = tempData;
		List<Grocery> temp = new ArrayList<>();
		condition = condition.toLowerCase();
		value = value.toLowerCase();
		
		switch(condition) {
			case "":
				for(Grocery g: data) {
					if ( g.getName().toLowerCase().contains(value) || g.getSupplier().toLowerCase().contains(value))
						temp.add(g);	
					else {
						try {
							if ( g.getQuantity() == Integer.parseInt(value) )
								temp.add(g);
						} catch (NumberFormatException e) {}
					}
				}
				break;
			case "name":
				for(Grocery g: data) {
					if ( g.getName().toLowerCase().contains(value)) 
						temp.add(g);			
				}
				break;
			case "supplier":
				for(Grocery g: data) {
					if ( g.getSupplier().toLowerCase().contains(value))
						temp.add(g);
				}
				break;
			case "quantity":
				for(Grocery g: data) {
					try {
						if ( g.getQuantity() == Long.parseLong(value) )
							temp.add(g);
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Enter a number!", null, 2, null);
						break;
					}
				}
				break;
		}
		
		if (temp.size() < 1)
			JOptionPane.showMessageDialog(null, "No results found! " + value, null, 2, null);
		else {
			
			data = temp;
			table.revalidate();
			table.repaint();
	    	System.out.println("Filtered Table");
		}
	}
	public void mousePressed(MouseEvent e) {
/*		if (e.getButton() == MouseEvent.BUTTON3)
			System.out.println("hello");*/

		try {
			int row = table.getSelectedRow();		
			currGrocery = data.get(row);
			
			//Here should add a function to allow the user to edit and send the new update to the server
			//Since java no proper update mechanism, can do a read your writes method, where program will query for the same item, then +/- the difference from old data
			// Current GUI: 100
			// Database : 100
			// User edit qty to 120,
			// 10 Stock got sold at the same time.
			// GUI will calculate the difference between the Current GUI and the new Updated Value = 20
			// When writing to DB, GUI will query for the latest, getLatest() = 90, then GUI will send do the maths with the latest field and update.
			// Finally the db will get 110
			
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setSize(400,500);
			frame.setName("Update Stock");
			frame.setLayout(new GridLayout(0, 1, 50, 20));
			
			JLabel nameLabel, supplierLabel, itemIDLabel, quantityLabel, priceLabel;
	
			JButton submit = new JButton("Update");
			JButton delete = new JButton("Delete");
			nameLabel = new JLabel("Name: " + currGrocery.getName());
			supplierLabel = new JLabel("Supplier Name: " + currGrocery.getSupplier());
			itemIDLabel = new JLabel("ItemID: " + currGrocery.getItemID());
			quantityLabel = new JLabel("Quantity: " + currGrocery.getQuantity());
			priceLabel = new JLabel("Price(SGD): " + currGrocery.getPrice());
			name = new JRoundedTextField(1);
			supplier = new JRoundedTextField(1);
			itemID = new JRoundedTextField(1);
			qty = new JRoundedTextField(1);
			price = new JRoundedTextField(1);
			
			submit.addActionListener(new submit_AL(frame));
			delete.addActionListener(new delete_AL(frame));
			
			frame.add(nameLabel);
			frame.add(name);
			frame.add(supplierLabel);
			frame.add(supplier);
			frame.add(itemIDLabel);
			frame.add(itemID);
			frame.add(quantityLabel);
			frame.add(qty);
			frame.add(priceLabel);
			frame.add(price);
			frame.add(submit);
			frame.add(delete);
			
			frame.setAlwaysOnTop(true);	
			frame.setVisible(true);

			} catch ( ArrayIndexOutOfBoundsException e1) {
					
		}
			
	}	
	class submit_AL implements ActionListener {
		JFrame frame;
			
		public submit_AL(JFrame temp) {
			frame = temp;
		}
			
		public void actionPerformed(ActionEvent e) {

			Grocery temp = new Grocery (name.getText(),	supplier.getText(),	Long.parseLong(itemID.getText()), Long.parseLong(qty.getText()), Double.parseDouble(price.getText()));				
			//temp = compare(currGrocery, temp);

			frame.dispose();
			//Function to compare the difference between the objects
				
			// Update DB
			//sp.updateStock(g, g);
			//Subsequently refresh GroceryPanel
			//Retrieve Latest Database Connection list
			//Grocery latestG = sp.getData();	
			//Subsequently refresh DeliveryPanel and GroceryPanel
			//sp.updateTable(driver.getGrocery());
			//frame.revalidate();	
		}
	}
			
	class delete_AL implements ActionListener {
		JFrame frame;
				
		public delete_AL(JFrame temp) {
				frame = temp;
		}
				
		public void actionPerformed(ActionEvent e) {
					frame.dispose();
					//Send a Delete Request To Firestore

					//Subsequently refresh GroceryPanel
					//Retrieve Latest Database Connection list
					
					//sp.updateTable(driver.getGrocery());
					//frame.revalidate();
					
		}
			
	}		
	
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e)  {}
	public void mouseExited(MouseEvent e)   {}
	public void mouseClicked(MouseEvent e)  {}
	
	//Update this
/*	public Grocery compare(Grocery curr, Grocery input) {
		Grocery updated = new Grocery();
		
		if(curr.getName().equalsIgnoreCase(input.getName().trim()) == false && input.getName().trim() != "")
			updated.setName(input.getName().trim());
		if(curr.getSupplier().equalsIgnoreCase(input.getSupplier().trim()) == false && input.getSupplier().trim() != "")
			updated.setSupplier(input.getSupplier().trim());
		if(curr.getItemID() == input.getItemID() == false)
		
		
		return updated;
	}*/
	
	public void updateTableGrocery(ArrayList<Grocery> g) {
		tempData = g;
		data = g;
		table.revalidate();
		table.repaint();
    	
    	System.out.println("Update Table");
	}
	
	//Edit table column width
	class ModelData extends AbstractTableModel {

		String colNames[] = { "Item ID", "Name", "Quantity", "Price", "Supplier Name"};
	    Class<?> colClasses[] = { Long.class, String.class, Long.class, Double.class, String.class  };

	    //Constructor
	    public ModelData() {
	    	data = new ArrayList<Grocery>();
	    	tempData = new ArrayList<Grocery>();
	    }

	    public int getRowCount() {
	        return data.size();
	    }

	    public int getColumnCount() {
	        return colNames.length;
	    }

	    public Object getValueAt(int rowIndex, int columnIndex) {
	        
	    	switch(columnIndex) {
	    	case 0: return data.get(rowIndex).getItemID();
	    	case 1: return data.get(rowIndex).getName();
	    	case 2: return data.get(rowIndex).getQuantity();
	    	case 3: return data.get(rowIndex).getPrice();
	    	case 4: return data.get(rowIndex).getSupplier();
	    	
	    	default:   return null;
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
		  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		      boolean hasFocus, int row, int column) {
		    return (JComponent) value;
		  }
		}
}
