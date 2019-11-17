import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Login extends JFrame implements MouseListener{

		
		private JButton exitButton;
		private JButton loginButton;
		private JTextField username, password;
		private JLabel usernameLabel, passwordLabel, welcomeBanner1, welcomeBanner2, welcomeBanner3;
		private JLabel bankerLabel, managerLabel;
		private boolean loginType;
		
		public Login() {
			 	loginType = true;
				setTitle("Management POS");
			    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			    setSize(screenSize.width,screenSize.height);
			    
			    //Consider a groupLayout to sort the setbounds issue
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
				username.setBounds(700, 300, 300, 50);
				password.setBounds(700, 400, 300, 50);
				loginButton.setBounds(700, 500, 100, 75);
				exitButton.setBounds(900, 500, 100, 75);
				usernameLabel.setBounds(620, 300, 100, 50);
				passwordLabel.setBounds(620, 400, 100, 50);
				welcomeBanner1.setBounds(775, 125 , 200, 100);
				welcomeBanner2.setBounds(725, 100 , 300, 200);
				welcomeBanner3.setBounds(750, 175 , 200, 100);
				bankerLabel.setBounds(700, 250, 100, 50);
				managerLabel.setBounds(900, 250, 100, 50);
				
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
		
		public class exitButtonActionListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {	
				JOptionPane.showMessageDialog(null, "Good bye!", null, 1, null);
				System.exit(0);
			}	
		}
		
		public class loginButtonActionListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {	
				
				//Authentication do later
				if(loginType == true)
					System.out.println("Login type: Manager");
				else
					System.out.println("Login type: Banker");
				
				JOptionPane.showMessageDialog(null, "Login!", null, 2, null);
			}	
		}
		
		public void mousePressed(MouseEvent e) {
			Point p = e.getPoint();
			
			//true is manager, false is Banker;
			if ( p.getY() >= 300 && p.getY() <= 320 ) {
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
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {}
		
		public static void main(String[] args) {
			Login login = new Login();

		}

	}