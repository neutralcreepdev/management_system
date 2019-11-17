package panels;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.InteractionView;
import resources.Delivery;
import tables.DeliveryTable;

//Should sort by date intially
public class DeliveryPanel extends JPanel {
	
	private JLabel panelLabel, filterLabel;
	private JComboBox<String> filterComboBox;
	private String [] filteringCriteria = new String [] {"<None>" , "STATUS - WAITING" , "STATUS - PACKAGED", "STATUS - DELIVERING"};
	private DeliveryTable table;			
	InteractionView iv;
	
	public DeliveryPanel(InteractionView v) {

		iv = v;
		table = new DeliveryTable(iv.getMainPanel());
		
		setLayout(null);
		setBounds(50, 100, 610, 600);
		setBorder(BorderFactory.createEtchedBorder());
		setBackground(new Color(0xf2f5f6));
		
		panelLabel = new JLabel("<html><h2>Pending Deliveries</h2></html>");
		filterLabel = new JLabel("FILTER: ");
		filterComboBox = new JComboBox<>(filteringCriteria);
		
		panelLabel.setBounds(220, 60, 200, 25);
		filterLabel.setBounds(190, 100, 70, 20);
		filterComboBox.setBounds(240, 100, 150, 20);
		table.setBounds(50, 130, 510, 450);
	
		add(panelLabel);
		add(filterLabel);
		add(filterComboBox);
		add(table);
		
		filterComboBox.addItemListener(new filter_IL());
		
	}

	public void updateTable(ArrayList<Delivery> d) {
		table.updateTableDelivery(d);
	}
	
	//Attach a filter listener
	//This one should be more of extracting values based on the filter
	public class filter_IL implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			//Waiting, Packaging, Packaged, Delivery, Delivered/Late
			if ( e.getItem().equals("<None>") && e.getStateChange() == ItemEvent.SELECTED) {
				table.reset();
				table.sort(0);
			} else if ( e.getItem().equals("STATUS - WAITING")   && e.getStateChange() == ItemEvent.SELECTED) {
				table.filter("Waiting");
			} else if ( e.getItem().equals("STATUS - PACKAGED")   && e.getStateChange() == ItemEvent.SELECTED) {
			 	table.filter("Packaged");
			} else if ( e.getItem().equals("STATUS - DELIVERING")   && e.getStateChange() == ItemEvent.SELECTED) {
			 	table.filter("Delivering");
			}
		}	
	}	
	
}
