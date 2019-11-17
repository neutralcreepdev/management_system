package panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import comparators.StaffSortByEmployeeID;
import comparators.StaffSortByName;
import comparators.StaffSortByRole;
import panels.SearchTable.delete_AL;
import panels.SearchTable.update_AL;
import resources.Staff;
import resources.Grocery;
import resources.JRoundedTextField;
import resources.Staff;

//http://www.java2s.com/Tutorial/Java/0240__Swing/CustomizingColumnHeaderswithIcons.htm

public class StaffTable extends JPanel implements MouseListener {

	JTable table;
	JScrollPane scrollPane;
	List<Staff> data;
	Staff currStaff;
	ModelData modelData;
	boolean eID = true, eFName = true, eLName = true, eRole = true;
	final JTableHeader header;
	private TaskBar tb;
	
	JRoundedTextField fName = new JRoundedTextField(1), lName = new JRoundedTextField(1), 
			address = new JRoundedTextField(1), contactNo = new JRoundedTextField(1),
			email = new JRoundedTextField(1);

	public StaffTable(TaskBar taskbar) {
		tb = taskbar;
		modelData = new ModelData();
		table = new JTable(modelData);
		currStaff = new Staff();

		scrollPane = new JScrollPane(table);
		table.setPreferredScrollableViewportSize(new Dimension(500, 400));

		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		table.setVisible(true);
		table.addMouseListener(this);
		add(scrollPane);

		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

		table.getColumn("Employee ID").setCellRenderer( leftRenderer);
		table.getColumn("First Name").setCellRenderer( leftRenderer );
		table.getColumn("Last Name").setCellRenderer( leftRenderer );
		table.getColumn("Role").setCellRenderer( leftRenderer );
		
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	    
		header = table.getTableHeader();

		// Doesn't allow swapping of columns
		header.setReorderingAllowed(false);

		// Adds a mouse listener
		header.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int column = header.columnAtPoint(e.getPoint());
				sort(column);
			}
		}); // End of header's mouseListener
	}

	public void sort(int column) {
		switch (column) {
		case 0 :
			Collections.sort(data, new StaffSortByEmployeeID());
			
			if (eID == false) 
				Collections.reverse(data);
			eID = !eID;
		break;
		
		case 1 : 
			Collections.sort(data, new StaffSortByName(0)); 	
				if (eFName == false)
					Collections.reverse(data);
			eFName = !eFName;
		break;
		
		case 2 : 
			Collections.sort(data, new StaffSortByName(1));
			if (eLName) 
				Collections.reverse(data);
			eLName = !eLName;
		break;
		
		case 3 :
			Collections.sort(data, new StaffSortByRole());
			
			if(eRole)
				Collections.reverse(data);
			eRole = !eRole;
			break;
		}

		//Update the display
		table.revalidate();
		table.repaint();
		System.out.println("Sorted Staff Table");

	}
	
	public Staff getSelectedStaff() {
		return currStaff;
	}

	public void mousePressed(MouseEvent e) {
		
		try {
			int row = table.getSelectedRow();

			currStaff = data.get(row);

				JFrame frame = new JFrame("Update Staff name");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setSize(400,500);
				frame.setResizable(false);
				frame.setLayout(new GridLayout(0, 1, 50, 20));
				
				JLabel fNameLabel, lNameLabel, contactNoLabel, addressLabel, emailLabel;
		
				JButton update = new JButton("Update");
				JButton delete = new JButton("Delete");
				
				fNameLabel = new JLabel("First Name: " + currStaff.getFirstName());
				lNameLabel = new JLabel("Last Name: " + currStaff.getLastName());
				addressLabel = new JLabel("Address: " + currStaff.getAddress());
				contactNoLabel = new JLabel("Contact Number: " + currStaff.getContactNumber());
				emailLabel = new JLabel("Email: " + currStaff.getEmail());
				
				fName = new JRoundedTextField(1);
				lName = new JRoundedTextField(1);
				address = new JRoundedTextField(1);
				contactNo = new JRoundedTextField(1);
				email = new JRoundedTextField(1);
				
				update.addActionListener(new update_AL(frame));
				delete.addActionListener(new delete_AL(frame));
				
				frame.add(fNameLabel);
				frame.add(fName);
				frame.add(lNameLabel);
				frame.add(lName);
				frame.add(addressLabel);
				frame.add(address);
				frame.add(contactNoLabel);
				frame.add(contactNo);
				frame.add(emailLabel);
				frame.add(email);
				frame.add(update);
				frame.add(delete);
				
				frame.setAlwaysOnTop(true);	
				frame.setVisible(true);
				
		} catch (ArrayIndexOutOfBoundsException e1) {

		}
	}
	
	class delete_AL implements ActionListener {
		JFrame frame;
				
		public delete_AL(JFrame temp) {
				frame = temp;
		}
				
		public void actionPerformed(ActionEvent e) {
			//Send a Delete Request To Firestore
			tb.deleteStaff(currStaff);
			frame.dispose();	
			int pos = 0;
			
			for ( Staff s : data) {
				if (s.getDocID() == currStaff.getDocID())
					break;
				
				pos++;
			}
			
			System.out.println("Current Staff DocID: " + currStaff.getDocID());
			System.out.println("Removing pos " + pos + " with Staff DocID " + data.get(pos).getDocID());
			
			data.remove(pos);
			table.revalidate();
			table.repaint();
		}
	}
	
	class update_AL implements ActionListener {
		JFrame frame;
			
		public update_AL(JFrame temp) {
			frame = temp;
		}
			
		public void actionPerformed(ActionEvent e) {
			
			Staff temp = new Staff();		
		
			int fieldChecker = 0;
			
			
			//Check if the textbox is empty
			// If empty == true Then set to default (old) value
			// Else set to the new updated value
			if(fName.getText().trim().isEmpty() == true) {
				fieldChecker++;
				temp.setFirstName(currStaff.getFirstName());
			} else
				temp.setFirstName(fName.getText().trim());	
			
			if(lName.getText().trim().isEmpty() == true) {
				fieldChecker++;
				temp.setLastName(currStaff.getLastName());
			} else 
				temp.setLastName(lName.getText().trim());
			
			if(address.getText().trim().isEmpty() == true) {
				fieldChecker++;
				temp.setAddress(currStaff.getAddress());
			} else 
				temp.setAddress(address.getText().trim());
			
			if(contactNo.getText().trim().isEmpty() == true) {
				fieldChecker++;
				temp.setContactNumber(currStaff.getContactNumber());
			} else 
				temp.setContactNumber(contactNo.getText().trim());
			
			if(email.getText().trim().isEmpty() == true) {
				fieldChecker++;
				temp.setEmail(currStaff.getEmail());
			} else
				temp.setEmail(email.getText().trim());
			
			if( fieldChecker == 5) {
				JOptionPane.showMessageDialog(null, "Fields are empty" , "Updating Staff Information", 2);
			} else {
			
			temp.setRole(currStaff.getRole());
			temp.setGender(currStaff.getGender());
			temp.setEmployeeID(currStaff.getEmployeeID());
			temp.setDOB(currStaff.getDOB());
			temp.setDocID(currStaff.getDocID());

			//Update the database
			tb.updateStaff(temp);				
			frame.dispose();
			int pos = 0;
			
			for ( Staff s : data) {
				if (s.getDocID() == currStaff.getDocID())
					break;
				
				pos++;
			}
			
			System.out.println("Current Staff DocID: " + currStaff.getDocID());
			System.out.println("Removing pos " + pos + " with Staff DocID " + data.get(pos).getDocID());
			
			data.remove(pos);
			data.add(temp);
			table.revalidate();
			table.repaint();

			
			}

		}
	}
	
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}

	public void updateTableStaff(List<Staff> d) {

		data = d;
		table.revalidate();
		table.repaint();
		
	    //table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	    
		System.out.println("Updated Staff Table");
	}
	
	//Edit table column width
	class ModelData extends AbstractTableModel {

		String colNames[] = { "Employee ID", "First Name", "Last Name", "Role"};
		Class<?> colClasses[] = { String.class,  String.class, String.class, String.class};

		// Constructor
		public ModelData() {
			data = new ArrayList<Staff>();
		}

		public int getRowCount() {
			return data.size();
		}

		public int getColumnCount() {
			return colNames.length;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			
			switch(columnIndex) {
				case 0: return data.get(rowIndex).getEmployeeID();
				case 1: return data.get(rowIndex).getFirstName(); 
				case 2: return data.get(rowIndex).getLastName();
				case 3: return data.get(rowIndex).getRole();
				default: return null;
			}
			
		}

		public String getColumnName(int columnIndex) {
			return colNames[columnIndex];
		}

		public Class<?> getColumnClass(int columnIndex) {
			return colClasses[columnIndex];
		}

	}

	class JComponentTableCellRenderer implements TableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			return (JComponent) value;
		}
	}
}

