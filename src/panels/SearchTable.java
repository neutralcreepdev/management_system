package panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.imageio.ImageIO;
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

import QR.BarcodeFormat;
import QR.BitMatrix;
import QR.EncodeHintType;
import QR.ErrorCorrectionLevel;
import QR.QRCodeWriter;
import QR.WriterException;
import comparators.InventorySortByItemID;
import comparators.InventorySortByName;
import comparators.InventorySortByPrice;
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
    boolean gID = true, gName = true, gSupplier = true, gQty = true, gPrice = true, gDesc = true;
    JRoundedTextField name, supplier, itemID, qty, price, description; 
    
	public SearchTable(SearchPanel s ){
		sp = s;
		modelData = new ModelData();
		table = new JTable(modelData);
		currGrocery = new Grocery();

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
		
		table.getColumn("Item ID").setCellRenderer( leftRenderer );
		table.getColumn("Name").setCellRenderer( leftRenderer );
		table.getColumn("Quantity").setCellRenderer( leftRenderer );
		table.getColumn("Price").setCellRenderer( leftRenderer);
		table.getColumn("Supplier Name").setCellRenderer( leftRenderer );
		
		table.getColumn("Item ID").setPreferredWidth(20);
		table.getColumn("Price").setPreferredWidth(10);
		table.getColumn("Quantity").setPreferredWidth(20);
		
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
			if (gQty == false) 
				Collections.reverse(data);
			
			gQty = !gQty;
		break;
		
		case 3:
			Collections.sort(data, new InventorySortByPrice());
			if (gPrice == false) 
				Collections.reverse(data);
			
			gPrice = !gPrice;
		break;
		
		case 4 : 
			Collections.sort(data, new InventorySortBySupplier());
			if (gSupplier == false) 
				Collections.reverse(data);
			
			gSupplier = !gSupplier;
		break;
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
			
			JFrame frame = new JFrame("Update Stock");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setSize(400,450);
			frame.setResizable(false);
			frame.setLayout(null);
			
			JLabel nameLabel, supplierLabel, quantityLabel, priceLabel, descriptionLabel;
			JButton update = new JButton("Update");
			JButton delete = new JButton("Delete");
			JButton generateQR = new JButton("<html><h2>Generate QR</h2></html>");
			
			nameLabel = new JLabel("<html>Name: " + currGrocery.getName()+"</html>");
			supplierLabel = new JLabel("<html>Supplier Name: " + currGrocery.getSupplier() +"</html>");
			quantityLabel = new JLabel("Quantity: " + currGrocery.getQuantity());
			priceLabel = new JLabel("Price(SGD): " + currGrocery.getPrice());
			descriptionLabel = new JLabel("<html>Description: <br>" + currGrocery.getDescription() +"</html>");
			
			name = new JRoundedTextField(1);
			supplier = new JRoundedTextField(1);
			qty = new JRoundedTextField(1);
			price = new JRoundedTextField(1);
			description = new JRoundedTextField(1);
			
			update.addActionListener(new update_AL(frame));
			delete.addActionListener(new delete_AL(frame));
			generateQR.addActionListener(new generateQR_AL());
			
			nameLabel.setBounds(5, 10, 150, 30);
			name.setBounds(180, 10, 200, 30);
			supplierLabel.setBounds(5, 50, 150, 30);
			supplier.setBounds(180, 50, 200, 30);
			quantityLabel.setBounds(5, 90, 120, 20);
			qty.setBounds(180, 90, 200, 30);
			priceLabel.setBounds(5, 130, 120, 20);
			price.setBounds(180, 130, 200, 30);
			descriptionLabel.setBounds(5, 170, 370, 50);
			description.setBounds(5, 220, 370, 50);
			
			update.setBounds(5, 300, 100, 47);
			update.setBackground(new Color(0x62f56e));
			delete.setBounds(5, 350 ,100, 47);
			delete.setBackground(new Color(0xf58c8c));
			generateQR.setBounds(200, 300 , 170, 100);
			generateQR.setBackground(new Color(0xd2fcfc));

			frame.add(nameLabel);
			frame.add(name);
			frame.add(supplierLabel);
			frame.add(supplier);
			frame.add(quantityLabel);
			frame.add(qty);
			frame.add(priceLabel);
			frame.add(price);
			frame.add(descriptionLabel);
			frame.add(description);
			frame.add(update);
			frame.add(delete);
			frame.add(generateQR);
			
			frame.setAlwaysOnTop(true);	
			frame.setVisible(true);

			} catch ( ArrayIndexOutOfBoundsException e1) {
					
		}
			
	}	
	
	class generateQR_AL implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			  String qrCodeText = currGrocery.getItemID() + ":" + currGrocery.getName() + ":" + currGrocery.getDescription() + ":" + currGrocery.getSupplier() + ":" + currGrocery.getPrice() + ":" + currGrocery.getImageURL();
			  String filePath = "QRCodes\\" + currGrocery.getName() + ".png";
			  int size = 125;
			  String fileType = "png";
			  File qrFile = new File(filePath);
			  try {
				createQRImage(qrFile, qrCodeText, size, fileType);
			} catch (WriterException | IOException e) {
				System.out.println("error with QR generation");
			}
			  System.out.println("DONE");
		}
		
	}
	class update_AL implements ActionListener {
		JFrame frame;
			
		public update_AL(JFrame temp) {
			frame = temp;
		}
			
		public void actionPerformed(ActionEvent e) {
			
			Grocery temp = new Grocery();		
		
			int fieldChecker = 0;
			
			
			//Check if the textbox is empty
			// If empty == true Then set to default (old) value
			// Else set to the new updated value
			if(name.getText().trim().isEmpty() == true) {
				fieldChecker++;
				temp.setName(currGrocery.getName());
			} else
				temp.setName(name.getText());	
			
			if(supplier.getText().trim().isEmpty() == true) {
				fieldChecker++;
				temp.setSupplier(currGrocery.getSupplier());
			} else 
				temp.setSupplier(supplier.getText());
			
			if(qty.getText().trim().isEmpty() == true) {
				fieldChecker++;
				temp.setQuantity(currGrocery.getQuantity());
			} else 
				temp.setQuantity(Long.parseLong(qty.getText()));
			
			if(price.getText().trim().isEmpty() == true) {
				fieldChecker++;
				temp.setPrice(currGrocery.getPrice());
			} else 
				temp.setPrice(Double.parseDouble(price.getText()));
			if(description.getText().trim().isEmpty() == true) {
				fieldChecker++;
				temp.setDescription(currGrocery.getDescription());
			} else
				temp.setDescription(description.getText());
			
			if( fieldChecker == 5) {
				JOptionPane.showMessageDialog(null, "Fields are empty" , "Updating Stock", 2);
			} else {
			
			//Function to compare the difference between the objects
			temp = compare(temp);
			
			if( temp == null ) {
			//Do nothing
			} else
				//Update the database
				//Table Update is handled by InteractionView
			
				sp.updateStock(temp, currGrocery);
			}
			
			frame.dispose();
			try {
				Thread.sleep(1500);
				sp.updateTable();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
			
	class delete_AL implements ActionListener {
		JFrame frame;
				
		public delete_AL(JFrame temp) {
				frame = temp;
		}
				
		public void actionPerformed(ActionEvent e) {
			//Send a Delete Request To Firestore
			//Table Update is handled by InteractionView
			sp.deleteStock(currGrocery);
			
			frame.dispose();
			
			//CONSIDER THIS TO BE NON-THREADED
			try {
				Thread.sleep(1500);
				sp.updateTable();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
			
	}		
	
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e)  {}
	public void mouseExited(MouseEvent e)   {}
	public void mouseClicked(MouseEvent e)  {}
	
	//Update this
	public Grocery compare(Grocery input) {
		
		boolean sendRequest = true;
		int sameCounter = 0;
		
		//name is fine to be the same in different casing
		//Same for supplier
		//itemID also can be the same
		//qty and price can also be the same
		
		//Base case that checks for all the same value
		//Ultimately i want to know that at least 1 change has been made
		
		//Only QTY checks for differences and sends the differences to the database
		
		Grocery updated = new Grocery();
		
		if(currGrocery.getName().equals(input.getName()) == true)
			sameCounter++;
		
		updated.setName(input.getName());

		
		if ( currGrocery.getSupplier().equals(input.getSupplier()) == true) 
			sameCounter++;

		updated.setSupplier(input.getSupplier());
		
		if ( currGrocery.getQuantity() == input.getQuantity() ) {
			sameCounter++;
			updated.setQuantity(0);
		} else { 
			
			//input - curr
			// 80 - 100 = -20 (100+(-20) = 80)		
			// 120 - 100 = 20 ( 100+(20) = 120)
			
			long result = input.getQuantity() - currGrocery.getQuantity();
			
			//Check for 0 is not needed as the first IF resolves that possiblity
			
			updated.setQuantity(result);
			
		}
		if (currGrocery.getPrice() == input.getPrice() ) {
			sameCounter++;
			updated.setPrice(input.getPrice());
		} else {
			
			if(input.getPrice() < 0.01) {
				JOptionPane.showMessageDialog(null, "Price cannot be less than $0.01 - No update executed" , "Updating Stock", 2);
				return null;
			}
			
			updated.setPrice(input.getPrice());
			
		}
		
		updated.setItemID(currGrocery.getItemID());
		
		if (sameCounter == 5) {
			JOptionPane.showMessageDialog(null, "No Changes Detected - No update executed" , "Updating Stock", 2);
			return null;
		} 
		
		return updated;
	}
	
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
	    Class<?> colClasses[] = { String.class, String.class, Long.class, Double.class, String.class  };

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
	
	private static void createQRImage(File qrFile, String qrCodeText, int size, String fileType) throws WriterException, IOException {
			  // Create the ByteMatrix for the QR-Code that encodes the given String
			  Hashtable hintMap = new Hashtable();
			  hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			  QRCodeWriter qrCodeWriter = new QRCodeWriter();
			  BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText,
			    BarcodeFormat.QR_CODE, size, size, hintMap);
			  // Make the BufferedImage that are to hold the QRCode
			  int matrixWidth = byteMatrix.getWidth();
			  BufferedImage image = new BufferedImage(matrixWidth, matrixWidth,
			    BufferedImage.TYPE_INT_RGB);
			  image.createGraphics();
			 
			  Graphics2D graphics = (Graphics2D) image.getGraphics();
			  graphics.setColor(Color.WHITE);
			  graphics.fillRect(0, 0, matrixWidth, matrixWidth);
			  // Paint and save the image using the ByteMatrix
			  graphics.setColor(Color.BLACK);
			 
			  for (int i = 0; i < matrixWidth; i++) {
			   for (int j = 0; j < matrixWidth; j++) {
			    if (byteMatrix.get(i, j)) {
			     graphics.fillRect(i, j, 1, 1);
			    }
			   }
			  }
			  ImageIO.write(image, fileType, qrFile);
			 }
			 
	
	
	
	class JComponentTableCellRenderer implements TableCellRenderer {
		  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		      boolean hasFocus, int row, int column) {
		    return (JComponent) value;
		  }
		}
}
