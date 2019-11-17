package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import panels.DeliveryPanel;
import panels.SearchPanel;
import panels.TaskBar;
import panels.TaskBar.ClockListener;
import resources.Delivery;
import resources.Grocery;
import resources.ImageFilter;
import resources.ImagePreview;
import resources.JRoundedTextArea;
import resources.JRoundedTextField;
import resources.Staff;
import resources.TransactionHistory;
import tables.ImportReplenishmentTable;
import tables.StockOrderTable;
import tables.ViewReplenishmentTable;

// This is the main view page for the user's interaction
@SuppressWarnings("serial")
public class InteractionView extends JPanel{

	SearchPanel sp;
	DeliveryPanel dp;
	TaskBar tb;
	ArrayList<Grocery> Groceries;
	JButton refreshButton, addStockButton, importStockDelivery, urgentStockRequest, weeklyStockRequest;
	Driver driver;
	JPanel mainPanel = this;

	JLabel dailyTotal = new JLabel("Daily Sales: $0.00");
	JLabel monthlyTotal = new JLabel("$0.00");
	javax.swing.Timer dailySalesTimer;
	
	public InteractionView (Driver mainDriver) {

		driver = mainDriver;
		sp = new SearchPanel(this);
		dp = new DeliveryPanel(this);
		tb = new TaskBar(this);
		
		setLayout(null);
		refreshButton = new JButton("Refresh Page");
		refreshButton.setBounds(690, 120, 120, 20);
		addStockButton = new JButton("Add New Stock");
		addStockButton.setBounds(690, 160, 120, 20);
		importStockDelivery = new JButton("Receive Stock");
		importStockDelivery.setBounds(690, 200, 120, 20);
		urgentStockRequest = new JButton("<html><center>Urgent<br>Stock Ordering</center></html>");
		urgentStockRequest.setBounds(690, 240, 120, 40);
		weeklyStockRequest = new JButton("<html><center>Weekly <br>Stock Ordering</center></html>");
		weeklyStockRequest.setBounds(690, 300, 120, 40);

		dailyTotal.setFont(new Font("sansserif", Font.PLAIN, 20));
		dailyTotal.setBounds(670, 60, 200, 20);
		setSize(1525, 800);

		refreshButton.addActionListener(new Refresh_AL());
		addStockButton.addActionListener(new AddStock_AL());
		importStockDelivery.addActionListener(new ImportStock_AL());
		urgentStockRequest.addActionListener(new UrgentStockRequest_AL());
		weeklyStockRequest.addActionListener(new WeeklyStockRequest_AL());
		
		dailySalesTimer = new javax.swing.Timer(20000, new SalesListener());
		
		add(refreshButton);
		add(addStockButton);
		add(importStockDelivery);
		add(urgentStockRequest);
		add(weeklyStockRequest);
		add(sp);
		add(dp);
		add(tb);
		add(dailyTotal);
		setBackground(new Color(0xf2f5f6));
		setVisible(false);

	}

	public void setView() {
		sp.setVisible(true);
		dp.setVisible(true);
		tb.setVisible(true);
	}

	public void setData(ArrayList<Grocery> g, ArrayList<Delivery> d) {
		Groceries = g;
		sp.updateTable(g);
		dp.updateTable(d);
	}

	public void logout() {
		dailySalesTimer.stop();
		driver.logout();
	}

	public void updateStock(Grocery updatedG, Grocery oldG) {
		driver.updateGrocery(updatedG, oldG);
	}

	public void deleteStock(Grocery g) {
		driver.deleteGrocery(g);
	}

	public void createStaff(Staff s) {
		driver.createStaff(s);
	}

	public void updateTables() {
		//Retrieve Latest Database Connection list
		driver.readData();
		sp.updateTable(driver.getGrocery());
		dp.updateTable(driver.getPendingDelivery());

	}

	public void updateGroceryTable() {
		driver.readData();
		sp.updateTable(driver.getGrocery());
	}

	public ArrayList<Delivery> getPastDeliveries(){
		return driver.getPastDelivery();
	}

