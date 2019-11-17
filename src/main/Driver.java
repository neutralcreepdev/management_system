package main;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.Transaction;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;


import resources.Delivery;
import resources.EmailSender;
import resources.Grocery;
import resources.Staff;

import com.google.firebase.FirebaseApp;

public class Driver {
	
	private Login login;
	private Firestore db;
	private InteractionView iv;
	private ArrayList<Grocery> Groceries;
	private ArrayList<Delivery> Deliveries;
	private JFrame frame;
	
	final static String c_GROCERIES = "Groceries";
	final static String c_ALL_DELIVERIES = "Deliveries";
	final static String c_PENDING_DELIVERIES = "Pending Deliveries";
	final static String c_STAFF = "Staff";
	//Might need an update function to update all deliveries to move them to history everyday
	
	public Driver() {
		
		frame = new JFrame();
		login = new Login(this);
		iv = new InteractionView(this);
		
		frame.setTitle("Management POS");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(iv);
		frame.add(login);	
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setSize(1525, 800);
		frame.setBackground(new Color(0xf2f5f6));
		
		iv.setVisible(false);
		frame.setVisible(true);
	}

	//Transition method between login to POS
	//Login screen will toggle the view
	public void toggleLoginView() {

		iv.setVisible(true);
		login.setVisible(false);
		iv.setView();
		connect();
		readData();
		iv.setData(Groceries, Deliveries);
		frame.revalidate();
	}
	
	public void logout() {
		login.reset();
		login.setVisible(true);
		iv.setVisible(false);
		frame.revalidate();
	}
	
