package panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import comparators.InventorySortByItemID;
import main.InteractionView;
import resources.Grocery;
import resources.JRoundedTextField;
import tables.SearchTable;

public class SearchPanel extends JPanel {
	
	private JLabel panelLabel, searchLabel, filterLabel;
	private JComboBox<String> filterComboBox;
	private JRoundedTextField searchQueryField;
	private JButton searchButton, resetButton; 
	private String [] filteringCriteria = new String [] {"<None>", "NAME", "SUPPLIER" , "QUANTITY"};
	private String filterCondition = "";
	private SearchTable table;
	private InteractionView iv;
	private JPanel mainPanel;

	public SearchPanel (InteractionView v) {
		iv = v;
		mainPanel = iv.getMainPanel();
		table = new SearchTable(this);
		
		setLayout(null);
		setBounds(850, 100, 610, 600);
		setBorder(BorderFactory.createEtchedBorder());
		setBackground(new Color(0xf2f5f6));
		
		panelLabel = new JLabel("<html><h2>Inventory Status</h2></html>");
		searchLabel = new JLabel("SEARCH: ");
		filterLabel = new JLabel("FILTER: ");
		filterComboBox = new JComboBox<>(filteringCriteria);
		
		searchQueryField = new JRoundedTextField(15);
		searchButton = new JButton("Search");
		resetButton = new JButton("Reset");
		
		panelLabel.setBounds(230, 60, 150, 25);
		searchLabel.setBounds(55, 100, 60, 20);
		searchQueryField.setBounds(110, 100, 100, 20);
		searchButton.setBounds(220, 100, 80, 20);
		filterLabel.setBounds(320, 100, 70, 20);
		filterComboBox.setBounds(370, 100, 100, 20);
		resetButton.setBounds(475, 100, 80, 20);
		table.setBounds(40, 130, 530, 450);
	
		add(resetButton);
		add(panelLabel);
		add(searchLabel);
		add(filterLabel);
		add(filterComboBox);
		add(searchQueryField);
		add(searchButton);
		add(table);
		
		filterComboBox.addItemListener(new filter_IL());
		searchButton.addActionListener(new search_AL());
		resetButton.addActionListener(new reset_AL());
		
	}
	
	public void updateStock(Grocery updatedG, Grocery oldG) {
		iv.updateStock(updatedG, oldG);
	}
	
	public void deleteStock(Grocery g)  {
		iv.deleteStock(g);
	}
	
	public void updateTable() {
		iv.updateTables();
	}
	
	public String uploadImagetoGCS(String fileName, String filePath) {
		return iv.uploadImagetoGCS(fileName, filePath);
	}
	
	public void updateTable(ArrayList<Grocery> g) {
		try{
			if(!g.isEmpty()) {
		
			Collections.sort(g, new InventorySortByItemID());
			Collections.reverse(g);
			table.updateTableGrocery(g);
			} else {
				System.out.println("Waiting for data...");
			}
		} catch (NullPointerException e) {}
	}
	
	public JPanel getMainPanel() {
		return mainPanel;
	}

	public class filter_IL implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			filterCondition = (String) e.getItem();
		}
	}			
	
	public class search_AL implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// Execute the search for said item, returning the results via a temp arraylist and updating the table
			
			if (searchQueryField.getText().trim().isEmpty())
				JOptionPane.showMessageDialog(mainPanel, "Search Field is Empty!" , "Search Field", 2);
			else
				table.filter(filterCondition, searchQueryField.getText());
		}
	}
	
	public class reset_AL implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			filterComboBox.setSelectedIndex(0);
			iv.updateTables();
		}
	}		
}
