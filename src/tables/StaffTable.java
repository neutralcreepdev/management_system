package tables;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
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
import panels.TaskBar;
import resources.JRoundedTextField;
import resources.Staff;


//http://www.java2s.com/Tutorial/Java/0240__Swing/CustomizingColumnHeaderswithIcons.htm
//Need to update the update function to add in the docID for each call. if not usage overtime will cause arraylist to hold some docID and some not holding
public class StaffTable extends JPanel implements MouseListener {

	JTable table;
	JScrollPane scrollPane;
	List<Staff> data;
	Staff currStaff;
	ModelData modelData;
	boolean eID = true, eFName = true, eLName = true, eRole = true;
	final JTableHeader header;
	private TaskBar tb;
	JRoundedTextField fName, lName, address, contactNo,street, postal, unit, number;

	public StaffTable(TaskBar taskbar) {
		tb = taskbar;
		modelData = new ModelData();
		table = new JTable(modelData);
		currStaff = new Staff();

		scrollPane = new JScrollPane(table);
		//table.setPreferredScrollableViewportSize(new Dimension(500, 400));
		table.setAutoscrolls(true);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		table.setVisible(true);
		table.addMouseListener(this);
		add(scrollPane);

		DefaultTableCellRenderer middleRenderer = new DefaultTableCellRenderer();
		middleRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		table.getColumn("ID").setCellRenderer( middleRenderer);
		table.getColumn("First Name").setCellRenderer( middleRenderer );
		table.getColumn("Last Name").setCellRenderer( middleRenderer );
		table.getColumn("Role").setCellRenderer( middleRenderer );

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
		JFrame frame;
		if(e.getClickCount() % 2 == 0) {
			try {
	
				int row = table.getSelectedRow();
				currStaff = data.get(row);
	
				frame = new JFrame("Update Staff Profile");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setSize(320,590);
				frame.setResizable(false);
				frame.setLayout(null);
	
				//Set color
				JButton update = new JButton("<html><h3>UPDATE</h3></html>");
				update.setBackground(new Color(0x62f56e));
				JButton delete = new JButton("<html><h3>DELETE</h3></html>");
				delete.setBackground(new Color(0xf58c8c));
			
	
				JLabel fNameLabel = new JLabel("First Name: " + currStaff.getFirstName()), 
						lNameLabel = new JLabel("Last Name: " + currStaff.getLastName()), 
						contactNoLabel = new JLabel("Contact #: " + currStaff.getContactNumber()) , 
						streetLabel = new JLabel("Street: " + currStaff.getStreet()),  postalLabel = new JLabel("Postal Code: " + currStaff.getPostal()), 
						unitLabel = new JLabel("Unit (#): " + currStaff.getUnit());
	
				fName = new JRoundedTextField(15);
				lName = new JRoundedTextField(15);
				address = new JRoundedTextField(15); 
				contactNo = new JRoundedTextField(15);
				street = new JRoundedTextField(15); 
				postal = new JRoundedTextField(15);
				unit  = new JRoundedTextField(15);
				number = new JRoundedTextField(15);
	
				JPanel addressPanel = new JPanel();
	
				fNameLabel.setBounds(15, 10, 150, 20);
				fName.setBounds(15, 40, 150, 30);
				lNameLabel.setBounds(15, 80, 150, 20);
				lName.setBounds(15, 110, 150, 30);
	
				contactNoLabel.setBounds(15, 150, 150, 20);
				contactNo.setBounds(15, 190, 150, 30);
	
				streetLabel.setBounds(10, 25, 200, 20);
				street.setBounds(10, 50, 200, 30);
				postalLabel.setBounds(10, 90, 150, 20);
				postal.setBounds(10, 120, 150, 30);
				unitLabel.setBounds(10, 160, 150, 20);
				unit.setBounds(10, 190, 150, 30);
	
				addressPanel.setLayout(null);
				addressPanel.setSize(280, 150);
				addressPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Address"));
	
				addressPanel.setBounds(15, 230, 280, 250);
				addressPanel.add(streetLabel);
				addressPanel.add(street);
				addressPanel.add(postalLabel);
				addressPanel.add(postal);
				addressPanel.add(unitLabel);
				addressPanel.add(unit);		
	
				update.setBounds(190, 500, 100, 50);
				delete.setBounds(10, 500, 100, 50);
				
				update.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						Staff temp = new Staff();		

						int fieldChecker = 0;
						boolean numberCheck = false;

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

						if (street.getText().trim().isEmpty() == true) {
							fieldChecker++;
							temp.setStreet((currStaff.getStreet()));
						} else
							temp.setStreet(street.getText().trim());

						if(postal.getText().trim().isEmpty() == true) {
							fieldChecker++;
							temp.setPostal((currStaff.getPostal()));
						} else
							temp.setPostal(postal.getText().trim());

						if(unit.getText().trim().isEmpty() == true) {
							temp.setUnit((currStaff.getUnit()));
						} else {
							temp.setUnit(unit.getText().trim());
							fieldChecker++;
						}
						if(contactNo.getText().trim().isEmpty() == true) {
							fieldChecker++;
							temp.setContactNumber(currStaff.getContactNumber());
							numberCheck = true;
						} else {
							try {
								if ( contactNo.getText().length() < 8) {
										JOptionPane.showMessageDialog(tb.getMainPanel(), "Phone Number is too short" , "Add New Staff", 2);	
								} else if ( contactNo.getText().length() > 8 ) {
										JOptionPane.showMessageDialog(tb.getMainPanel(), "Phone Number is too long" , "Add New Staff", 2);
								} else {
									Long.parseLong(contactNo.getText().trim());
									temp.setContactNumber(contactNo.getText().trim());
									numberCheck = true;
								}
							} catch (NumberFormatException e1) {
								numberCheck = false;
							}
						}
						
						if( fieldChecker == 5) {
							JOptionPane.showMessageDialog(tb.getMainPanel(), "1 or more fields are empty" , "Updating Staff Information", 2);
						} else if(fName.getText().trim().equalsIgnoreCase(currStaff.getFirstName()) || lName.getText().trim().equalsIgnoreCase(currStaff.getLastName()) ||  contactNo.getText().trim().equalsIgnoreCase(currStaff.getContactNumber()) || street.getText().trim().equalsIgnoreCase(currStaff.getStreet())
								|| postal.getText().trim().equalsIgnoreCase(currStaff.getPostal())) {
							JOptionPane.showMessageDialog(tb.getMainPanel(), "1 or more fields have duplicated values - Remove affected fields" , "Updating Staff Information", 2);
						} else if (numberCheck == false) {
							JOptionPane.showMessageDialog(tb.getMainPanel(), "Enter a Singapore Number" , "Updating Staff Information", 2);
						} 
						else {

							temp.setRole(currStaff.getRole());
							temp.setGender(currStaff.getGender());
							temp.setUID(currStaff.getUID());
							temp.setDD(currStaff.getDD());
							temp.setMM(currStaff.getMM());
							temp.setYY(currStaff.getYY());
							temp.setEmail(currStaff.getEmail());
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

							data.remove(pos);
							data.add(temp);
							
							JOptionPane.showMessageDialog(tb.getMainPanel(), "Successfully Updated Profile!" , "Staff Profile Update", 2);
							
							table.revalidate();
							table.repaint();

						}

					}
				});
				//Send a Delete Request To Firestore
				delete.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						tb.deleteStaff(currStaff);
						frame.dispose();	
						int pos = 0;

