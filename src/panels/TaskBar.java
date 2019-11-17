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
import java.awt.event.MouseListener;
import java.util.Calendar;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import main.InteractionView;
import resources.JRoundedTextField;
import resources.Staff;

public class TaskBar extends JPanel {

	private JLabel companyLabel, logout, history, addStaff, time;
	private boolean male = true, female = false;
	private InteractionView iv;
	JRadioButton maleRB = new JRadioButton("Male"), femaleRB = new JRadioButton("Female");
	JRoundedTextField fName = new JRoundedTextField(15), lName = new JRoundedTextField(15), 
			 address = new JRoundedTextField(15), eID = new JRoundedTextField(15), number = new JRoundedTextField(15), 
			 role = new JRoundedTextField(15), email = new JRoundedTextField(15),
			 dob = new JRoundedTextField(15);
	JButton okayButton = new JButton("Submit");
	
	public TaskBar(InteractionView iv) {
		
		setLayout(null); 
		setBounds(0, 0, 1510, 50);
		setBackground(Color.orange);
		companyLabel = new JLabel("Neutral Creep");
		logout = new JLabel("Logout");
		history = new JLabel("History");
		addStaff = new JLabel("Add Staff");
		
		companyLabel.setFont(new Font("sansserif", Font.PLAIN, 20));
		
		time = new JLabel("TIME: ");
		time.setFont(new Font("sansserif", Font.PLAIN, 12));

		companyLabel.setBounds(40, 10, 200, 30);
		time.setBounds(700, 10, 200, 30);
		addStaff.setBounds(1200, 10, 100, 30);
		history.setBounds(1310, 10, 100, 30);
		logout.setBounds(1400, 10, 100, 30);
		
		addStaff.addMouseListener(new MouseAdapter()  {  
		
			
		    public void mouseClicked(MouseEvent e)  { 
		    	JFrame addStaffFrame = new JFrame();
				System.out.println("Clicked on addStaff");
				addStaffFrame.setSize(400,500);
				addStaffFrame.setName("Add Staff");
				addStaffFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				JLabel  fNameLabel = new JLabel("First Name: "), lNameLabel = new JLabel("Last Name: "),
						addressLabel = new JLabel("Address: "), emailLabel = new JLabel("Email: "),
						contactNumberLabel = new JLabel("Number: "), dobLabel = new JLabel("DOB: "),
						eIDLabel = new JLabel("Employee ID: "), roleLabel = new JLabel("Role: "), 
						genderLabel = new JLabel("Gender");
				
				addStaffFrame.setLayout(new GridLayout(0,1));
				
				ButtonGroup genderSelectionGroup = new ButtonGroup();
				genderSelectionGroup.add(maleRB);
				genderSelectionGroup.add(femaleRB);
				okayButton.addActionListener(new okay_AL(addStaffFrame));
				maleRB.addItemListener(new gender_M_IL());
				femaleRB.addItemListener(new gender_F_IL());
				maleRB.setSelected(true);
				femaleRB.setSelected(false);
				
				addStaffFrame.add(fNameLabel);
				addStaffFrame.add(fName);
				addStaffFrame.add(lNameLabel);
				addStaffFrame.add(lName);
				addStaffFrame.add(addressLabel);
				addStaffFrame.add(address);
				addStaffFrame.add(emailLabel);
				addStaffFrame.add(email);
				addStaffFrame.add(contactNumberLabel);
				addStaffFrame.add(number);
				addStaffFrame.add(dobLabel);
				addStaffFrame.add(dob);
				addStaffFrame.add(eIDLabel);
				addStaffFrame.add(eID);
				addStaffFrame.add(roleLabel);
				addStaffFrame.add(role);
				addStaffFrame.add(genderLabel);
				addStaffFrame.add(maleRB);
				addStaffFrame.add(femaleRB);
				addStaffFrame.add(okayButton);
				
				
				addStaffFrame.setAlwaysOnTop(true);
				addStaffFrame.setVisible(true);
				
		    }  
		    
			class okay_AL implements ActionListener {
				JFrame addStaffFrame;
				
				public okay_AL(JFrame frame) {
					addStaffFrame = frame;
				}
				
				public void actionPerformed(ActionEvent e) {
					String gender;

					if (male == true) 
						gender = "Male";
					else 
						gender = "Female";
					
					Staff s = new Staff( fName.getText(), lName.getText(), address.getText(), email.getText(), eID.getText(), role.getText(), gender, dob.getText());
					addStaffFrame.dispose();
					iv.addStaff(s);
				}
				
			}
		}); 
		
		
		history.addMouseListener(new MouseAdapter() {  
		    public void mouseClicked(MouseEvent e)  {  
				System.out.println("Clicked on history");
		    }  
		}); 
		
		logout.addMouseListener(new MouseAdapter()  {  
		    public void mouseClicked(MouseEvent e)  {  
				iv.logout();

		    }  
		}); 
		
		add(time);
		add(companyLabel);
		add(logout);
		add(history);
		add(addStaff);
		javax.swing.Timer timer = new javax.swing.Timer(1000, new ClockListener());
		timer.start();

	}

	class ClockListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			Calendar now = Calendar.getInstance();
			int h = now.get(Calendar.HOUR_OF_DAY);
			int m = now.get(Calendar.MINUTE);
			int s = now.get(Calendar.SECOND);
			time.setText("TIME: " + h + ":" + m + ":" +s);

		}
	}
	public class gender_M_IL implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == e.SELECTED) {
				male = true;
				female = false;
				
			}
		}
	}

	public class gender_F_IL implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == e.SELECTED) {
				female = true;
				male = false;
				
			}
		}
	}
	

	

}
