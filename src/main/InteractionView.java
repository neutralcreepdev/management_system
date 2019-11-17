package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import panels.DeliveryPanel;
import panels.SearchPanel;
import panels.TaskBar;
import resources.Delivery;
import resources.Grocery;
import resources.JRoundedTextField;
import resources.Staff;

// This is the main view page for the user's interaction
public class InteractionView  extends JPanel{
	
	SearchPanel sp;
	DeliveryPanel dp;
	TaskBar tb;
	
	JButton refreshButton, addStockButton;
	ArrayList<Grocery> Groceries;
	ArrayList<Delivery> Deliveries;
	Driver driver;
	
	public InteractionView (Driver newDriver) {
		//Groceries = g;
		//Deliveries = d;
		driver = newDriver;
		sp = new SearchPanel(this);
		dp = new DeliveryPanel(this);
		tb = new TaskBar(this);

		setLayout(null);
		
		refreshButton = new JButton("Refresh Page");
		refreshButton.setBounds(690, 100, 120, 20);
		addStockButton = new JButton("Add New Stock");
		addStockButton.setBounds(690, 140, 120, 20);
	    //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    setSize(1525, 800);
		
	    refreshButton.addActionListener(new Refresh_AL());
	    addStockButton.addActionListener(new AddStock_AL());
	    add(refreshButton);
	    add(addStockButton);
		add(sp);
		add(dp);
		add(tb);
		setVisible(false);
	}
	
	public void setView() {
		sp.setVisible(true);
		dp.setVisible(true);
		tb.setVisible(true);
	}
	
	public void setData(ArrayList<Grocery> g, ArrayList<Delivery> d) {
		Groceries = g;
		Deliveries = d;
		sp.updateTable(g);
		dp.updateTable(d);
	}
	
	public void logout() {
		driver.logout();
	}
	
//	public Grocery getGroceryRow() {
//		return driver.getGroceryRow();
//	}
	public void addStaff(Staff s) {
		driver.addStaff(s);
	}
	
	public void updateTables() {
		//Retrieve Latest Database Connection list
		driver.readData();
		//Subsequently refresh DeliveryPanel and GroceryPanel
		sp.updateTable(driver.getGrocery());
		dp.updateTable(driver.getDelivery());

	}
	
	public class Refresh_AL implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			//Retrieve Latest Database Connection list
			driver.readData();
			//Subsequently refresh DeliveryPanel and GroceryPanel
			sp.updateTable(driver.getGrocery());
			dp.updateTable(driver.getDelivery());

		}
	}
	
	public class AddStock_AL implements ActionListener {
		JRoundedTextField name, supplier, itemID, qty, price; 
		public void actionPerformed(ActionEvent arg0) {

			JFrame addNewStock = new JFrame();
			//Write a new data to the firebase
			System.out.println("Clicked on addNewStock");

			addNewStock.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			addNewStock.setSize(400,500);
			addNewStock.setName("Add New Stock");
			addNewStock.setLayout(new GridLayout(0, 1, 50, 20));
			
			JLabel nameLabel, supplierLabel, itemIDLabel, quantityLabel, priceLabel;
	
			JButton submit = new JButton("Submit");
			nameLabel = new JLabel("Name: ");
			supplierLabel = new JLabel("Supplier Name: ");
			itemIDLabel = new JLabel("ItemID: ");
			quantityLabel = new JLabel("Quantity: ");
			priceLabel = new JLabel("Price(SGD): ");
			name = new JRoundedTextField(5);
			supplier = new JRoundedTextField(5);
			itemID = new JRoundedTextField(5);
			qty = new JRoundedTextField(5);
			price = new JRoundedTextField(5);
			
			submit.addActionListener(new submit_AL(addNewStock));
			
			addNewStock.add(nameLabel);
			addNewStock.add(name);
			addNewStock.add(supplierLabel);
			addNewStock.add(supplier);
			addNewStock.add(itemIDLabel);
			addNewStock.add(itemID);
			addNewStock.add(quantityLabel);
			addNewStock.add(qty);
			addNewStock.add(priceLabel);
			addNewStock.add(price);
			addNewStock.add(submit);
			
			addNewStock.setAlwaysOnTop(true);	
			addNewStock.setVisible(true);
			
		}
		class submit_AL implements ActionListener {
			JFrame frame;
			
			public submit_AL(JFrame temp) {
				frame = temp;
			}
			
			public void actionPerformed(ActionEvent e) {

				Grocery g = new Grocery(name.getText(), supplier.getText(), Long.parseLong(qty.getText()), Long.parseLong(itemID.getText()), Double.parseDouble(price.getText()));
				frame.dispose();
				driver.addStock(g);
				//Subsequently refresh GroceryPanel
				//Retrieve Latest Database Connection list
				driver.readData();
				//Subsequently refresh DeliveryPanel and GroceryPanel
				sp.updateTable(driver.getGrocery());
				//frame.revalidate();
				
			}
			
		}
	}	
	 
	
}
