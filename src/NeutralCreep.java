//import javax.swing.SwingUtilities;

import main.Driver;

public class NeutralCreep {
	Driver d;
	
	public NeutralCreep () {
		d = new Driver();
	}
	
	public void execute() {
		d.connect();
		d.readData();	
	}
	
	public static void main(String [] args) {

		//SwingUtilities.invokeLater(new Runnable() {
		   // @Override
		    //public void run() {
				NeutralCreep nc = new NeutralCreep();
				nc.execute();
		  //  }
		//});
	}
	
}
