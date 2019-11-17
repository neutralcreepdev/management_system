package tables;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
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
import panels.SearchPanel;
import resources.Grocery;
import resources.ImageFilter;
import resources.ImagePreview;
import resources.JRoundedTextArea;
import resources.JRoundedTextField;

//http://www.java2s.com/Tutorial/Java/0240__Swing/CustomizingColumnHeaderswithIcons.htm

@SuppressWarnings("serial")
public class SearchTable extends JPanel implements MouseListener {
	JPanel mainPanel;
	JTable table;
	JScrollPane scrollPane;
    List<Grocery> data;
    List<Grocery> tempData;
    Grocery currGrocery;
    ModelData modelData;
    SearchPanel sp;
    final JTableHeader header;
    boolean gID = true, gName = true, gSupplier = true, gQty = true, gPrice = true, gDesc = true;
    JRoundedTextField name, supplier, itemID, qty, price;
    JRoundedTextArea area;
    String imagePath = "";
    ImageIcon imgPreview;

	public SearchTable(SearchPanel s ){
		sp = s;
		mainPanel = sp.getMainPanel();
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
		DefaultTableCellRenderer middleRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		middleRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		table.setBackground(new Color(0xdaddd8));
		scrollPane.getViewport().setBackground(new Color(0xf2f5f6));
		
		table.getColumn("Item ID").setCellRenderer( middleRenderer );
		table.getColumn("Name").setCellRenderer( leftRenderer );
		table.getColumn("Quantity").setCellRenderer( middleRenderer );
		table.getColumn("Price").setCellRenderer( middleRenderer);
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
			case "<none>":
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
						JOptionPane.showMessageDialog(mainPanel, "Enter a number!", "Search by Quantity", 2, null);
						break;
					}
				}
				break;
		}
		
		if (temp.size() < 1)
			JOptionPane.showMessageDialog(mainPanel, "No results found for ( " + value +" )", "Search Results", 2, null);

		else {
			
			data = temp;
			table.revalidate();
			table.repaint();
	    	System.out.println("Filtered Table");
		}
	}
	
	public void mousePressed(MouseEvent e) {
		
		if(e.getClickCount() % 2 == 0) {		
			try {
				currGrocery = data.get(table.getSelectedRow());
				
				// Here should add a function to allow the user to edit and send the new update to the server
				// Function will calculate the difference between the Current Grocery Object from Arraylist and the Newly Input Grocery Object
				// When writing to DB, GUI will query for the latest information then the program will do the maths with the latest field and update.
				
				JFrame frame = new JFrame("Update Grocery Information");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setSize(900,440);
				frame.setResizable(false);
				frame.setLayout(null);
				
				JPanel imagePreviewPanel = new JPanel();
				JPanel previewPanel = new JPanel();
				previewPanel.setSize(400, 400);
				previewPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Image Preview"));
				
				JButton update = new JButton("<html><h2>UPDATE</h2></html>");
				JButton delete = new JButton("<html><h3>DELETE</h3></html>");
				JButton generateQR = new JButton("<html><h3>QR CODE</h3></html>");
				JButton imageSelector = new JButton("Select Image");
				JButton deleteImage = new JButton("X");
				
				JLabel nameLabel = new JLabel("<html>Name: " + currGrocery.getName()+"</html>");
				JLabel supplierLabel = new JLabel("<html>Supplier Name: " + currGrocery.getSupplier() +"</html>");
				JLabel quantityLabel = new JLabel("Quantity: " + currGrocery.getQuantity());
				JLabel priceLabel = new JLabel("Price: " + NumberFormat.getCurrencyInstance().format(currGrocery.getPrice()));
				JLabel descriptionLabel = new JLabel("<html>Description: <br>" + currGrocery.getDescription() +"</html>");
	   
				name = new JRoundedTextField(1);
				supplier = new JRoundedTextField(1);
				qty = new JRoundedTextField(1);
				price = new JRoundedTextField(1);
				//description = new JRoundedTextField(1);
			
				area = new JRoundedTextArea();
				area.setLineWrap(true);
				area.setWrapStyleWord(true);
			    JScrollPane description = new JScrollPane(area);
			    description.setBorder(null);

				
				
				//Retrieve Image URL from firebase link || Dependant on the internet speed
				imagePath = currGrocery.getImageURL();
				Image test = ImageIO.read(new URL(imagePath));
				imgPreview = new ImageIcon(test);
				imgPreview = new ImageIcon(imgPreview.getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
				
				if(!imagePath.isEmpty()) {
					imagePreviewPanel.add(new JLabel(imgPreview));
					imagePreviewPanel.repaint();
					imagePreviewPanel.revalidate();		
				}
								
				update.addActionListener(new ActionListener() {
					String url = "";				
					public void actionPerformed(ActionEvent e) {
						boolean imageEmpty = false;
						boolean priceIsPositive = false;
						boolean qtyIsPositive = false;
						Grocery temp = new Grocery();		
					
						int fieldChecker = 0;
						
						//Check if the textbox is empty
						// If empty == true Then set to default (old) value
						// Else set to the new updated value
						try {
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
							qtyIsPositive = true;
						} else {
							if (Long.parseLong(qty.getText()) < 0)
								qtyIsPositive = false;
							else {
								temp.setQuantity(Long.parseLong(qty.getText()));
								qtyIsPositive = true;
							}
						}
						if(price.getText().trim().isEmpty() == true) {
							fieldChecker++;
							temp.setPrice(currGrocery.getPrice());
							priceIsPositive = true;
						} else {
							if ( Double.parseDouble(price.getText()) < 0.01)
								priceIsPositive = false;
							else {
								temp.setPrice(Double.parseDouble(price.getText()));
								priceIsPositive = true;
							}
						}
						
						if(area.getText().trim().isEmpty() == true) {
							fieldChecker++;
							temp.setDescription(currGrocery.getDescription());
						} else
							temp.setDescription(area.getText());

						//if ImageURL is different, Run a Image Uploader here and retrieve URL first before commiting to the update function
						if(currGrocery.getImageURL().equalsIgnoreCase(imagePath) == false) {
								
							url = sp.uploadImagetoGCS(temp.getName() + randomNumberGenerator() + ".jpg", imagePath);
							System.out.println("New url for image = " + url);
							fieldChecker--; // If Use case is to replace image, then this will enable it		
						} else
							url = currGrocery.getImageURL();  //if same then ignore.
						
						
						if( fieldChecker == 5) {
							if(imageEmpty == false)
								JOptionPane.showMessageDialog(frame, "Fields are empty" , "Updating Stock", 2);							
						} else if (qtyIsPositive == false) {
							JOptionPane.showMessageDialog(frame, "Quantity cannot be below 0" , "Updating Stock", 2);
						} else if (priceIsPositive == false) {
							JOptionPane.showMessageDialog(frame, "Price of Stock cannot be less than $0.01" , "Updating Stock", 2);	
						} else {
						
							//Function to compare the difference between the objects
							if( compare(temp, frame) == false)
								JOptionPane.showMessageDialog(frame, "No Changes Detected - No update executed" , "Updating Stock", 2);
							else {
								
								//Remaining Fields that has not been added to the Grocery Object
								temp.setItemID(currGrocery.getItemID());
								temp.setCategory(currGrocery.getCategory());
								temp.setImageUrl(url);
								
								sp.updateStock(temp, currGrocery);
				
								frame.dispose();
								
								try {
									Thread.sleep(1500);
									sp.updateTable();
								} catch (InterruptedException e1) {
									System.out.println("Update Stock Caught an InterruptedException - SearchTable/SearchPanel/Update_AL");
								}
								
							}
						}
					} catch (NumberFormatException ee) {
						JOptionPane.showMessageDialog(frame, "Value of Quantity or Price must be a positive number" , "Updating Stock", 2);
						}
					}
				});
				
				//Send a Delete Request To Firestore
				//Table Update is handled by InteractionView
				delete.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						sp.deleteStock(currGrocery);
						frame.dispose();
						sp.updateTable();
					}
				});
				generateQR.addActionListener(new ActionListener() {
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
							JOptionPane.showMessageDialog(mainPanel, "Error generating the QR Code, please contact Technical Support for assistance", "Generation Error", 0, null);
						}
						  JOptionPane.showMessageDialog(mainPanel, "QR Code Image Stored at \"" + filePath + "\"", "Generation Success", 1, null);
					}
				});
				imageSelector.addActionListener(new ActionListener() {
	
					@Override
					public void actionPerformed(ActionEvent arg0) {
						//To reset the imagePreview if not image is selected
						String tempImagePath = imagePath;
						
						//Checks if the image is being overwritten (User decides to upload another one instead of removing current image)
						if(!imagePath.isEmpty()) {
							imagePreviewPanel.removeAll();
							imagePreviewPanel.repaint();
							imagePreviewPanel.revalidate();
						}
						
						imagePath = fileChooser();
						imgPreview = new ImageIcon(imagePath);
						
						//Checks if the image is selected, else leave it as a blank panel
						if(!imagePath.isEmpty()) {	
							imgPreview = new ImageIcon(imgPreview.getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
								
							imagePreviewPanel.add(new JLabel(imgPreview));
							imagePreviewPanel.repaint();
							imagePreviewPanel.revalidate();
							
						} else {
							//Reset the ImageURL to original
							//System.out.println("Reset to original");
							imagePath = tempImagePath;
							imgPreview = new ImageIcon(test);
							imgPreview = new ImageIcon(imgPreview.getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
							
							imagePreviewPanel.add(new JLabel(imgPreview));
							imagePreviewPanel.repaint();
							imagePreviewPanel.revalidate();
						}
					}
					
				});
				deleteImage.addActionListener(new ActionListener() {
	
					@Override
					public void actionPerformed(ActionEvent arg0) {
						imagePreviewPanel.removeAll();
						imagePreviewPanel.repaint();
						imagePreviewPanel.revalidate();
					}
					
				});
				
				//Set Colors
				update.setBackground(new Color(0x62f56e));
				delete.setBackground(new Color(0xf58c8c));
				generateQR.setBackground(new Color(0xd2fcfc));
				deleteImage.setForeground(Color.RED);				
				
				/*Positions (Bounds)*/
				
				//BUTTONS
				generateQR.setBounds(15, 290 , 100, 47);
				delete.setBounds(15, 340 ,100, 47);
				update.setBounds(200, 290, 170, 97);
				
				//Labels & TextFields
				nameLabel.setBounds(20, 10, 150, 30); 			name.setBounds(200, 10, 200, 30);
				supplierLabel.setBounds(20, 55, 150, 30); 		supplier.setBounds(200, 50, 200, 30);
				quantityLabel.setBounds(20, 95, 120, 20); 		qty.setBounds(200, 90, 200, 30);
				priceLabel.setBounds(20, 135, 120, 20); 			price.setBounds(200, 130, 200, 30);
				descriptionLabel.setBounds(20, 170, 370, 50); 	description.setBounds(20, 220, 370, 50);
				
				//Preview Panel
				previewPanel.setBounds(450, 10, 400, 380);				
				imagePreviewPanel.setBounds(10, 80, 300, 300);
				imageSelector.setBounds(50, 10, 120, 30);
				deleteImage.setBounds(200, 10, 30, 30);
	
				previewPanel.add(imagePreviewPanel);
				previewPanel.add(imageSelector);
				previewPanel.add(deleteImage);
				
				frame.add(previewPanel);
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
				
				frame.setVisible(true);
				
			} catch ( ArrayIndexOutOfBoundsException e1) {
					
			} catch (IOException e1) {
				System.out.println("Image URL invalid - SearchTable/SearchPanel/mousePressed");
			}		
		}
	}
	
	
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e)  {}
	public void mouseExited(MouseEvent e)   {}
	public void mouseClicked(MouseEvent e)  {}
	
	public String randomNumberGenerator() {
		Random a = new Random();
		return "" + a.nextInt(1000);
	}
	
	/* Conditions for Comparison
	 * name, supplier, description is fine to be the same in different casing
	 * itemID must be the same
	 * quantity and price can be the same
	 * Base case that checks for all the same value
	 * Ultimately i want to know that at least 1 change has been made
	 * 		- Only QTY checks for differences and sends the differences to the database
	*/
	public boolean compare(Grocery newGrocery, JFrame frame ) {
		
		int sameCounter = 0;
		//Name 
		if(currGrocery.getName().equals(newGrocery.getName()) == true)
			sameCounter++;
		//Supplier
		if ( currGrocery.getSupplier().equals(newGrocery.getSupplier()) == true) 
			sameCounter++;
	    //Quantity
		if ( currGrocery.getQuantity() == newGrocery.getQuantity())
			sameCounter++;
		else
			newGrocery.setQuantity(newGrocery.getQuantity() - currGrocery.getQuantity());			
		//Price
		if (currGrocery.getPrice() == newGrocery.getPrice() )
			sameCounter++;
		
		//Return statement on the function
		if (sameCounter == 5) 	
			return false;
		else 
			return true;
	}
	
	public void updateTableGrocery(ArrayList<Grocery> g) {
		tempData = g;
		data = g;
		table.revalidate();
		table.repaint();
    	
    	System.out.println("Update Table Grocery");
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
	    	case 2: return data.get(rowIndex).getQuantity();
	    	case 3: return NumberFormat.getCurrencyInstance().format(data.get(rowIndex).getPrice());
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	
	public String fileChooser() {
		//Set up the file chooser.
		JPanel fileChooserPanel = new JPanel();
		JFileChooser fc = new JFileChooser();

			fc.addChoosableFileFilter(new ImageFilter());
			fc.setAcceptAllFileFilterUsed(false);

			fileChooserPanel.setBackground(Color.WHITE);
			fileChooserPanel.add(fc);
			fileChooserPanel.setVisible(true);

			fc.setAccessory(new ImagePreview(fc));

			int returnVal = fc.showOpenDialog(fc);
			//Process the results.
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				//Get file name or file here.
				try {
					return fc.getSelectedFile().getPath();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "File you've chosen is of the wrong format", "File Format Invalid", 0);
				}
			}
		return "";
	}
	
	class JComponentTableCellRenderer implements TableCellRenderer {
		  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		      boolean hasFocus, int row, int column) {
		    return (JComponent) value;
		  }
		}
}
