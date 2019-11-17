package panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import resources.Grocery;
import resources.JRoundedTextField;

public class SearchPanel extends JPanel {
	
	private JLabel panelLabel, searchLabel, filterLabel;
	private JComboBox<String> filterComboBox;
	private JRoundedTextField searchQueryField;
	private JButton searchButton; 
	private String [] filteringCriteria = new String [] {"NAME", "SUPPLIER" , "QUANTITY"};
	private SearchList table;

	public SearchPanel (ArrayList<Grocery> g) {
		
		table = new SearchList(true);
		
		setLayout(null);
		setBounds(850, 75, 610, 600);
		setBorder(BorderFactory.createEtchedBorder());
		
		panelLabel = new JLabel("<html><h2>Inventory Status</h2></html>");
		searchLabel = new JLabel("SEARCH: ");
		filterLabel = new JLabel("FILTER: ");
		filterComboBox = new JComboBox<>(filteringCriteria);
		
		searchQueryField = new JRoundedTextField(15);
		searchButton = new JButton("Search");
		
		
		panelLabel.setBounds(230, 60, 150, 25);
		searchLabel.setBounds(55, 100, 60, 20);
		searchQueryField.setBounds(110, 100, 100, 20);
		searchButton.setBounds(220, 100, 80, 20);
		filterLabel.setBounds(400, 100, 70, 20);
		filterComboBox.setBounds(455, 100, 100, 20);

		table.setBounds(50, 130, 510, 450);
		
		table.updateTableGrocery(g);
		
		add(panelLabel);
		add(searchLabel);
		add(filterLabel);
		add(filterComboBox);
		add(searchQueryField);
		add(searchButton);
		add(table); //Attach SearchList
		
		filterComboBox.addItemListener(new filter_IL());
		searchButton.addActionListener(new search_AL());
		
	}

	//Attach a filter listener
	public class filter_IL implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if ( e.getItem().equals("NAME") && e.getStateChange() == e.SELECTED)
				System.out.println("NAME SELECTED");//Sort the results by name
			else if ( e.getItem().equals("SUPPLIER")   && e.getStateChange() == e.SELECTED)
				System.out.println("SUPPLIER SELECTED"); //Sort the results by supplier
			else if ( e.getItem().equals("QUANTITY")   && e.getStateChange() == e.SELECTED)
				System.out.println("QTY SELECTED");//Sort the results by qty
		}
	}			
	
	//Attach the Search functions listener
	public class search_AL implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// Execute the search for said item, returning the results via a temp arraylist and updating the table
			
		}
	}		
}
