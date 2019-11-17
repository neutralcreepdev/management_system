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
import tables.DeliveryTable;
import tables.StaffTable;
import tables.TransactionHistoryTable;

@SuppressWarnings("serial")
public class TaskBar extends JPanel {

	private UtilDateModel model;
	private JDatePanelImpl datePanel;
	private JDatePickerImpl datePicker;
	private ArrayList<Staff> Staffs;
	private InteractionView iv;
	private	StaffTable sTable;
	private JPanel mainPanel;
	private boolean male = true, packer = true;

	private JLabel companyLabel, logout, deliveryHistory, staffMgmt, time, tHistoryLabel;
	private JButton staffSubmitButton = new JButton("<html><h2>Submit</h2></html>");
	private JRadioButton maleRB = new JRadioButton("Male"), femaleRB = new JRadioButton("Female");
	private JRadioButton packerRB = new JRadioButton("Packer"), deliveryRB = new JRadioButton("Delivery");
	private JRoundedTextField fName, lName, street, postal, unit, number, email;

	public TaskBar(InteractionView iv) {

		TaskBar tb = this;
		this.iv = iv;
		mainPanel = iv.getMainPanel();

		setLayout(null);
		setBounds(0, 0, 1520, 50);
		setBackground(Color.orange);

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
				JFrame transactionFrame = new JFrame("Transaction History");
				TransactionHistoryTable thTable = new TransactionHistoryTable(tb);

				JLabel panelLabel = new JLabel("<html><h1>TRANSACTION HISTORY</h1></html>");

				transactionFrame.setSize(600, 650);
				transactionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				transactionFrame.setLayout(null);

				thTable.setBorder(BorderFactory.createEtchedBorder());
				panelLabel.setBounds(160, 30, 300, 25);
				thTable.setBounds(25, 100, 530, 450);

				transactionFrame.add(panelLabel);
				transactionFrame.add(thTable);

				transactionFrame.setVisible(true);

				thTable.updateTable(iv.getTransactionHistory());
			}
		});

		//STAFF MANAGEMENT
		staffMgmt.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				JFrame staffFrame = new JFrame("Staff Management");
				sTable = new StaffTable(tb);
				JButton addStaff = new JButton("Add New Staff");
				JLabel panelLabel = new JLabel("<html><h1>STAFF MANAGEMENT</h1></html>");

				staffFrame.setSize(1000, 700);
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
				String[] filteringCriteria = new String[] { "<None>", "STATUS - DELIVERED", "STATUS - LATE" };

				panelLabel = new JLabel("<html><h1>All Deliveries</h1></html>");
				filterLabel = new JLabel("FILTER: ");
				filterComboBox = new JComboBox<>(filteringCriteria);

				DeliveryTable table = new DeliveryTable(mainPanel);

				filterLabel.setBounds(190, 50, 70, 20);
				filterComboBox.setBounds(240, 50, 150, 20);
				panelLabel.setBounds(200, 5, 200, 30);
				table.setBounds(10, 100, 560, 450);
				table.setBorder(BorderFactory.createEtchedBorder());

				filterComboBox.addItemListener(new filter_IL(table));
				
				table.updateTableDelivery(iv.getPastDeliveries());

				dhFrame.add(filterLabel);
				dhFrame.add(filterComboBox);
				dhFrame.add(panelLabel);
				dhFrame.add(table);
				
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
			model.setDate(1994,9,9);
			Properties p = new Properties();
			p.put("text.today", "Today");
			p.put("text.month", "Month");
			p.put("text.year", "Year");

			datePanel = new JDatePanelImpl(model, p);
			datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

			JFrame staffMgmtFrame = new JFrame("Add Staff");
			JPanel genderPanel = new JPanel(), rolePanel = new JPanel(), addressPanel = new JPanel();
			JLabel fNameLabel = new JLabel("First Name: "), lNameLabel = new JLabel("Last Name: "), 
					emailLabel = new JLabel("Email: "),contactNumberLabel = new JLabel("Number: "), 
					dobLabel = new JLabel("DOB: "), roleLabel = new JLabel("Role: "), genderLabel = new JLabel("Gender: "),			
					streetLabel = new JLabel("Street: "), postalLabel = new JLabel("Postal Code: "), unitLabel = new JLabel("Unit (#): ");

			fName = new JRoundedTextField(15);
			lName = new JRoundedTextField(15); 
			street = new JRoundedTextField(15);
			postal  = new JRoundedTextField(15);
			unit  = new JRoundedTextField(15);
			number = new JRoundedTextField(15);
			email = new JRoundedTextField(15);

			staffMgmtFrame.setSize(340, 540);
			staffMgmtFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			staffMgmtFrame.setLayout(null);

			fNameLabel.setBounds(15, 10, 80, 20);
			fName.setBounds(100, 5, 150, 30);
			lNameLabel.setBounds(15, 50, 80, 20);
			lName.setBounds(100, 45, 150, 30);

			streetLabel.setBounds(5, 28, 80, 20);
			street.setBounds(85, 20, 150, 30);
			postalLabel.setBounds(5, 68, 100, 20);
			postal.setBounds(85, 65, 100, 30);
			unitLabel.setBounds(5, 108, 80, 20);
			unit.setBounds(85, 105, 100, 30);

			emailLabel.setBounds(15, 253, 80, 20);
			email.setBounds(100, 250, 150, 30);
			contactNumberLabel.setBounds(15, 290, 80, 20);
			number.setBounds(100, 285, 150, 30);
			dobLabel.setBounds(15, 335, 50, 20);
			datePicker.setBounds(60, 330, 150, 30);
			rolePanel.setBounds(15, 365, 100, 50);
			genderPanel.setBounds(200, 365, 100, 50);
			staffSubmitButton.setBounds(80, 440, 150, 50);
			staffSubmitButton.setBackground(Color.GREEN);

			genderPanel.setLayout(new GridLayout(3,0));
			genderPanel.add(genderLabel);
			genderPanel.add(maleRB);
			genderPanel.add(femaleRB);

			rolePanel.setLayout(new GridLayout(3,0));
			rolePanel.add(roleLabel);
			rolePanel.add(packerRB);
			rolePanel.add(deliveryRB);

			addressPanel.setLayout(null);
			addressPanel.setSize(280, 150);
			addressPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Address"));

			addressPanel.setBounds(15, 85, 280, 150);
			addressPanel.add(streetLabel);
			addressPanel.add(street);
			addressPanel.add(postalLabel);
			addressPanel.add(postal);
			addressPanel.add(unitLabel);
			addressPanel.add(unit);

			ButtonGroup genderRBGroup = new ButtonGroup();
			genderRBGroup.add(maleRB);
			genderRBGroup.add(femaleRB);

			ButtonGroup roleRBGroup = new ButtonGroup();
			roleRBGroup.add(packerRB);
			roleRBGroup.add(deliveryRB);	

			staffSubmitButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					String gender, role, date = "";
					boolean numberCheck = false;

					if (male == true)
						gender = "Male";
					else
						gender = "Female";

					if (packer == true) 
						role = "Packer";
					else
						role = "Delivery";

					int counter = 0;

					if (fName.getText().trim().isEmpty())
						counter++;
					if (lName.getText().trim().isEmpty())
						counter++;		
					if(postal.getText().trim().isEmpty())
						counter++;
					if(street.getText().trim().isEmpty()) 
						counter++;	
					if(email.getText().trim().isEmpty())
						counter++;
					if(number.getText().trim().isEmpty())
						counter++;
					else {
						try {
							numberCheck = true;
							Long.parseLong(number.getText());
						} catch (NumberFormatException ex) {	
							numberCheck = false;
						}
					}
					if (datePicker.getJFormattedTextField().getText().isEmpty())
						counter++;
					else 
						date = datePicker.getJFormattedTextField().getText();


					if (counter > 0) {
						JOptionPane.showMessageDialog(mainPanel, "1 or more fields are empty" , "Add New Staff", 2);	
					} else if ( number.getText().length() < 8) { 
						JOptionPane.showMessageDialog(mainPanel, "Phone Number is too short" , "Add New Staff", 2);	
					} else if ( number.getText().length() > 8 ) {
						JOptionPane.showMessageDialog(mainPanel, "Phone Number is too long" , "Add New Staff", 2);
					} else if (email.getText().contains("@") == false || email.getText().contains(".com") == false  ){
						JOptionPane.showMessageDialog(mainPanel, "Invalid Email Address Format ( address@server.com )" , "Add New Staff", 2);
					} else if(numberCheck == false) 
						JOptionPane.showMessageDialog(mainPanel, "Enter a valid Singapore Number" , "Add New Staff", 2);	
					else {

						Staff s = new Staff(fName.getText().trim(), lName.getText().trim(), email.getText().trim(), role, gender, date, number.getText().trim(), "", "");
						s.setStreet(street.getText());
						s.setPostal(postal.getText());

						if(unit.getText().trim().isEmpty())
							s.setUnit("");
						else
							s.setUnit(unit.getText());	

						iv.createStaff(s);
						staffMgmtFrame.dispose();
						table.updateTableStaff(iv.getStaffList());
					}
				}


			});

			maleRB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						male = true;
					}
				}
			});
			femaleRB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						male = false;
					}
				}
			});
			packerRB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						packer = true;
					}
				}
			});
			deliveryRB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						packer = false;
					}
				}
			});

			//Initialize the RadioButton State
			packerRB.setSelected(true);
			deliveryRB.setSelected(false);
			maleRB.setSelected(true);
			femaleRB.setSelected(false);			

			staffMgmtFrame.add(fNameLabel);
			staffMgmtFrame.add(fName);				
			staffMgmtFrame.add(lNameLabel);
			staffMgmtFrame.add(lName);
			staffMgmtFrame.add(addressPanel);
			staffMgmtFrame.add(emailLabel);
			staffMgmtFrame.add(email);
			staffMgmtFrame.add(contactNumberLabel);
			staffMgmtFrame.add(number);
			staffMgmtFrame.add(dobLabel);
			staffMgmtFrame.add(genderPanel);
			staffMgmtFrame.add(rolePanel);
			staffMgmtFrame.add(staffSubmitButton);		
			staffMgmtFrame.add(datePicker);

			staffMgmtFrame.setVisible(true);
		}
	}

	public class filter_IL implements ItemListener {
		DeliveryTable table;

		public filter_IL(DeliveryTable t) {
			table = t;
		}

		public void itemStateChanged(ItemEvent e) {

			if ((e.getItem().equals("") || e.getItem().equals("<None>")) && e.getStateChange() == ItemEvent.SELECTED) {
				table.reset();
			} else if (e.getItem().equals("STATUS - DELIVERED") && e.getStateChange() == ItemEvent.SELECTED)
				table.filter("delivered");
			else if (e.getItem().equals("STATUS - LATE") && e.getStateChange() == ItemEvent.SELECTED)
				table.filter("delivered (late)");
			
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

	public class DateLabelFormatter extends AbstractFormatter {

		private String datePattern = "dd/MM/yyyy";
		private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

		@Override
		public Object stringToValue(String text) throws ParseException {
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

	public void deleteStaff(Staff s) {
		iv.deleteStaff(s);
	}

	public void updateStaff(Staff s) {
		iv.updateStaff(s);
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	//Not in use
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


}