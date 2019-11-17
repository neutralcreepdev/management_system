package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import panels.DeliveryPanel;
import panels.SearchPanel;
import panels.TaskBar;
import resources.Delivery;
import resources.Grocery;
import resources.JRoundedFormattedTextField;
import resources.JRoundedTextField;
import resources.Staff;

// This is the main view page for the user's interaction
public class InteractionView  extends JPanel{
	
	SearchPanel sp;
	DeliveryPanel dp;
	TaskBar tb;
	ArrayList<Grocery> Groceries;
	JButton refreshButton, addStockButton;
	Driver driver;

	
	public InteractionView (Driver newDriver) {

		driver = newDriver;
		sp = new SearchPanel(this);
		dp = new DeliveryPanel(this);
		tb = new TaskBar(this);

		setLayout(null);
		refreshButton = new JButton("Refresh Page");
		refreshButton.setBounds(690, 100, 120, 20);
		addStockButton = new JButton("Add New Stock");
		addStockButton.setBounds(690, 140, 120, 20);
	    setSize(1525, 800);
		
	    refreshButton.addActionListener(new Refresh_AL());
	    addStockButton.addActionListener(new AddStock_AL());
	    add(refreshButton);
	    add(addStockButton);
		add(sp);
		add(dp);
		add(tb);
		
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
		driver.logout();
	}
	
	public void updateStock(Grocery updatedG, Grocery oldG) {
		driver.updateStock(updatedG, oldG);
	}
	
	public void deleteStock(Grocery g) {
		driver.deleteStock(g);
	}
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
	
	public ArrayList<Delivery> getAllDeliveries(){
		return driver.getAllDeliveries();
	}
	
	public class Refresh_AL implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			updateTables();
		}
	}
	
	public class AddStock_AL implements ActionListener {
		JRoundedTextField name, supplier, qty, price,description;
		
		public void actionPerformed(ActionEvent arg0) {
			
			JFrame addNewStock = new JFrame("Add New Stock");
			
			addNewStock.setResizable(false);
			addNewStock.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			addNewStock.setSize(400,500);
			addNewStock.setLayout(new GridLayout(0, 1, 50, 20));
			
			JLabel nameLabel, supplierLabel, quantityLabel, priceLabel, descriptionLabel;
	
			JButton submit = new JButton("Add");

			nameLabel = new JLabel("Name: ");
			supplierLabel = new JLabel("Supplier Name: ");
			descriptionLabel = new JLabel("Description");
			quantityLabel = new JLabel("Quantity: ");
			priceLabel = new JLabel("Price(SGD): ");
			name = new JRoundedTextField(5);
			supplier = new JRoundedTextField(5);
			qty = new JRoundedTextField(5);
			qty.setText("0");
			price = new JRoundedTextField(5);
			price.setText("0.00");
			description = new JRoundedTextField(5);
			
			submit.addActionListener(new submit_AL(addNewStock));
			
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
				
				//Error Handle the information
				boolean duplicateCheck = false, price_numberFormatError = false, qty_numberFormatError = false;
				boolean nameFieldStatus = false, suppFieldStatus = false, qtyFieldStatus = false, priceFieldStatus = false , descFieldStatus = false;
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
				if(description.getText().trim().isEmpty() == true)
					descFieldStatus = true;
				try {
					q = Long.parseLong(qty.getText().trim());
				} catch (NumberFormatException e1) {
					qty_numberFormatError = true;
				}
						
				try {
					p = Double.parseDouble(price.getText().trim());
				} catch (NumberFormatException e1) {
					price_numberFormatError = true;
				}
				
				if( nameFieldStatus == true|| suppFieldStatus == true || qtyFieldStatus == true || priceFieldStatus == true || descFieldStatus == true)
					JOptionPane.showMessageDialog(null, "1 or more fields are empty" , "Add New Stock", 2);
				else if (price_numberFormatError == true)
					JOptionPane.showMessageDialog(null, "ERROR! - Price is not a decimal number || Invalid Character Detected" , "Add New Stock", 0);	
				else if (qty_numberFormatError == true)
					JOptionPane.showMessageDialog(null, "ERROR! - Quantity cannot be a decimal || Invalid Character Detected" , "Add New Stock", 0);
				else if(p < 0.01)
					JOptionPane.showMessageDialog(null, "Price cannot be less than $0.01" , "Add New Stock", 0);
				else {
					for ( Grocery g: Groceries) {
						if ( g.getName().equalsIgnoreCase(name.getText().trim()) && g.getSupplier().equalsIgnoreCase(supplier.getText().trim()))
							duplicateCheck = true;
					}
					
					if(duplicateCheck == false) {
						//Generate new Grocery ID function
						Grocery g = new Grocery(name.getText(), supplier.getText(), generateNewGroceryID(), q, p, "", description.getText());
						
						//TODO:Add some logic to the Image URL generation here
						
						driver.addStock(g);
						//Subsequently refresh SearchPanel and  SearchTable
						driver.readData();
						sp.updateTable(driver.getGrocery());
						frame.dispose();
					} else
						JOptionPane.showMessageDialog(null, "Duplicate product!" , "Add New Stock", 0);
				}
				
			}
			
		}	
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
}
