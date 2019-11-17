package panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TaskBar extends JPanel implements MouseListener {

	private JLabel companyLabel, logout, history, addStaff, time;

	public TaskBar() {
		
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
		
		add(time);
		add(companyLabel);
		add(logout);
		add(history);
		add(addStaff);
		addMouseListener(this);
		javax.swing.Timer timer = new javax.swing.Timer(1000, new ClockListener());
		timer.start();

	}

	public void mouseClicked(MouseEvent me) {}
	public void mouseEntered(MouseEvent me) {}
	public void mouseExited(MouseEvent me) {}
	public void mouseReleased(MouseEvent me) {}
	public void mousePressed(MouseEvent me) {
		
		if (me.getY() >= 5 && me.getY() <= 35) {
			if (me.getX() >= 1200 && me.getX() <= 1255) {
				System.out.println("Clicked on addStaff");
			} else if (me.getX() >= 1310 && me.getX() <= 1350) {
				System.out.println("Clicked on history");
			} else if (me.getX() >= 1400 && me.getX() <= 1440) {
				System.out.println("Clicked on logout");
			}
		} 
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
	

}
