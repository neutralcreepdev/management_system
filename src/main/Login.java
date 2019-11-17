package main;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JPanel {

	private JButton exitButton;
	private JButton loginButton;
	private JTextField username;
	private JPasswordField password;
	private JLabel usernameLabel, passwordLabel, welcomeBanner1, welcomeBanner2, welcomeBanner3;
	JPanel pageElements = new JPanel();
	private JPanel mainPanel = this;
	private JLabel statusLabel;
	private String [] status = {"Establishing Connection...", "Connected", "Offline"};
	
	Driver driver;

	public Login(Driver d) {
		
		driver = d;
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//setSize(screenSize.width, screenSize.height);

	    setSize(1525, 800);
	    
		// Consider a groupLayout to sort the setbounds issue
		setLayout(null);
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		//Set background image
		ImageIcon icon = new ImageIcon("src//images//Supermarket3.jpg");
		icon = new ImageIcon(icon.getImage().getScaledInstance(1525, 800, Image.SCALE_DEFAULT));
		JLabel backgroundImage = new JLabel(icon);
				


		loginButton = new JButton("<html><h3>LOGIN</h3></html>");
		exitButton = new JButton("<html><h3>EXIT</h3></html>");
		username = new JTextField();
		password = new JPasswordField();
		usernameLabel = new JLabel("Username: ");
		passwordLabel = new JLabel("Password: ");
		welcomeBanner1 = new JLabel("<html><h2>Welcome to</h2></html>");
		welcomeBanner2 = new JLabel("<html><h1>NEUTRAL CREEP</h1></html>");
		welcomeBanner3 = new JLabel("<html><h3>Management System</h3></html>");
		statusLabel = new JLabel(status[0]);
		statusLabel.setBackground(Color.WHITE);
		statusLabel.setOpaque(true);
		statusLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		// Button display
		pageElements.setBounds(525, 175, 475, 400);
		pageElements.setLayout(null);
		pageElements.setBorder(BorderFactory.createLineBorder(Color.black));
		pageElements.setBackground(new Color(0x72B0D6));
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		welcomeBanner1.setBounds(184, 0, 200, 100);
		welcomeBanner2.setBounds(137, 30, 300, 100);
		welcomeBanner3.setBounds(167, 60, 200, 100);
		
		usernameLabel.setBounds(67, 150, 100, 40);
		passwordLabel.setBounds(67, 200, 100, 40);
		username.setBounds(167, 150, 200, 40);
		password.setBounds(167, 200, 200, 40);
		loginButton.setBounds(77, 300, 125, 75);
		exitButton.setBounds(297, 300, 100, 75);
		statusLabel.setBounds(325, 0, 150, 20);

		username.setBorder(BorderFactory.createLineBorder(Color.black));
		password.setBorder(BorderFactory.createLineBorder(Color.black));
		backgroundImage.setBounds(0, 0, 1525, 800);
		backgroundImage.setBorder(BorderFactory.createLineBorder(Color.black));
		
		exitButton.setBorder(BorderFactory.createLineBorder(Color.black));
		loginButton.setBorder(BorderFactory.createLineBorder(Color.black));

		exitButton.addActionListener(new exitButtonActionListener());
		loginButton.addActionListener(new loginButtonActionListener());


		pageElements.add(exitButton);
		pageElements.add(loginButton);
		pageElements.add(username);
		pageElements.add(password);
		pageElements.add(usernameLabel);
		pageElements.add(passwordLabel);
		pageElements.add(welcomeBanner1);
		pageElements.add(welcomeBanner2);
		pageElements.add(welcomeBanner3);
		pageElements.add(statusLabel);
		add(pageElements);
		add(backgroundImage);
		
		setVisible(true);
	}

	public void updateStatus(int statusCode) {
		if (statusCode == 1) {
			pageElements.remove(statusLabel);
			statusLabel = new JLabel("  " + status[1]);
			pageElements.add(statusLabel);
			statusLabel.setBounds(400, 0, 150, 20);
			statusLabel.setBorder(BorderFactory.createLineBorder(Color.black));
			statusLabel.setBackground(Color.GREEN);
			statusLabel.setOpaque(true);
		} else if ( statusCode == 2) {
			pageElements.remove(statusLabel);
			statusLabel = new JLabel(" " + status[2]);
			pageElements.add(statusLabel);
			statusLabel.setBorder(BorderFactory.createLineBorder(Color.black));
			statusLabel.setBounds(430, 0, 150, 20);
			statusLabel.setOpaque(true);
			statusLabel.setBackground(Color.RED);
		}
		pageElements.revalidate();
		pageElements.repaint();
	}
	
	public class exitButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(mainPanel, "Good bye!", "Logout Successful", 1, null);
			System.exit(0);
		}
	}

	public class loginButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			char[] input = password.getPassword();
			
			try {
				File file = new File("src\\resources\\passwd.dat");
				File file2 = new File("src\\resources\\shadow.dat");
				String hashPW = read(file);
				String salt = read(file2);

				String pw = convertToString(input);
				
				String userHash = MD5(pw + salt);
				
				if (statusLabel.getText().trim().equals("Connected")) {				
					if (userHash.equals(hashPW)) {
						JOptionPane.showMessageDialog(mainPanel, "Success!", "Login Success", 1, null);	
						driver.toggleLoginView();
					} else
						JOptionPane.showMessageDialog(mainPanel, "Failed!", "Login Failed", 0, null);
				} else 
					JOptionPane.showMessageDialog(mainPanel, "Check the internet connection!", "Login Failed", 0, null);
					
				
			} catch (IOException io) {
				io.printStackTrace();
			}
			
			//Zero out the possible password, for security.
            Arrays.fill(input, '0');
 
            password.selectAll();
            resetFocus();
		}
	}
	
	
	private String convertToString (char [] input) {
		String a = "";
		for(int i = 0; i < input.length; i++) {
			a += input[i];
		}	
		return a;
	}
    protected void resetFocus() {
        password.requestFocusInWindow();
    }
	
	public void reset() {
		username.setText("");
		password.setText("");
	}

	// Reading localized data
	public String read(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;

		String[] stSplit;
		String secondField = "";
		while ((st = br.readLine()) != null) {
			stSplit = st.split(":");
			String key = username.getText().trim();
			if (stSplit[0].equals(key)) {
				secondField = stSplit[1];
				break;
			}
		}
		br.close();
		return secondField;
	}

	public String MD5(String input) {
		try {
			// Static getInstance method is called with hashing MD5
			MessageDigest md = MessageDigest.getInstance("MD5");

			// digest() method is called to calculate message digest
			// of an input digest() return array of byte
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		}

		// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}