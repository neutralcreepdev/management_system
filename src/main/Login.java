package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
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

	Driver driver;

	public Login(Driver d) {

		driver = d;
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//setSize(screenSize.width, screenSize.height);

	    setSize(1525, 800);
	    
		// Consider a groupLayout to sort the setbounds issue
		setLayout(null);

		loginButton = new JButton("Login");
		exitButton = new JButton("Exit");
		username = new JTextField();
		password = new JPasswordField();
		usernameLabel = new JLabel("Username: ");
		passwordLabel = new JLabel("Password: ");
		welcomeBanner1 = new JLabel("<html><h3>Welcome to</h3></html>");
		welcomeBanner2 = new JLabel("<html><h1>NEUTRAL CREEP</h1></html>");
		welcomeBanner3 = new JLabel("<html><h3>Management System</h3></html>");

		// Button display
		username.setBounds(600, 300, 300, 50);
		password.setBounds(600, 400, 300, 50);
		loginButton.setBounds(600, 500, 100, 75);
		exitButton.setBounds(800, 500, 100, 75);
		usernameLabel.setBounds(520, 300, 100, 50);
		passwordLabel.setBounds(520, 400, 100, 50);
		welcomeBanner1.setBounds(700, 125, 200, 100);
		welcomeBanner2.setBounds(650, 100, 300, 200);
		welcomeBanner3.setBounds(675, 175, 200, 100);

		exitButton.addActionListener(new exitButtonActionListener());
		loginButton.addActionListener(new loginButtonActionListener());
		password.addActionListener(new password_AL());

		add(exitButton);
		add(loginButton);
		add(username);
		add(password);
		add(usernameLabel);
		add(passwordLabel);
		add(welcomeBanner1);
		add(welcomeBanner2);
		add(welcomeBanner3);
		setVisible(true);
	}

	public class exitButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "Good bye!", null, 1, null);
			System.exit(0);
		}
	}

	public class loginButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			char[] input = password.getPassword();
			
			try {
				File file = new File("passwd.dat");
				File file2 = new File("shadow.dat");
				String hashPW = read(file);
				String salt = read(file2);


				String pw = convertToString(input);
				
				String userHash = MD5(pw + salt);
				if (userHash.equals(hashPW)) {

					JOptionPane.showMessageDialog(null, "Login!", null, 2, null);

					driver.toggleLoginView();

				} else
					JOptionPane.showMessageDialog(null, "Failed!", null, 2, null);
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
	
	public class password_AL implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
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