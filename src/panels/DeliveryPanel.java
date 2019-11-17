package panels;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.Delivery;

public class DeliveryPanel extends JPanel {
	
	private JLabel panelLabel, filterLabel;
	private JComboBox<String> filterComboBox;
	private String [] filteringCriteria = new String [] {"ORDER NO", "STATUS - DONE" , "STATUS - DELIVERING" , "STATUS - LATE", "STATUS - WAITING"};
	private SearchList table;
	
	public DeliveryPanel(ArrayList<Delivery> d) {
		
		table = new SearchList(false);
		
		setLayout(null);
		setBounds(50, 75, 610, 600);
		setBorder(BorderFactory.createEtchedBorder());
		
		panelLabel = new JLabel("<html><h2>Deliveries Today</h2></html>");
		filterLabel = new JLabel("FILTER: ");
		filterComboBox = new JComboBox<>(filteringCriteria);
		
		
		panelLabel.setBounds(220, 60, 150, 25);
		filterLabel.setBounds(190, 100, 70, 20);
		filterComboBox.setBounds(240, 100, 150, 20);
		table.setBounds(50, 130, 510, 450);
		
		table.updateTableDelivery(d);
		
		add(panelLabel);
		add(filterLabel);
		add(filterComboBox);
		add(table); //Attach the SearchList
		
		filterComboBox.addItemListener(new filter_IL());
		
	}

	//Attach a filter listener
	public class filter_IL implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if ( e.getItem().equals("ORDER NO") && e.getStateChange() == e.SELECTED)
				System.out.println("ORDER NO SELECTED");//Sort the results by name
			else if ( e.getItem().equals("STATUS - DONE")   && e.getStateChange() == e.SELECTED)
				System.out.println("DONE SELECTED"); //Sort the results by supplier
			else if ( e.getItem().equals("STATUS - DELIVERING")   && e.getStateChange() == e.SELECTED)
				System.out.println("DELIVERING SELECTED");//Sort the results by qty
			else if ( e.getItem().equals("STATUS - LATE")   && e.getStateChange() == e.SELECTED)
				System.out.println("LATE SELECTED");//Sort the results by qty
			else if ( e.getItem().equals("STATUS - WAITING")   && e.getStateChange() == e.SELECTED)
				System.out.println("LATE SELECTED");//Sort the results by qty
		}
	}			
		
}
