
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import resources.Delivery;
import resources.Grocery;

import com.google.firebase.FirebaseApp;

public class Driver {
	
	//private Login login;
	private Firestore db;
	private InteractionView iv;
	private ArrayList<Grocery> Groceries;
	private ArrayList<Delivery> Deliveries;
	
	public Driver() {
		//login = new Login();
		Groceries = new ArrayList<>();
		Deliveries = new ArrayList<>();
		connect();
		readData();
		for ( Grocery a : Groceries) 
			System.out.println(a.toString());
		for ( Delivery a : Deliveries) 
			System.out.println(a.toString());
		
		iv = new InteractionView(Groceries, Deliveries);
	}
	
	public void connect() {
		
		try {
			
			FileInputStream serviceAccount = new FileInputStream("C:\\Users\\USER\\Downloads\\service.json");
			GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
			FirebaseOptions options = new FirebaseOptions.Builder()
				    .setCredentials(credentials)
				    .setDatabaseUrl("https://pos-system-bb06b.firebaseio.com")
				    .build();
			
			FirebaseApp.initializeApp(options);
			db = FirestoreClient.getFirestore();

			System.out.println("hello world");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readData() {
		// asynchronously retrieve all users
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
				  Delivery temp2 = new Delivery(document2.getString("transactionID"), document2.getString("customerID"),document2.getString("employeeID"), document2.getString("deliveryID"));
				  Deliveries.add(temp2);  
			}
			
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Change this to read data from the database
		// - Delivery List

	}
	
	public void writeData() {
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

	}
	
	// Get InteractionView Class to Communicate with the Driver Class to retrieve latest information from firebase

}