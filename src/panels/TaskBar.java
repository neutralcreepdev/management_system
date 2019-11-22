package panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import main.InteractionView;
import resources.Delivery;
import resources.Grocery;
import resources.JRoundedTextField;
import resources.Staff;
import resources.TransactionHistory;
import tables.CustomerTransactionTable;
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
		setBackground(new Color(0x8b041f));

		companyLabel = new JLabel("Neutral Creep");
		logout = new JLabel("Logout");
		deliveryHistory = new JLabel("Delivery History");
		staffMgmt = new JLabel("Staff Management");
		tHistoryLabel = new JLabel("Transaction History");
		
		Font sanserif16 = new Font("sansserif", Font.PLAIN, 16);
		Font sanserif14 = new Font("sansserif", Font.PLAIN, 14);
		Font sanserif20 = new Font("sansserif", Font.PLAIN, 20);
		deliveryHistory.setForeground(Color.WHITE);
		deliveryHistory.setFont(sanserif14);
		staffMgmt.setForeground(Color.WHITE);
		staffMgmt.setFont(sanserif14);
		tHistoryLabel.setForeground(Color.WHITE);
		tHistoryLabel.setFont(sanserif14);
		logout.setForeground(Color.WHITE);
		logout.setFont(sanserif14);
		
		companyLabel.setFont(sanserif20);
		companyLabel.setForeground(Color.WHITE);
		time = new JLabel("TIME: ");
		time.setFont(sanserif16);
		time.setForeground(Color.WHITE);

		tHistoryLabel.setBounds(1140, 10, 200, 30);
		companyLabel.setBounds(40, 10, 200, 30);
		time.setBounds(700, 10, 200, 30);
		staffMgmt.setBounds(1000, 10, 150, 30);
		deliveryHistory.setBounds(1280, 10, 100, 30);
		logout.setBounds(1400, 10, 100, 30);

		//TRANSACTION HISTORY
		tHistoryLabel.addMouseListener(new MouseAdapter() {
			Font sansserif16Bold = new Font("sansserif", Font.BOLD, 16);
			Font sansserif14Bold = new Font("sansserif", Font.BOLD, 14);
			double total = 0;
			
			public void mouseClicked(MouseEvent e) {
				total = getTotalSales();
				JFrame transactionFrame = new JFrame("Transaction History");
				
				transactionFrame.setSize(600, 750);
				transactionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				transactionFrame.setLayout(null);
				transactionFrame.setResizable(false);
				transactionFrame.getContentPane().setBackground(Color.white);
				
				
				TransactionHistoryTable thTable = new TransactionHistoryTable(tb);

				/*Header*/
				JLabel panelLabel = new JLabel("<html><h1>TRANSACTION HISTORY</h1></html>");
				panelLabel.setBounds(160, 0, 300, 25);
				panelLabel.setForeground(Color.white);
				
				/*Red Panel*/
				JPanel redPanel = new JPanel();
				redPanel.setBackground(new Color(0x8b041f));
				redPanel.add(panelLabel);
				redPanel.setBounds(0, 0, 600, 60);
				
				/*Total Sales*/
				JLabel monthlyTotal = new JLabel("Total Sales (All): " + NumberFormat.getCurrencyInstance().format(getTotalSales()));
				monthlyTotal.setBounds(60, 600, 250, 30);
				monthlyTotal.setFont(sansserif16Bold);
				
				/*Net Sales*/
				JLabel netSales = new JLabel("Sales(w/o Tax): " + NumberFormat.getCurrencyInstance().format(getNetSales(total)));
				netSales.setFont(sansserif14Bold);
				netSales.setBounds(60, 630, 250, 30);
				
				/*Tax*/
				JLabel tax = new JLabel("GST(7%): " + NumberFormat.getCurrencyInstance().format(getGstAmount(total)));
				tax.setFont(sansserif14Bold);
				tax.setBounds(60, 660, 250, 30);
				
				/*Avg Sales*/
				JLabel averageSales = new JLabel("Avg. Sale (All): " + NumberFormat.getCurrencyInstance().format(getAverageDailySales(total)));
				averageSales.setBounds(320, 600, 250, 30);
				averageSales.setFont(sansserif16Bold);
				
				ImageIcon searchIcon = new ImageIcon("assets//searchIcon.png");
				searchIcon = new ImageIcon(searchIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
				JButton search = new JButton(searchIcon);
				search.setBounds(480,90,40,40);
				
				search.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						Object selection = JOptionPane.showInputDialog(mainPanel, "Enter Transaction ID", "Search for a record", JOptionPane.PLAIN_MESSAGE);
						if ( selection == null ) {
							System.out.println("Cancel Selected");
							//Do nothing
						} else {	
							boolean resultFound = false;
							for ( TransactionHistory h : iv.getTransactionHistory() ) {
								if ( selection.toString().equals(h.getTransactionID())) {
									JFrame frame = new JFrame("Transaction History for #" + h.getTransactionID());
									frame.setSize(630, 450);
									frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
									frame.setLayout(null);
									
									CustomerTransactionTable table = new CustomerTransactionTable();
									table.updateTable(h.getArray());
									
									//Create a table here showing customer profile and details
									JLabel nameLabel, addressLabel, contactNumberLabel, amountLabel, typeLabel, dateOfTransactionLabel, transactionID;
						
									nameLabel = new JLabel("Name: " + h.getCustomer().getFirstName() + " " + h.getCustomer().getLastName());
									
									if(h.getCustomer().getUnit().isEmpty()) 
										addressLabel = new JLabel("Address: " + h.getCustomer().getStreet() + " S" + h.getCustomer().getPostal());
									else 
										addressLabel = new JLabel("Address: " + h.getCustomer().getStreet() + " #" + h.getCustomer().getUnit() + " S" + h.getCustomer().getPostal());
									
									contactNumberLabel = new JLabel("Contact No. : " + h.getCustomer().getContactNumber());
									typeLabel = new JLabel("Type: " + h.getType());
									dateOfTransactionLabel = new JLabel("Transaction Date: " + parseDateFormal(h.getTransactionDate()));
									amountLabel = new JLabel("Amount: " + NumberFormat.getCurrencyInstance().format(h.getAmount()));
									transactionID = new JLabel("ID: " + h.getTransactionID());
														
									nameLabel.setBounds		(35, 10, 300, 20);			dateOfTransactionLabel.setBounds	(330, 10, 300, 20);			
									addressLabel.setBounds	(35, 40, 300, 20);			contactNumberLabel.setBounds		(330, 40, 300, 20);			
									amountLabel.setBounds	(35, 70, 300, 20);
									
									table.setBounds(5, 150, 600, 250);
									
									frame.add(nameLabel);
									frame.add(addressLabel);
									frame.add(contactNumberLabel);
									frame.add(typeLabel);
									frame.add(dateOfTransactionLabel);
									frame.add(amountLabel);
									frame.add(transactionID);
									frame.add(table);
									frame.setVisible(true);
									resultFound = true;
									
								}
							}
							if ( resultFound == false) 
								JOptionPane.showMessageDialog(mainPanel, "Transaction record not found!", "Search for a record", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				
				/*Transaction Panel*/
				JPanel viewTransactionPanel = new JPanel();
				viewTransactionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "View Transactions"));
				viewTransactionPanel.setBounds(70, 80, 250, 60);
				viewTransactionPanel.setBackground(Color.white);
	
				
				ImageIcon reportIcon = new ImageIcon("assets//ExcelIcon.png");
				reportIcon = new ImageIcon(reportIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
				JButton generateReport = new JButton("Generate Report", reportIcon);
				generateReport.setBounds( 320, 640, 170, 40);
				generateReport.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						
						Object filename = JOptionPane.showInputDialog(mainPanel, "Enter the filename", "Save report as", JOptionPane.PLAIN_MESSAGE);
						//Check how to make the spacing of the excel file
						Workbook wb = new HSSFWorkbook();

						CreationHelper createHelper = wb.getCreationHelper();
						Sheet sheet = wb.createSheet("Sheet1");
						// Create a row and put some cells in it. Rows are 0 based.
						Row row = sheet.createRow(0);
						// Create a cell and put a value in it.
						Cell cell = row.createCell(0);
						cell.setCellValue("Grocery");
						cell = row.createCell(1);
						cell.setCellValue("Qty Sold");
						cell = row.createCell(2);
						cell.setCellValue("Price");

						
						HSSFCellStyle style = (HSSFCellStyle) wb.createCellStyle();
						HSSFFont font = (HSSFFont) wb.createFont();
						font.setFontName(HSSFFont.FONT_ARIAL);
						font.setFontHeightInPoints((short)16);
						font.setBold(true);
						style.setFont(font);

						for(int j = 0; j<3; j++)
							row.getCell(j).setCellStyle(style);
						
						//Insert algorithm to gather data\
						ArrayList<Grocery> groceryReport = consolidateArrayList(thTable.getTableList());
						int rowCounter = 1;
						for (Grocery a : groceryReport) {
							//Creates Rows
							row = sheet.createRow(rowCounter++);
							//Create Cells and populate them
							cell = row.createCell(0);
							cell.setCellValue(a.getName());
							cell = row.createCell(1);
							cell.setCellValue(a.getQuantity());
							cell = row.createCell(2);
							cell.setCellValue(a.getPrice());
						}
						
						//Insert Sales update. using the index from the above algorithm to continue
						
						font.setFontHeightInPoints((short)12);	
						
						Row rowSummary = sheet.createRow(rowCounter+5);
						cell = rowSummary.createCell(0);
						cell.setCellValue("Total Sales: ");
						cell = rowSummary.createCell(1);
						cell.setCellValue(monthlyTotal.getText().substring(19));
						rowSummary.getCell(0).setCellStyle(style);
						
						cell = rowSummary.createCell(2);
						cell.setCellValue("Average Sales");
						cell = rowSummary.createCell(3);
						cell.setCellValue(averageSales.getText().substring(17));
						rowSummary.getCell(2).setCellStyle(style);
						
						rowSummary = sheet.createRow(rowCounter+6);
						cell = rowSummary.createCell(0);
						cell.setCellValue("Net Sale: ");
						cell = rowSummary.createCell(1);
						cell.setCellValue(netSales.getText().substring(16));
						rowSummary.getCell(0).setCellStyle(style);
						
						rowSummary = sheet.createRow(rowCounter+7);
						cell = rowSummary.createCell(0);
						cell.setCellValue("GST(7%): ");
						cell = rowSummary.createCell(1);
						cell.setCellValue(tax.getText().substring(9));
						rowSummary.getCell(0).setCellStyle(style);
						
						sheet.autoSizeColumn(0);
						sheet.autoSizeColumn(1);
						sheet.autoSizeColumn(2);
						sheet.autoSizeColumn(3);
						
						
						//Write the output to a file
						try  (OutputStream fileOut = new FileOutputStream("reports//" + filename.toString() + ".xls")) {
						    wb.write(fileOut);
						    JOptionPane.showMessageDialog(mainPanel, "Report - " + filename.toString() + " saved!", "File Saved", JOptionPane.INFORMATION_MESSAGE);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				
				//need to add in more fake data to show the differences in the list
				JButton dailyViewButton = new JButton("by Day");
				dailyViewButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						String [] options = {"1" ,"2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
						
						Object selection = JOptionPane.showInputDialog(mainPanel, "Choose a day", "Daily Transaction Records", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				        
						if ( selection == null ) {
							System.out.println("Cancel Selected");
							//Do nothing
						} else {
							thTable.updateTable(iv.getTransactionHistory());
							double value = thTable.filterByDay(selection.toString());
							if ( value < 0)
								JOptionPane.showMessageDialog(mainPanel, "Selected Day has no records" , "Empty Records", JOptionPane.INFORMATION_MESSAGE);
							else {
								monthlyTotal.setText("Total Sales ("+ selection.toString()+"): " + NumberFormat.getCurrencyInstance().format(value));
								averageSales.setText("Avg. Sale ("+ selection.toString()+"): " + NumberFormat.getCurrencyInstance().format(value/thTable.getTableSize()));
								tax.setText("GST(7%): "+ selection.toString()+"): " + NumberFormat.getCurrencyInstance().format(getGstAmount(value)));
								netSales.setText("Sales(W/o Tax): " + NumberFormat.getCurrencyInstance().format(getNetSales(value)));
							}
						}
					} 
				});
				
				JButton monthlyViewButton = new JButton("by Month");
				monthlyViewButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						String [] options = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
						
						Object selection = JOptionPane.showInputDialog(mainPanel, "Choose a month", "Monthly Transaction Records", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				        
						if ( selection == null ) {
							System.out.println("Cancel Selected");
							//Do nothing
						} else {
							thTable.updateTable(iv.getTransactionHistory());
							
							double value = thTable.filterByMonth(selection.toString());
							
							if ( value < 0)
								JOptionPane.showMessageDialog(mainPanel, "Selected Month has no records" , "Empty Records", JOptionPane.INFORMATION_MESSAGE);
							else {
								monthlyTotal.setText("Total Sales ("+ selection.toString()+"): " + NumberFormat.getCurrencyInstance().format(value));
								averageSales.setText("Avg. Sale ("+ selection.toString()+"): " + NumberFormat.getCurrencyInstance().format(value/thTable.getTableSize()));
								tax.setText("GST(7%): "+ selection.toString()+"): " + NumberFormat.getCurrencyInstance().format(getGstAmount(value)));
								netSales.setText("Sales(W/o Tax): " + NumberFormat.getCurrencyInstance().format(getNetSales(value)));
							}
						}
					} 
					
				});

				
				viewTransactionPanel.add(monthlyViewButton);
				viewTransactionPanel.add(dailyViewButton);
				
				thTable.setBounds(25, 150, 530, 450);
				thTable.setBackground(Color.WHITE);

				transactionFrame.add(redPanel);
				transactionFrame.add(thTable);
				transactionFrame.add(viewTransactionPanel);
				transactionFrame.add(monthlyTotal);
				transactionFrame.add(averageSales);
				transactionFrame.add(search);
				transactionFrame.add(tax);
				transactionFrame.add(netSales);
				transactionFrame.add(generateReport);

				transactionFrame.setVisible(true);

				thTable.updateTable(iv.getTransactionHistory());
			}
		});

		//STAFF MANAGEMENT
		staffMgmt.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				JFrame staffFrame = new JFrame("Staff Management");
				
				staffFrame.setSize(600, 650);
				staffFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				staffFrame.setLayout(null);
				staffFrame.setResizable(false);
				staffFrame.getContentPane().setBackground(Color.white);
				
				JPanel redPanel = new JPanel();
				redPanel.setBackground(new Color(0x8b041f));

				sTable = new StaffTable(tb);
				
				ImageIcon addNewStaffIcon = new ImageIcon("assets//addStaffIcon.jpg");
				addNewStaffIcon = new ImageIcon(addNewStaffIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));

				
				JButton addStaff = 	new JButton(addNewStaffIcon);
				JLabel panelLabel = new JLabel("<html><h1>STAFF MANAGEMENT</h1></html>");
				panelLabel.setForeground(Color.WHITE);
				redPanel.add(panelLabel);
				redPanel.setBounds(0, 0, 610, 60);

				panelLabel.setBounds(80,0, 290, 25);
				sTable.setBounds(40, 150, 510, 490);
				sTable.setBackground(Color.WHITE);
				addStaff.setBounds(480, 80, 60, 60);

				addStaff.addActionListener(new addStaff_AL(sTable));

				staffFrame.add(redPanel);
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
				dhFrame.getContentPane().setBackground(Color.WHITE);
				
				JLabel panelLabel, filterLabel;
				JPanel redPanel = new JPanel();
				redPanel.setBackground(new Color(0x8b041f));

				panelLabel = new JLabel("<html><h1>Past Deliveries</h1></html>");
				panelLabel.setForeground(Color.WHITE);
				redPanel.add(panelLabel);
				redPanel.setBounds(0, 0, 600, 60);

				
				ImageIcon searchIcon = new ImageIcon("assets//searchIcon.png");
				searchIcon = new ImageIcon(searchIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
				JButton search = new JButton(searchIcon);
				search.setBounds(500,80,40,40);
				
				search.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						Object selection = JOptionPane.showInputDialog(mainPanel, "Enter Transaction ID", "Search for a record", JOptionPane.PLAIN_MESSAGE);
						if ( selection == null ) {
							System.out.println("Cancel Selected");
							//Do nothing
						} else {	
							boolean resultFound = false;

								//Might need to add a boolean to determine if the delivery information is enough
								// E.g. Pending unSelected deliveries, they dont have certain information like staff names etc
								for (Delivery d : iv.getPastDeliveries()) {
									if (d.getTransactionID().equals(selection.toString())) {
										//This method shall print the details of the delivery set
										resultFound = true;
										JFrame frame = new JFrame("Delivery Information #" + d.getTransactionID());
										frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
										frame.setSize(630, 450);
										frame.setLayout(null);
										
										JLabel customerName, address, status, expectedTime, actualTime, transactionDate, amountSpent;
										JLabel employeeName; 
										
										customerName = new JLabel("Customer Name: " + d.getCustomerName());
										address = new JLabel("Address: " + d.getAddress());
										status = new JLabel("Status: " + d.getDeliveryStatus());
										expectedTime = new JLabel("Time Expected: " + d.getExpectedDate() + " " + parseTimeString(d.getExpectedTime()));
										actualTime = new JLabel("Time Arrived: " + d.getActualDate() + " " + parseTimeString(d.getActualTime()));
										transactionDate = new JLabel("Transaction Date: " + parseDateFormal(d.getTransactionDate()));
										amountSpent = new JLabel("Amount: " + NumberFormat.getCurrencyInstance().format(d.getAmount()));
										employeeName = new JLabel("Employee Name: " + d.getEmployeeName());
										
										 
										CustomerTransactionTable thTable = new CustomerTransactionTable();
										thTable.updateTable(d.getItemList());
										
										thTable.setBounds(20, 150, 570, 240);
										
										customerName.setBounds		(20, 10, 250, 20);		transactionDate.setBounds	(380, 10, 250, 20);	 		
										address.setBounds			(20, 40, 250, 20);		status.setBounds			(380, 40, 250, 20);		
										expectedTime.setBounds		(20, 70, 250, 20); 		actualTime.setBounds		(380, 70, 250, 20);
										employeeName.setBounds		(20, 100, 250, 20);		amountSpent.setBounds		(380, 100, 250, 20);					
										
										frame.add(customerName);
										frame.add(address);
										frame.add(status);
										frame.add(expectedTime);
										frame.add(actualTime);
										frame.add(transactionDate);
										frame.add(amountSpent);
										frame.add(employeeName);
										frame.add(thTable);
							
										frame.setVisible(true);
									}
								}
							
							if ( resultFound == false) 
								JOptionPane.showMessageDialog(mainPanel, "Delivery record not found!", "Search for a record", JOptionPane.ERROR_MESSAGE);
						}
					}
				});		

				JComboBox<String> filterComboBox;
				String[] filteringCriteria = new String[] { "<None>", "STATUS - DELIVERED", "STATUS - LATE" };

				filterLabel = new JLabel("FILTER: ");
				filterComboBox = new JComboBox<>(filteringCriteria);

				DeliveryTable table = new DeliveryTable(mainPanel);
				table.setBackground(Color.WHITE);

				filterLabel.setBounds(190, 100, 70, 20);
				filterComboBox.setBounds(240, 100, 150, 20);
				panelLabel.setBounds(200, 0, 200, 30);
				table.setBounds(10, 150, 560, 460);

				filterComboBox.addItemListener(new filter_IL(table));
				
				table.updateTableDelivery(iv.getPastDeliveries());

				dhFrame.add(filterLabel);
				dhFrame.add(filterComboBox);
				dhFrame.add(redPanel);
				dhFrame.add(table);
				dhFrame.add(search);
				
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
			if (m < 10 && s < 10)
				time.setText("TIME: " + h + ":0" + m + ":0" + s);
			else if (m < 10)
				time.setText("TIME: " + h + ":0" + m + ":" + s);
			else if (s < 10)
				time.setText("TIME: " + h + ":" + m + ":0" + s);
			else
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
	
	public double getTotalSales() {
		double total = 0.00;
		for ( TransactionHistory h : iv.getTransactionHistory()) {
			total += h.getAmount();
		}
		
		return total;
	}
	
	public double getAverageDailySales(double value) {
		int days = iv.getTransactionHistory().size();
		
		if (value == 0) {
			return 0;
		}
		
		return value/days;
	}
	
	public double getNetSales(double value) {
		return (value / 107) * 100;
	}
	
	public double getGstAmount(double value) {
		return (value / 107) * 7;
	}
	
	public String parseDateFormal(Date d) {
		return new SimpleDateFormat("dd/MM/yyyy hh:mm a").format(d);
	}
	
	public String parseTimeString(String d) {
		try {
			Date temp = new SimpleDateFormat("hh:mm").parse(d);
			return new SimpleDateFormat("hh:mm a").format(temp);
		} catch (ParseException | NullPointerException e) {
			return "<Waiting for Pickup>";
		}
	}

	public ArrayList<Grocery> consolidateArrayList(ArrayList<TransactionHistory> input) {
		ArrayList<Grocery> consolidatedList = new ArrayList<>();

		for ( TransactionHistory th : input) {
			ArrayList<Object> tempCustTransaction = (ArrayList<Object>) th.getArray().get(0);
			if ( consolidatedList.size() == 0 ) {
				//If the list is empty, then the first order of the TH will be added automatically
					for ( Object o : tempCustTransaction) {
						Map<String, Object> t = (Map<String, Object>) o;
						Grocery a = new Grocery((String)t.get("name"), (long)t.get("quantity"), (double)t.get("cost"));
						consolidatedList.add(a);
					}	
			} else {
				Grocery tempMemoryEatingObject = new Grocery();
				for ( Object o : tempCustTransaction) {
					boolean isUnique = false;
					Map<String, Object> t = (Map<String, Object>) o;
					
					for (Grocery b : consolidatedList) {
						if(b.getName().equals(t.get("name"))) {
								b.setQuantity(b.getQuantity()+(long)t.get("quantity"));
								isUnique = false;
								break;
						} else {
							tempMemoryEatingObject = new Grocery((String)t.get("name"), (long)t.get("quantity"), (double)t.get("cost"));
							isUnique = true;
						}
					}
					//add record as a new grocery object
					if (isUnique == true) {
						consolidatedList.add(tempMemoryEatingObject);
					}
				}	
			}
		}
		return consolidatedList;
	}
}