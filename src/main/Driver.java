package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
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
	
	public Driver() {
		
		frame = new JFrame();
		login = new Login(this);
		iv = new InteractionView(this);
		
		frame.setTitle("Management POS");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(iv);
		frame.add(login);	
		frame.setLayout(null);
		frame.setSize(1525, 800);
		
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
		ApiFuture<QuerySnapshot> queryGrocery = db.collection("Groceries").get();
		ApiFuture<QuerySnapshot> queryDelivery = db.collection("Deliveries").get();
		
		QuerySnapshot querySnapshot;
		
		try {
			querySnapshot = queryGrocery.get();
			List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
			
			for (QueryDocumentSnapshot document : documents) {
			  Grocery temp = new Grocery(document.getString("Name"), document.getString("Supplier"),document.getLong("Item ID"), document.getLong("Quantity"),document.getDouble("Price"));
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
		DocumentReference docRef = db.collection("Groceries").document();

		//asynchronously write data
		
		Map<String, Object> data = new HashMap<>();
		data.put("Name", g.getName());
		data.put("Supplier", g.getSupplier());
		data.put("Quantity", g.getQuantity());
		data.put("Price", g.getPrice());
		data.put("Item ID", g.getItemID());

		ApiFuture<WriteResult> result = docRef.set(data);
		
		try {
			System.out.println("Update time : " + result.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("Done");
	}
	
	public void updateStock(Grocery currG, Grocery updatedG) {
		
	}
	
	public void addStaff(Staff s) {

		DocumentReference docRef = db.collection("Staff").document();

		//asynchronously write data
		
		Map<String, Object> data = new HashMap<>();
		data.put("firstName", s.getFirstName());
		data.put("lastName", s.getLastName());
		data.put("address", s.getAddressName());
		data.put("email", s.getEmail());
		data.put("contactNumber", s.getContactNumber());
		data.put("role", s.getRole());
		data.put("employeeID", s.getEmployeeID());
		data.put("dob", s.getDOB());
		ApiFuture<WriteResult> result = docRef.set(data);
		
		try {
			System.out.println("Update time : " + result.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		//Consider using a thread to handle the email portion for quick GUI Response
		new EmailSender(s.getEmail(), "123456").send();
		System.out.println("Done");
	}
	
	public ArrayList<Grocery> getGrocery() {
		return Groceries;
	}
	
	public ArrayList<Delivery> getDelivery() {
		return Deliveries;
	}

	
	// Get InteractionView Class to Communicate with the Driver Class to retrieve latest information from firebase
	public static void main(String [] args) {
		Driver d = new Driver();
	}

}


//Sample
/*	public void writeData() {
	DocumentReference docRef = db.collection("users").document("alovelace");
	Map<String, Object> data = new HashMap<>();
	data.put("first", "Ada");
	data.put("last", "Lovelace");
	data.put("born", 1815);
	//asynchronously write data
	ApiFuture<WriteResult> result = docRef.set(data);

	try {
		System.out.println("Update time : " + result.get().getUpdateTime());
	} catch (InterruptedException | ExecutionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}*/