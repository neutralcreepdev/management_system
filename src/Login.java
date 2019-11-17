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
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Login extends JFrame implements MouseListener {

	private JButton exitButton;
	private JButton loginButton;
	private JTextField username, password;
	private JLabel usernameLabel, passwordLabel, welcomeBanner1, welcomeBanner2, welcomeBanner3;
	private JLabel bankerLabel, managerLabel;
	private boolean loginType;
	Driver driver;

	public Login() {
		loginType = true;
		setTitle("Management POS");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//setSize(screenSize.width, screenSize.height);

	    setSize(1525, 800);
	    
		// Consider a groupLayout to sort the setbounds issue
		setLayout(null);

		loginButton = new JButton("Login");
		exitButton = new JButton("Exit");
		username = new JTextField();
		password = new JTextField();
		usernameLabel = new JLabel("Username: ");
		passwordLabel = new JLabel("Password: ");
		welcomeBanner1 = new JLabel("<html><h3>Welcome to</h3></html>");
		welcomeBanner2 = new JLabel("<html><h1>NEUTRAL CREEP</h1></html>");
		welcomeBanner3 = new JLabel("<html><h3>Management System</h3></html>");
		bankerLabel = new JLabel("<html><h2>BANKER</h2></html>");
		managerLabel = new JLabel("<html><h2>MANAGER</h2></html>");

		// Button display
		username.setBounds(600, 300, 300, 50);
		password.setBounds(600, 400, 300, 50);
		loginButton.setBounds(600, 500, 100, 75);
		exitButton.setBounds(800, 500, 100, 75);
		usernameLabel.setBounds(520, 300, 100, 50);
		passwordLabel.setBounds(520, 400, 100, 50);
		welcomeBanner1.setBounds(675, 125, 200, 100);
		welcomeBanner2.setBounds(625, 100, 300, 200);
		welcomeBanner3.setBounds(650, 175, 200, 100);
		bankerLabel.setBounds(600, 250, 100, 50);
		managerLabel.setBounds(800, 250, 100, 50);

		managerLabel.setForeground(Color.BLUE);

		exitButton.addActionListener(new exitButtonActionListener());
		loginButton.addActionListener(new loginButtonActionListener());

		add(exitButton);
		add(loginButton);
		add(username);
		add(password);
		add(usernameLabel);
		add(passwordLabel);
		add(welcomeBanner1);
		add(welcomeBanner2);
		add(welcomeBanner3);
		add(bankerLabel);
		add(managerLabel);
		addMouseListener(this);
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
			try {
				File file = new File("passwd.dat");
				File file2 = new File("shadow.dat");
				String hashPW = read(file);
				String salt = read(file2);

				String pw = password.getText().trim();
				String userHash = MD5(pw + salt);
				if (userHash.equals(hashPW)) {
					// Authentication do later
					if (loginType == true)
						System.out.println("Login type: Manager");
					else
						System.out.println("Login type: Banker");

					JOptionPane.showMessageDialog(null, "Login!", null, 2, null);

					driver = new Driver();

				} else
					JOptionPane.showMessageDialog(null, "Failed!", null, 2, null);
			} catch (IOException io) {
				io.printStackTrace();
			}

		}
	}

	public void mousePressed(MouseEvent e) {
		Point p = e.getPoint();

		// true is manager, false is Banker;
		if (p.getY() >= 300 && p.getY() <= 320) {
			if (p.getX() >= 700 && p.getX() <= 795) {
				loginType = false;
				bankerLabel.setForeground(Color.BLUE);
				managerLabel.setForeground(Color.BLACK);
			} else if (p.getX() >= 900 && p.getX() <= 1010) {
				loginType = true;
				managerLabel.setForeground(Color.BLUE);
				bankerLabel.setForeground(Color.BLACK);
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public static void main(String[] args) {
		Login login = new Login();

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