						for ( Staff s : data) {
							if (s.getDocID() == currStaff.getDocID())
								break;
							pos++;
						}
						
						JOptionPane.showMessageDialog(tb.getMainPanel(), "Successfully removed " + data.get(pos).getFirstName() + " " + data.get(pos).getLastName() + " from the database" , "Staff Profile Deletion", 2);
						
						data.remove(pos);
						table.revalidate();
						table.repaint();
					}
				});
	
				frame.add(fNameLabel);
				frame.add(fName);
				frame.add(lNameLabel);
				frame.add(lName);
	
				frame.add(addressPanel);
				frame.add(contactNoLabel);
				frame.add(contactNo);
	
				frame.add(update);
				frame.add(delete);
	
				frame.setVisible(true);
	
			} catch (ArrayIndexOutOfBoundsException e1) {}
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

		System.out.println("Updated Staff Table");
	}

	//Edit table column width
	class ModelData extends AbstractTableModel {

		String colNames[] = { "ID", "First Name", "Last Name", "Role"};
		Class<?> colClasses[] = { String.class,  String.class, String.class, String.class};

		// Constructor
		public ModelData() {
			data = new ArrayList<Staff>();
		}

		public int getRowCount() {
			int value = 0;
			try {
				value = data.size();
			} catch (NullPointerException e) {
				return 0;
			}
			return value;
		}

		public int getColumnCount() {
			return colNames.length;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {

			switch(columnIndex) {
			case 0: return data.get(rowIndex).getUID();
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

