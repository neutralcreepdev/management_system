import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;

import panels.DeliveryPanel;
import panels.SearchPanel;
import panels.TaskBar;
import resources.Delivery;
import resources.Grocery;

// This is the main view page for the user's interaction
public class InteractionView  extends JFrame{
	
	SearchPanel sp;
	DeliveryPanel dp;
	TaskBar tb;
	//Login l;
	
	JButton refreshButton;
	ArrayList<Grocery> Groceries;
	ArrayList<Delivery> Deliveries;
	
	public InteractionView (ArrayList<Grocery> g, ArrayList<Delivery> d) {
		Groceries = g;
		Deliveries = d;
		sp = new SearchPanel(g);
		dp = new DeliveryPanel(d);
		tb = new TaskBar();
		//l = new Login();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		
		refreshButton = new JButton("Update");
		refreshButton.setBounds(700, 100, 100, 20);
	    //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    setSize(1525, 800);
	    
	    //Initial Login View
	    //sp.setVisible(false);
	    //dp.setVisible(false);
	    //tb.setVisible(false);
	    //l.setVisible(true);
		
	    refreshButton.addActionListener(new Refresh_AL());
	    
	    add(refreshButton);
		add(sp);
		add(dp);
		add(tb);
		//add(l);
		setVisible(true);
	}
	
	//Transition method between login to POS
	//Login screen will toggle the view
	public void toggleLoginView(boolean status) {
		
		//if true, then show the rest of the screen
		sp.setVisible(status);
		dp.setVisible(status);
		tb.setVisible(status);
		//else show login screen only
		//l.setVisible(!status);
	}
	
	public class Refresh_AL implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			//Retrieve Latest Database Connection list
			//Subsequently refresh DeliveryPanel and GroceryPanel
		}
	}	
	
}
