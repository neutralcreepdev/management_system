package tables;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

import comparators.InvoiceSortByDate;
import comparators.InvoiceSortByID;
import main.InteractionView.ImportStock_AL;
import resources.Invoice;
import resources.JRoundedTextField;

public class ImportReplenishmentTable extends JPanel implements MouseListener {

		JTable table;
		JScrollPane scrollPane;
	    List<Invoice> data;
	    Invoice currInvoice;
	    ModelData modelData;
	    final JTableHeader header;
	    JRoundedTextField name, supplier, itemID, qty, price, description; 
	    boolean iID = false, dID = false;
	    ImportStock_AL importStock;
	    
		public ImportReplenishmentTable(ImportStock_AL a) {
			importStock = a;
			modelData = new ModelData();
			table = new JTable(modelData);
			currInvoice = new Invoice();

			scrollPane = new JScrollPane(table);
			table.setPreferredScrollableViewportSize(new Dimension(522, 420));
			scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			table.setOpaque(false);
			table.setVisible(true);
			table.addMouseListener(this);
			add(scrollPane);
			
			
			DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
			leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

			table.setBackground(new Color(0xdaddd8));
			scrollPane.getViewport().setBackground(new Color(0xf2f5f6));
			
			table.getColumn("Date").setCellRenderer( leftRenderer );
			table.getColumn("Invoice No").setCellRenderer( leftRenderer );
			table.getColumn("Status").setCellRenderer(leftRenderer);
			
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			
	        header = table.getTableHeader();
	        
	        //Doesn't allow swapping of columns
	        header.setReorderingAllowed(false);
	        
	        //Adds a mouse listener
	        header.addMouseListener(new MouseAdapter() {
	            public void mouseClicked(MouseEvent e) {
	                sort(header.columnAtPoint(e.getPoint()));
	            }
	        }); // End of header's mouseListener
		}
				
		public Invoice getSelectedInvoice() { 
			return currInvoice; 
		}  
		
		public void sort(int column) {
			switch (column) {

			case 0 : 
				Collections.sort(data, new InvoiceSortByDate()); 
				
					if (iID == false)
						Collections.reverse(data);
					iID = !iID;
			break;
			case 1 :
				Collections.sort(data, new InvoiceSortByID());
				
				if (dID == false) 
					Collections.reverse(data);
				
				dID = !dID;
				
			break;
			}
		

			//Update the display
			table.revalidate();
			table.repaint();
			System.out.println("Sorted Replenishement Table");

		}
		
		public void mousePressed(MouseEvent e) {			
			int row = table.getSelectedRow();		
			currInvoice = data.get(row);
			importStock.setButtonState();
		}	
		
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e)  {}
		public void mouseExited(MouseEvent e)   {}
		public void mouseClicked(MouseEvent e)  {}
			
		public void updateTableInvoice(ArrayList<Invoice> g) {
			data = g;
			table.revalidate();
			table.repaint();
	    	
	    	System.out.println("Update Replenishment Table");
		}
		
		//Edit table column width
		class ModelData extends AbstractTableModel {

			String colNames[] = {"Date", "Invoice No", "Status"};
		    Class<?> colClasses[] = { Date.class, String.class , String.class };

		    //Constructor
		    public ModelData() {
		    	data = new ArrayList<Invoice>();
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
		    	case 0: return parseDateSimple(data.get(rowIndex).getDate());
		    	case 1: return data.get(rowIndex).getInvoiceNumber();
		    	case 2: return data.get(rowIndex).getStatus();
		    	
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
		
		public String parseDateSimple(Date d) {
			return new SimpleDateFormat("dd/MM/yyyy").format(d);
		}
		public String parseDateFormal(Date d) {
			return new SimpleDateFormat("dd/MM/yyyy hh:mm a").format(d);
		}

		class JComponentTableCellRenderer implements TableCellRenderer {
			  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			      boolean hasFocus, int row, int column) {
			    return (JComponent) value;
			  }
			}

}