	public void connect() {
		
		try {
			FileInputStream serviceAccount = new FileInputStream("E:\\eclipse-workspace\\_FYP\\service.json");
			GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
			FirebaseOptions options = new FirebaseOptions.Builder()
				    .setCredentials(credentials)
				    .setDatabaseUrl("https://pos-system-bb06b.firebaseio.com")
				    .build();
			
			FirebaseApp.initializeApp(options);
			db = FirestoreClient.getFirestore();

			System.out.println("Established a connection to firestore...");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	// asynchronously retrieve all users
	public void readData() {
		Groceries = new ArrayList<>();
		Deliveries = new ArrayList<>();
		ApiFuture<QuerySnapshot> queryGrocery = db.collection(c_GROCERIES).get();
		ApiFuture<QuerySnapshot> queryDelivery = db.collection(c_PENDING_DELIVERIES).get();

		QuerySnapshot querySnapshot;
		
		try {
			querySnapshot = queryGrocery.get();
			List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
			
			for (QueryDocumentSnapshot document : documents) {
			  Grocery temp = new Grocery(document.getString("Name"), document.getString("Supplier"),document.getString("Item ID"), document.getLong("Quantity"),document.getDouble("Price"), document.getId(), document.getString("Description"), document.getString("Url"));
			  Groceries.add(temp);
			}
			
			querySnapshot = queryDelivery.get();
			documents = querySnapshot.getDocuments();
			
			for (QueryDocumentSnapshot document2 : documents) {
				  Delivery temp2 = new Delivery(document2.getString("transactionID"), document2.getString("customerID"),document2.getString("employeeID"), document2.getString("deliveryID"), document2.getString("Status"), document2.getTimestamp("date"));
				  Deliveries.add(temp2);  
			}
			
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

	}
	

	public void addStock(Grocery g) {
		DocumentReference docRef = db.collection(c_GROCERIES).document();

		//asynchronously write data
		
		Map<String, Object> data = new HashMap<>();
		data.put("Name", g.getName());
		data.put("Supplier", g.getSupplier());
		data.put("Quantity", g.getQuantity());
		data.put("Price", g.getPrice());
		data.put("Item ID", g.getItemID());
		data.put("Description", g.getDescription());
		data.put("Url", g.getImageURL());

		ApiFuture<WriteResult> result = docRef.set(data);
		
		try {
			System.out.println("Update time : " + result.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("Done");
	}
	
	public void updateStock(Grocery updatedG, Grocery oldG) {
		
		System.out.println("Driver: Recieved an update call from SearchTable");
		
		final DocumentReference documentRef = db.collection(c_GROCERIES).document(oldG.getDocID());

		// run an asynchronous transaction
		ApiFuture<Void> transaction =
		    db.runTransaction(
		        new Transaction.Function<Void>() {
		          @Override
		          public Void updateCallback(Transaction transaction) throws Exception {
		            // retrieve document and update fields
		        	  
		            DocumentSnapshot snapshot = transaction.get(documentRef).get();
		            
		            //Qty
		            long oldQty = snapshot.getLong("Quantity");
		            
		            transaction.update(documentRef, "Quantity", oldQty + updatedG.getQuantity(), "Name", updatedG.getName(), "Supplier", updatedG.getSupplier(), "Price", updatedG.getPrice(), "Item ID" , updatedG.getItemID());

		            return null;
		          }
		        });
		// block on transaction operation using transaction.get()
	}
	
	public void deleteStock(Grocery g) {
		ApiFuture<WriteResult> writeResult = db.collection(c_GROCERIES).document(g.getDocID()).delete();

		try {
			System.out.println("Update time : " + writeResult.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	public void addStaff(Staff s) {

		DocumentReference docRef = db.collection(c_STAFF).document();

		//asynchronously write data
		
		Map<String, Object> data = new HashMap<>();
		data.put("firstName", s.getFirstName());
		data.put("lastName", s.getLastName());
		data.put("address", s.getAddress());
		data.put("email", s.getEmail());
		data.put("contactNumber", s.getContactNumber());
		data.put("role", s.getRole());
		data.put("employeeID", s.getEmployeeID());
		data.put("dob", s.getDOB());
		data.put("gender", s.getGender());
		ApiFuture<WriteResult> result = docRef.set(data);
		
		try {
			System.out.println("Update time : " + result.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		//Consider using a thread to handle the email portion for quick GUI Response
		new EmailSender(s.getEmail(), "123456").send();
		
		JOptionPane.showMessageDialog(null, "Profile Created" , "Add New Staff", 1);
	}
	
	public void updateStaff(Staff s) {
		ApiFuture<WriteResult> writeResult = db.collection(c_STAFF).document(s.getDocID()).delete();

		try {
			System.out.println("Update time : " + writeResult.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		DocumentReference docRef = db.collection(c_STAFF).document();

		//asynchronously write data
		
		Map<String, Object> data = new HashMap<>();
		data.put("firstName", s.getFirstName());
		data.put("lastName", s.getLastName());
		data.put("address", s.getAddress());
		data.put("email", s.getEmail());
		data.put("contactNumber", s.getContactNumber());
		data.put("role", s.getRole());
		data.put("employeeID", s.getEmployeeID());
		data.put("dob", s.getDOB());
		data.put("gender", s.getGender());
		ApiFuture<WriteResult> result = docRef.set(data);
		
		try {
			System.out.println("Update time : " + result.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteStaff(Staff s) {
		ApiFuture<WriteResult> writeResult = db.collection(c_STAFF).document(s.getDocID()).delete();

		try {
			System.out.println("Update time : " + writeResult.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Grocery> getGrocery() {
		return Groceries;
	}
	
	public ArrayList<Delivery> getDelivery() {
		return Deliveries;
	}
	
	public ArrayList<Delivery> getAllDeliveries() {
		ArrayList<Delivery> d = new ArrayList<>();

		ApiFuture<QuerySnapshot> queryDelivery = db.collection(c_ALL_DELIVERIES).get();
		
		try {
			QuerySnapshot querySnapshot = queryDelivery.get();
			List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
			
			for (DocumentSnapshot document2 : documents) {
				 Delivery temp2 = new Delivery(document2.getString("transactionID"), document2.getString("customerID"),document2.getString("employeeID"), document2.getString("deliveryID"), document2.getString("Status"), document2.getTimestamp("date"));
				 d.add(temp2);
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		return d;
	}
	
	public ArrayList<Staff> getStaff() {
		
		ArrayList<Staff> Staffs = new ArrayList<>();
		ApiFuture<QuerySnapshot> queryGrocery = db.collection(c_STAFF).get();

		QuerySnapshot querySnapshot;
		
		try {
			querySnapshot = queryGrocery.get();
			List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
			
			for (QueryDocumentSnapshot document : documents) {
			  Staff s = new Staff(document.getString("firstName"), document.getString("lastName"),document.getString("address"), document.getString("email"), document.getString("employeeID"), document.getString("role"), document.getString("gender"), document.getString("dob"), document.getString("contactNumber"), document.getId());
			  System.out.println("Document Path: " + document.getId());
			  Staffs.add(s);
			}		
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return Staffs;
	}
	
	// Get InteractionView Class to Communicate with the Driver Class to retrieve latest information from firebase
	public static void main(String [] args) {
		Driver d = new Driver();
	}

}


//Second Style of Delivery [Uses the auto-ID]

/*ApiFuture<QuerySnapshot> queryDelivery = db.collection(c_DELIVERIES).get();
QuerySnapshot querySnapshot;
try {
	DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
	String dateQuery1 = dtf1.format(now);
	
	
	querySnapshot = queryDelivery.get();
	List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
	for (QueryDocumentSnapshot document2 : documents) {
		Timestamp a = document2.getTimestamp("date");
		String temp = a.toString().substring(0, 10);
		System.out.println("Temp - " + temp);
		
		
		if ( temp.equals(dateQuery1)) {
			System.out.println("Matched");
			String path = document2.getId();
			//Reset the variables and redirect the path to the discovered path to retreive the deliveries
		}
	}
	
	System.out.println("Size-" + documents.size());
	
	System.out.println("Date " + dateQuery1);
} catch (InterruptedException | ExecutionException e) {
	e.printStackTrace();
}

*/