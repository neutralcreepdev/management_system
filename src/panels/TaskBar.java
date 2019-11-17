package panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import main.InteractionView;

import resources.JRoundedTextField;
import resources.Staff;

public class TaskBar extends JPanel {

	private UtilDateModel model;
	private JDatePanelImpl datePanel;
	private JDatePickerImpl datePicker;
	private ArrayList<Staff> Staffs;
	private InteractionView iv;
	private	StaffTable sTable;
	
	private boolean male = true, packer = true;

	private JLabel companyLabel, logout, deliveryHistory, staffMgmt, time, tHistoryLabel;
	private JButton okayButton = new JButton("Submit");
	private JRadioButton maleRB = new JRadioButton("Male"), femaleRB = new JRadioButton("Female");
	private JRadioButton packerRB = new JRadioButton("Packer"), deliveryRB = new JRadioButton("Delivery");
	private JRoundedTextField fName, lName, address, number, role, email;

	public TaskBar(InteractionView iv) {
		
		TaskBar tb = this;

		this.iv = iv;
		setLayout(null);
		setBounds(0, 0, 1520, 50);
		setBackground(new Color(0xecebe4));
		//d4d2c7

		companyLabel = new JLabel("Neutral Creep");
		logout = new JLabel("Logout");
		deliveryHistory = new JLabel("Delivery History");
		staffMgmt = new JLabel("Staff Management");
		tHistoryLabel = new JLabel("Transaction History");
		
		companyLabel.setFont(new Font("sansserif", Font.PLAIN, 20));

		time = new JLabel("TIME: ");
		time.setFont(new Font("sansserif", Font.PLAIN, 12));

		tHistoryLabel.setBounds(1140, 10, 200, 30);
		companyLabel.setBounds(40, 10, 200, 30);
		time.setBounds(700, 10, 200, 30);
		staffMgmt.setBounds(1000, 10, 150, 30);
		deliveryHistory.setBounds(1280, 10, 100, 30);
		logout.setBounds(1400, 10, 100, 30);

		//TRANSACTION HISTORY
		tHistoryLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(null, "This section is not available yet" , "Transaction History", 2);	
/*				JFrame transactionFrame = new JFrame("Transaction History");
				TransactionHistoryTable thTable = new TransactionHistoryTable(tb);

				JLabel panelLabel = new JLabel("<html><h1>TRANSACTION HISTORY</h1></html>");
				
				transactionFrame.setSize(1000, 650);
				transactionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				transactionFrame.setLayout(null);
		
				thTable.setBorder(BorderFactory.createEtchedBorder());
				transactionFrame.setBounds(50, 75, 610, 600);
				panelLabel.setBounds(160, 30, 300, 25);
				thTable.setBounds(40, 100, 510, 450);
				transactionFrame.setBounds(422, 75, 125, 20);
			
				transactionFrame.add(panelLabel);
				transactionFrame.add(sTable);
				transactionFrame.setAlwaysOnTop(true);
				transactionFrame.setVisible(true);
				
				//Staffs = iv.getStaffList();
				//sTable.updateTableStaff(Staffs);
*/			}
		});
		
		
		//STAFF MANAGEMENT
		staffMgmt.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				JFrame staffFrame = new JFrame("Staff Management");
				sTable = new StaffTable(tb);
				JButton addStaff = new JButton("Add New Staff");
				JLabel panelLabel = new JLabel("<html><h1>STAFF MANAGEMENT</h1></html>");
				
				staffFrame.setSize(1000, 650);
				staffFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				staffFrame.setLayout(null);
		
				sTable.setBorder(BorderFactory.createEtchedBorder());
				staffFrame.setBounds(50, 75, 610, 600);
				panelLabel.setBounds(160, 30, 300, 25);
				sTable.setBounds(40, 100, 510, 450);
				addStaff.setBounds(422, 75, 125, 20);
				
				addStaff.addActionListener(new addStaff_AL(sTable));
			
				staffFrame.add(panelLabel);
				staffFrame.add(sTable);
				staffFrame.add(addStaff);
				staffFrame.setAlwaysOnTop(true);
				staffFrame.setVisible(true);
				Staffs = iv.getStaffList();
				sTable.updateTableStaff(Staffs);
			}

		});

		//DELIVERY HISTORY
		deliveryHistory.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// Display all the delivery History on a Table in a new Frame

				JFrame dhFrame = new JFrame("Delivery History");
				dhFrame.setLayout(null);
				dhFrame.setResizable(false);
				dhFrame.setSize(600, 650);
				dhFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				JLabel panelLabel, filterLabel;
				JComboBox<String> filterComboBox;
				String[] filteringCriteria = new String[] { "", "ORDER NO", "STATUS - DONE", "STATUS - DELIVERING",
						"STATUS - LATE", "STATUS - WAITING" };

				panelLabel = new JLabel("<html><h1>All Deliveries</h1></html>");
				filterLabel = new JLabel("FILTER: ");
				filterComboBox = new JComboBox<>(filteringCriteria);

				DeliveryTable table = new DeliveryTable();
				
				filterLabel.setBounds(190, 50, 70, 20);
				filterComboBox.setBounds(240, 50, 150, 20);
				panelLabel.setBounds(200, 5, 200, 30);
				table.setBounds(10, 100, 560, 450);
				table.setBorder(BorderFactory.createEtchedBorder());
				
				filterComboBox.addItemListener(new filter_IL(table));

				table.updateTableDelivery(iv.getAllDeliveries());

				dhFrame.add(filterLabel);
				dhFrame.add(filterComboBox);
				dhFrame.add(panelLabel);
				dhFrame.add(table); // Attach the SearchList

				dhFrame.setAlwaysOnTop(true);
				dhFrame.setVisible(true);
			}

		});

		//LOGOUT
		logout.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				iv.logout();

			}
		});

		add(time);
		add(companyLabel);
		add(logout);
		add(deliveryHistory);
		add(staffMgmt);
		add(tHistoryLabel);
		javax.swing.Timer timer = new javax.swing.Timer(1000, new ClockListener());
		timer.start();

	}

	public class addStaff_AL implements ActionListener {
		private StaffTable table; 
		public addStaff_AL(StaffTable t) {
			table = t;
		}

		public void actionPerformed(ActionEvent arg0) {

			model = new UtilDateModel();
			model.setDate(2019,9,9);
			Properties p = new Properties();
			p.put("text.today", "Today");
			p.put("text.month", "Month");
			p.put("text.year", "Year");

			datePanel = new JDatePanelImpl(model, p);
			datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

			JFrame staffMgmtFrame = new JFrame("Add Staff");
			JPanel genderPanel = new JPanel(), rolePanel = new JPanel();
			JLabel fNameLabel = new JLabel("First Name: "), lNameLabel = new JLabel("Last Name: "),
					addressLabel = new JLabel("Address: "), emailLabel = new JLabel("Email: "),
					contactNumberLabel = new JLabel("Number: "), dobLabel = new JLabel("DOB: "),
					roleLabel = new JLabel("Role: "), genderLabel = new JLabel("Gender: ");			
			fName = new JRoundedTextField(15);
			lName = new JRoundedTextField(15); 
			address = new JRoundedTextField(15);
			number = new JRoundedTextField(15);
			role = new JRoundedTextField(15);
			email = new JRoundedTextField(15);
			
			staffMgmtFrame.setSize(340, 440);
			staffMgmtFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			staffMgmtFrame.setLayout(null);
				
				fNameLabel.setBounds(15, 10, 80, 20);
				fName.setBounds(100, 5, 150, 30);
				lNameLabel.setBounds(15, 50, 80, 20);
				lName.setBounds(100, 45, 150, 30);
				addressLabel.setBounds(15, 90, 80, 20);
				address.setBounds(100, 85, 150, 30);
				emailLabel.setBounds(15, 130, 80, 20);
				email.setBounds(100, 125, 150, 30);
				contactNumberLabel.setBounds(15, 170, 80, 20);
				number.setBounds(100, 165, 150, 30);
				dobLabel.setBounds(15, 220, 50, 20);
				
				datePicker.setBounds(15, 250, 150, 30);
				rolePanel.setBounds(15, 280, 100, 50);
				genderPanel.setBounds(200, 280, 100, 50);
				okayButton.setBounds(110, 350, 100, 30);
				
				
				genderPanel.setLayout(new GridLayout(3,0));
				genderPanel.add(genderLabel);
				genderPanel.add(maleRB);
				genderPanel.add(femaleRB);
				
				rolePanel.setLayout(new GridLayout(3,0));
				rolePanel.add(roleLabel);
				rolePanel.add(packerRB);
				rolePanel.add(deliveryRB);

				ButtonGroup genderRBGroup = new ButtonGroup();
				genderRBGroup.add(maleRB);
				genderRBGroup.add(femaleRB);
				
				ButtonGroup roleRBGroup = new ButtonGroup();
				roleRBGroup.add(packerRB);
				roleRBGroup.add(deliveryRB);
				
				
				okayButton.addActionListener(new okay_AL(staffMgmtFrame, table));
				
				maleRB.addItemListener(new gender_M_IL());
				femaleRB.addItemListener(new gender_F_IL());
				packerRB.addItemListener(new role_Packer_IL());
				deliveryRB.addItemListener(new role_Delivery_IL());
				packerRB.setSelected(true);
				deliveryRB.setSelected(false);
				maleRB.setSelected(true);
				femaleRB.setSelected(false);			

				staffMgmtFrame.add(fNameLabel);
				staffMgmtFrame.add(fName);				
				staffMgmtFrame.add(lNameLabel);
				staffMgmtFrame.add(lName);
				staffMgmtFrame.add(addressLabel);
				staffMgmtFrame.add(address);
				staffMgmtFrame.add(emailLabel);
				staffMgmtFrame.add(email);
				staffMgmtFrame.add(contactNumberLabel);
				staffMgmtFrame.add(number);
				staffMgmtFrame.add(dobLabel);
				staffMgmtFrame.add(genderPanel);
				staffMgmtFrame.add(rolePanel);
				staffMgmtFrame.add(okayButton);
				
				staffMgmtFrame.add(datePicker);
				
				staffMgmtFrame.setAlwaysOnTop(true);
				staffMgmtFrame.setVisible(true);

		}

	}

	public class filter_IL implements ItemListener {
		DeliveryTable table;

		public filter_IL(DeliveryTable t) {
			table = t;
		}

		public void itemStateChanged(ItemEvent e) {

			if (e.getItem().equals("") && e.getStateChange() == e.SELECTED) {
				table.reset();
				System.out.println("ALL");
			} else if (e.getItem().equals("ORDER NO") && e.getStateChange() == e.SELECTED) {
				System.out.println("ORDER NO SELECTED");
				table.reset();
				table.sort(0);
			} else if (e.getItem().equals("STATUS - DONE") && e.getStateChange() == e.SELECTED)
				table.filter("done");
			else if (e.getItem().equals("STATUS - DELIVERING") && e.getStateChange() == e.SELECTED)
				table.filter("delivering");
			else if (e.getItem().equals("STATUS - LATE") && e.getStateChange() == e.SELECTED)
				table.filter("late");
			else if (e.getItem().equals("STATUS - WAITING") && e.getStateChange() == e.SELECTED) {
				table.filter("waiting");
				System.out.println("Clicked on - waiting");
			}
		}

	}

	public class okay_AL implements ActionListener {
		JFrame staffMgmtFrame;
		private StaffTable table; 

		public okay_AL(JFrame frame, StaffTable t) {
			staffMgmtFrame = frame;
			table = t;
		}

		public void actionPerformed(ActionEvent e) {
			String gender, role, date = "";

			if (male == true)
				gender = "Male";
			else
				gender = "Female";

			if (packer == true) 
				role = "Packer";
			else
				role = "Delivery";
			
			//boolean fNameCheck = true, lNameCheck = true, addressCheck = true, emailCheck = true, dobCheck = true;
			int counter = 0;

			if (fName.getText().trim().isEmpty())
				counter++;
			if (lName.getText().trim().isEmpty())
				counter++;
			if(address.getText().trim().isEmpty())
				counter++;
			if(email.getText().trim().isEmpty())
				counter++;
			if(number.getText().trim().isEmpty())
				counter++;
			if (datePicker.getJFormattedTextField().getText().isEmpty())
				counter++;
			else 
				date = datePicker.getJFormattedTextField().getText();
					
			
			if (counter > 0) {
				JOptionPane.showMessageDialog(null, "1 or more fields are empty" , "Add New Staff", 2);	
			} else {

				Staff s = new Staff(fName.getText(), lName.getText(), address.getText(), email.getText(), generateNewStaffID(), role, gender, date, number.getText(), "");
				iv.addStaff(s);
				staffMgmtFrame.dispose();
				table.updateTableStaff(iv.getStaffList());
			}
		}

	}
	
	public class ClockListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			Calendar now = Calendar.getInstance();
			int h = now.get(Calendar.HOUR_OF_DAY);
			int m = now.get(Calendar.MINUTE);
			int s = now.get(Calendar.SECOND);
			time.setText("TIME: " + h + ":" + m + ":" + s);

		}
	}

	public class gender_M_IL implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == e.SELECTED) {
				male = true;

			}
		}
	}

	public class gender_F_IL implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == e.SELECTED) {
				male = false;

			}
		}
	}

	public class role_Packer_IL implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == e.SELECTED) {
				packer = true;

			}
		}
	}

	public class role_Delivery_IL implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == e.SELECTED) {
				packer = false;

			}
		}
	}

	public class DateLabelFormatter extends AbstractFormatter {
		 
	    private String datePattern = "dd/MM/yyyy";
	    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
	     
	    @Override
	    public Object stringToValue(String text) throws ParseException {
	    	System.out.println("text" + text);
	        return dateFormatter.parseObject(text);
	    }
	 
	    @Override
	    public String valueToString(Object value) throws ParseException {
	        if (value != null) {
	            Calendar cal = (Calendar) value;
	            return dateFormatter.format(cal.getTime());
	        }
	         
	        return "";
	    }
	 
	}

	public String generateNewStaffID() {
		String id = "" + (Staffs.size());
		System.out.println("ID: " + id);
		
		for (int i = 0; i < 8; i++) {
			if ( id.length() < 8) {
				id = "0" + id;
			}
		}
		
		return id;
	}
	
	public void deleteStaff(Staff s) {
		iv.deleteStaff(s);
	}
	
	public void updateStaff(Staff s) {
		iv.updateStaff(s);
	}

	
}