	public class Refresh_AL implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			updateTables();
		}
	}

	public class ImportStock_AL implements ActionListener {
		JButton selected;
		JFrame frame;
		public void setButtonState() {
			selected.setEnabled(true);
			frame.revalidate();
			frame.repaint();
		}
		
		public void actionPerformed(ActionEvent arg0) {
			frame = new JFrame("Replenishment System");
			selected = new JButton("<html>Receive</html>");
			JButton view = new JButton("<html>View List</html>");

			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setSize(800, 520);
			frame.setLayout(null);

			ImportReplenishmentTable rt = new ImportReplenishmentTable(this);
			rt.updateTableInvoice(driver.getReplenishmentList());
			
			//Retrieve Invoice Array here
			selected.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if ( rt.getSelectedInvoice().getInvoiceNumber().equalsIgnoreCase("0000000000"))
						JOptionPane.showMessageDialog(mainPanel, "No Replenishment Order Selected!" , "Stock Replenishment", 2);
					else if (rt.getSelectedInvoice().getStatus().equalsIgnoreCase("Completed"))
						JOptionPane.showMessageDialog(mainPanel, "Replenishment order already received!" , "Stock Replenishment", 2);
					else {
						//Process the changes (Update the values and status in Firebase)
						driver.processReplenishmentRequest(rt.getSelectedInvoice());
						try {
							Thread.sleep(1000);
							updateGroceryTable();
							rt.updateTableInvoice(driver.getReplenishmentList());
							selected.setEnabled(false);
							
							JOptionPane.showMessageDialog(mainPanel, "Replenishment Order "+ rt.getSelectedInvoice().getInvoiceNumber() + " has been received!" , "Stock Replenishment - Received", 2);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
			});

			view.addActionListener(new ActionListener()  {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					if ( rt.getSelectedInvoice().getInvoiceNumber().equalsIgnoreCase("0000000000"))
						JOptionPane.showMessageDialog(mainPanel, "No Replenishment Order Selected!" , "Stock Replenishment", 2);
					else {
						JFrame f = new JFrame("View Order " + rt.getSelectedInvoice().getInvoiceNumber());
						f.setResizable(false);
						f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						f.setSize(700, 500);
						f.setLayout(null);
	
						//Function of this is to SOLELY view
						ViewReplenishmentTable vr = new ViewReplenishmentTable();
						vr.updateTable(rt.getSelectedInvoice().getReplenishmentList());
						vr.setBounds(10, 10, 600, 500);
						f.add(vr);
						f.setVisible(true);
					}
				}
			});

			rt.setBounds(10, 10, 600, 450);
			selected.setBounds(640, 60, 100, 50);
			view.setBounds(640, 120, 100, 50);

			frame.add(rt);
			frame.add(selected);
			frame.add(view);
			frame.setVisible(true);
		}
	}

	public class WeeklyStockRequest_AL implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			driver.readData();
					
			JFrame solFrame;
			StockOrderTable sol = new StockOrderTable();
			sol.updateOrderListGrocery(driver.getGroceryMutedNotifications());
			solFrame = new JFrame("Weekly Stock Ordering");

			solFrame.setResizable(false);
			solFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			solFrame.setSize(900, 520);
			solFrame.setLayout(null);

			JButton submit = new JButton("Submit Order");
			JButton selectAll = new JButton("Select All");
			JButton deSelectAll = new JButton("Unselect All");
			submit.addActionListener(new SubmitOrder_AL(sol, solFrame, "Normal"));
			selectAll.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					sol.checkAll();					
				}
				
			});
			deSelectAll.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					sol.uncheckAll();					
				}
				
			});
			

			selectAll.setBounds(350, 450, 147, 20);
			deSelectAll.setBounds(525, 450, 147, 20);	
			submit.setBounds(700, 450, 147, 20);
			sol.setBounds(40, 10 , 810, 430);
			
			solFrame.add(selectAll);
			solFrame.add(deSelectAll);
			solFrame.add(sol);
			solFrame.add(submit);
			solFrame.setVisible(true);
		}
	}
	
	public class UrgentStockRequest_AL implements ActionListener {
		
		public void actionPerformed(ActionEvent arg0) {
			driver.readData();
			sp.updateTable(driver.getGrocery());
			
			String text = driver.getLowStock();
			ArrayList<Grocery> GroceriesOrderList = new ArrayList<>();
			JFrame solFrame;
			StockOrderTable sol = new StockOrderTable();
			solFrame = new JFrame("Urgent Stock Ordering");
			String[] a = text.split("\n");

					for (int i = 0; i < a.length; i++) {
						for (Grocery g : Groceries) {
							if ( g.getName().equals(a[i])) {
								GroceriesOrderList.add(g);
							}
						}
					}
					sol.updateOrderListGrocery(GroceriesOrderList); 

			solFrame.setResizable(false);
			solFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			solFrame.setSize(900, 520);
			solFrame.setLayout(null);
			JButton selectAll = new JButton("Select All");
			JButton deSelectAll = new JButton("Unselect All");
			JButton submit = new JButton("Submit Order");
			submit.addActionListener(new SubmitOrder_AL(sol, solFrame, "Urgent"));	
			selectAll.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					sol.checkAll();					
				}
				
			});
			deSelectAll.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					sol.uncheckAll();					
				}
				
			});
			

			selectAll.setBounds(350, 450, 147, 20);
			deSelectAll.setBounds(525, 450, 147, 20);	

			
			submit.setBounds(700, 450, 147, 20);
			sol.setBounds(40, 10 , 810, 430);
			solFrame.add(selectAll);
			solFrame.add(deSelectAll);
			solFrame.add(sol);
			solFrame.add(submit);
			solFrame.setVisible(true);
		}
	}

	public class SubmitOrder_AL implements ActionListener {

		StockOrderTable sol;
		JFrame frame;
		String status;
		public SubmitOrder_AL(StockOrderTable s, JFrame f, String status) { 
			sol = s;
			frame = f;
			this.status = status;
		}

		public void actionPerformed(ActionEvent arg0) {
			//Send the SOL to Driver class to upload to "Request Stock" Collections
			ArrayList<Grocery> toOrderList = new ArrayList<>();

			for (Grocery g : sol.getOrderList()) {
				if(g.getReplenishmentStatus() == true)
					toOrderList.add(g);
			}
			
			if(toOrderList.size() > 0) {
				String invoiceNo = driver.sendReplenishmentRequest(toOrderList, status);
				JOptionPane.showMessageDialog(mainPanel, "Order has been successfully posted!" , "Posting Successful for #" + invoiceNo , 2);	
				frame.dispose();
			} else 
				JOptionPane.showMessageDialog(mainPanel, "No Stock(s) Selected" , "Request for Stock", 2);	
		}

	}	

	public class AddStock_AL implements ActionListener {
		JRoundedTextField name, supplier, qty, price, category;
		JRoundedTextArea area;
		String imagePath = "";
		ImageIcon imgPreview;

		public void actionPerformed(ActionEvent arg0) {

			JFrame addNewStock = new JFrame("Add New Stock");
			JPanel imagePreviewPanel = new JPanel();
			JPanel previewPanel = new JPanel();
			
			previewPanel.setSize(400, 400);
			previewPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Image Preview"));
			
			imagePreviewPanel.setSize(300,300);
			
			addNewStock.setResizable(false);
			addNewStock.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			addNewStock.setSize(750,440);
			addNewStock.setLayout(null);

			JLabel nameLabel, supplierLabel, quantityLabel, priceLabel, descriptionLabel, categoryLabel;

			JButton submit = new JButton("<html><h1>SUBMIT</h1></html>");
			JButton imageSelector = new JButton("Select Image");
			JButton deleteImage = new JButton("X");
			submit.setBackground(new Color(0x0cb004));
			submit.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			deleteImage.setForeground(Color.RED);

			nameLabel = new JLabel("Name: ");
			supplierLabel = new JLabel("Supplier: ");
			descriptionLabel = new JLabel("Description: ");
			quantityLabel = new JLabel("Quantity: ");
			priceLabel = new JLabel("Price(SGD): ");
			categoryLabel = new JLabel("Category: ");
			
			name = new JRoundedTextField(1);
			supplier = new JRoundedTextField(1);
			qty = new JRoundedTextField(1);
			qty.setText("0");
			price = new JRoundedTextField(1);
			price.setText("0.00");
			category = new JRoundedTextField(5);
			
			area = new JRoundedTextArea();
			area.setLineWrap(true);
			area.setWrapStyleWord(true);
		    JScrollPane description = new JScrollPane(area);
		    description.setBorder(null);

			imageSelector.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(imagePath.isEmpty() == false) {
						imagePreviewPanel.removeAll();
						imagePreviewPanel.repaint();
						imagePreviewPanel.revalidate();
					}
					
					imagePath = fileChooser();
					imgPreview = new ImageIcon(imagePath);
					
					if(!imagePath.isEmpty()) {	
						imgPreview = new ImageIcon(imgPreview.getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
							
						imagePreviewPanel.add(new JLabel(imgPreview));
						imagePreviewPanel.repaint();
						imagePreviewPanel.revalidate();

					}
					//Set a resizing function here
				}
				
			});
			deleteImage.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					imagePath = "";
					imagePreviewPanel.removeAll();
					imagePreviewPanel.repaint();
					imagePreviewPanel.revalidate();
					
				}
				
			});
			submit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					//Error Handle the information
					boolean duplicateCheck = false, price_numberFormatError = false, qty_numberFormatError = false;
					boolean nameFieldStatus = false, suppFieldStatus = false, qtyFieldStatus = false, priceFieldStatus = false , descFieldStatus = false;
					boolean replenishmentStatus = false;
					boolean categoryStatus = false;
					double p = 0.00;
					long q = 0;

					//Check if the textbox is empty
					//Error handling is to check against a name and a supplier for duplicates

					if(name.getText().trim().isEmpty() == true)
						nameFieldStatus = true;

					if(supplier.getText().trim().isEmpty() == true)
						suppFieldStatus = true;

					if(qty.getText().trim().isEmpty() == true)
						qtyFieldStatus = true;

					if(price.getText().trim().isEmpty() == true)
						priceFieldStatus = true;

					if(area.getText().trim().isEmpty() == true)
						descFieldStatus = true;
					
					if(category.getText().trim().isEmpty() == true)
						categoryStatus = true;
					
					try {
						q = Long.parseLong(qty.getText().trim());
						if ( q > 50 )
							replenishmentStatus = false;
						else if ( q >= 0 && q < 50)
							replenishmentStatus = true;
						else {
							JOptionPane.showMessageDialog(mainPanel, "Quantity cannot be less than 0" , "Add New Stock", 2);
							qty_numberFormatError = true;
						}
					} catch (NumberFormatException e1) {
						qty_numberFormatError = true;
					}

					try {
						p = Double.parseDouble(price.getText().trim());

						if(p < 0) {
							JOptionPane.showMessageDialog(mainPanel, "Price cannot be less than 0" , "Add New Stock", 2);
							price_numberFormatError = true;
						}
					} catch (NumberFormatException e1) {
						price_numberFormatError = true;
					}

					if( nameFieldStatus == true|| suppFieldStatus == true || qtyFieldStatus == true || priceFieldStatus == true || descFieldStatus == true || categoryStatus == true)
						JOptionPane.showMessageDialog(mainPanel, "1 or more fields are empty" , "Add New Stock", 2);
					else if (price_numberFormatError == true)
						JOptionPane.showMessageDialog(mainPanel, "ERROR! - Price is not a decimal number or an Invalid Character Detected" , "Add New Stock", 0);	
					else if (qty_numberFormatError == true)
						JOptionPane.showMessageDialog(mainPanel, "ERROR! - Quantity cannot be a decimal or an Invalid Character Detected" , "Add New Stock", 0);
					else if(p < 0.01)
						JOptionPane.showMessageDialog(mainPanel, "Price cannot be less than $0.01" , "Add New Stock", 0);
					else {
						for ( Grocery g: Groceries) {
							if ( g.getName().equalsIgnoreCase(name.getText().trim()) && g.getSupplier().equalsIgnoreCase(supplier.getText().trim()))
								duplicateCheck = true;
						}

						if(duplicateCheck == false) {
							//Default Image URL
							if (imagePath.isEmpty() == true)
								JOptionPane.showMessageDialog(mainPanel, "Please add a picture for grocery!" , "Add New Stock - Add Picture", 0);
							else {

								//Generate new Grocery ID function
								Grocery g = new Grocery(name.getText(), supplier.getText(), generateNewGroceryID(), q, p, "", area.getText(),  replenishmentStatus, "Others" );
								
								//Run the GCS METHOD
								g.setImageUrl(driver.storeImageToGCS(g.getName() + ".jpg", imagePath));
								driver.addGrocery(g);
								imagePath = "";
								//Subsequently refresh SearchPanel and  SearchTable
								driver.readData();
								sp.updateTable(driver.getGrocery());
								addNewStock.dispose();
							}

						} else
							JOptionPane.showMessageDialog(mainPanel, "Duplicate product!" , "Add New Stock", 0);
					}

				}
			});
			
			
			nameLabel.setBounds			(20, 15, 100, 20);			name.setBounds			(95, 10, 170, 30);
			supplierLabel.setBounds		(20, 55, 100, 20);			supplier.setBounds		(95, 50, 170, 30);
			quantityLabel.setBounds		(20, 95, 100, 20);			qty.setBounds			(95, 90, 170, 30);
			priceLabel.setBounds		(20, 135, 100, 20);			price.setBounds			(95, 130, 170, 30);
			categoryLabel.setBounds		(20, 175, 100, 20);			category.setBounds		(95, 170, 170, 30);
			descriptionLabel.setBounds	(20, 210, 100, 20);			description.setBounds	(20, 235, 250, 70);
			
			submit.setBounds(40, 320, 210, 65);
			previewPanel.setBounds(310, 10, 400, 380);

			previewPanel.add(imagePreviewPanel);
			previewPanel.add(imageSelector);
			previewPanel.add(deleteImage);
		    
			addNewStock.add(nameLabel);
			addNewStock.add(name);
			addNewStock.add(supplierLabel);
			addNewStock.add(supplier);
			addNewStock.add(quantityLabel);
			addNewStock.add(qty);
			addNewStock.add(priceLabel);
			addNewStock.add(price);
			addNewStock.add(descriptionLabel);
			addNewStock.add(description);
			addNewStock.add(categoryLabel);
			addNewStock.add(category);
			addNewStock.add(submit);
			addNewStock.add(previewPanel);

			addNewStock.setVisible(true);

		}
	}	
	
	public String uploadImagetoGCS(String fileName,String filePath) {
		return driver.storeImageToGCS(fileName, filePath);
	}

	public String generateNewGroceryID() {
		ArrayList<Grocery> g = driver.getGrocery();
		String id = "" + (g.size()+1);
		System.out.println("ID: " + id);

		for (int i = 0; i < 8; i++) {
			if ( id.length() < 8) {
				id = "0" + id;
			}
		}

		return id;
	}

	public ArrayList<Staff> getStaffList() {
		return driver.getStaff();
	}

	public void updateStaff(Staff s) {
		driver.updateStaff(s);
	}

	public void deleteStaff(Staff s) {	
		driver.deleteStaff(s);
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
	
	public ArrayList<TransactionHistory> getTransactionHistory() {
		return driver.getTransactionHistory();
	}
	
	public class SalesListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			double dailyAmount = 0.0;
			
			for(TransactionHistory h : driver.getTransactionHistory()) {
				if ( h.isToday() == true) {
					dailyAmount += h.getAmount();
				}
			}
			dailyTotal.setText("Daily Sales: " + NumberFormat.getCurrencyInstance().format(dailyAmount));
			System.out.println("Updates Daily Sales Figure");

		}
	}
	
	public void startDailySales() {
		dailySalesTimer.start();
	}
	
	
	public JPanel getMainPanel() {
		return mainPanel;
	}
}
