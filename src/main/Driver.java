package main;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.Transaction;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;

import comparators.InvoiceSortByDate;
import resources.Customer;
import resources.Delivery;
import resources.EmailSender;
import resources.Grocery;
import resources.Invoice;
import resources.Staff;
import resources.TransactionHistory;

public class Driver {
	
	private Login login;
	private Firestore db;
	private InteractionView iv;
	private ArrayList<Grocery> Groceries;
	private ArrayList<Delivery> pastDeliveries;
	private ArrayList<Delivery> pendingDeliveries;
	private ArrayList<Staff> Staffs;
	private JFrame frame;
	private GCS gcs;
	
	final static String c_GROCERIES = "Groceries";
	final static String c_PAST_DELIVERIES = "Past Deliveries";
	final static String c_DELIVERIES = "Delivery";
	final static String c_PENDING_DELIVERIES = "Pending Deliveries";
	final static String c_STAFF = "Staff";
	final static String c_STOCK_DELIVERY = "Stock Delivery";
	final static String c_TRANSACTION_HISTORY = "Orders";
	final static String c_CUSTOMERS = "users";
	final static String FIREBASE_SERVICE = "assets//service.json";
	String text;

	public Driver() {
		
		frame = new JFrame();
		login = new Login(this);
		iv = new InteractionView(this);
		gcs = new GCS();
		
		frame.setTitle("Management System");
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
		iv.setData(Groceries, pendingDeliveries);
		frame.setBackground(Color.white);
		frame.revalidate();
		checkGroceryQty();
		gcs.connect();
	}
	
	public void logout() {
		login.reset();
		login.setVisible(true);
		iv.setVisible(false);
		frame.revalidate();
	}
	
	public void connect() {
		
		try {
			FileInputStream serviceAccount = new FileInputStream(FIREBASE_SERVICE);
			GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
			FirebaseOptions options = new FirebaseOptions.Builder()
				    .setCredentials(credentials)
				    .setDatabaseUrl("https://neutral-creep-dev.firebaseio.com")
				    .build();
			
			FirebaseApp.initializeApp(options);
			db = FirestoreClient.getFirestore();

			//Feedback system showing the current connection status on the top right of the screen, using lights
			System.out.println("Established a connection to firestore...");
			login.updateStatus(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Wrapper Function
	public void readData() {
		getAllStaff();
		getAllGroceries();
		getAllDeliveries();
	}
	
	//FIREBASE ADD GROCERIES 
	public void addGrocery(Grocery g) {
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
		data.put("toReplenish", g.getReplenishmentStatus());
		data.put("Category", g.getCategory());
		ApiFuture<WriteResult> result = docRef.set(data);
		
		try {
			System.out.println("Update time : " + result.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	// Edit Stock method to update the values of each field in firebase
	@SuppressWarnings("unused")
	public void updateGrocery(Grocery updatedG, Grocery oldG) {
				
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
		            
		            transaction.update(documentRef, "Quantity", oldQty + updatedG.getQuantity(), "Name", updatedG.getName(), "Supplier", updatedG.getSupplier(), "Price", updatedG.getPrice(), "Item ID" , updatedG.getItemID(), "Description", updatedG.getDescription(), "Url", updatedG.getImageURL());

		            return null;
		          }
		        });
		// block on transaction operation using transaction.get()
	}
	
	//Receive Stock method call to update the quantity 
	@SuppressWarnings("unused")
	public void updateGrocery(String docID, long qty) {
		
		final DocumentReference documentRef = db.collection(c_GROCERIES).document(docID);

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
		            
		            transaction.update(documentRef, "Quantity", oldQty + qty, "toReplenish", true);

		            return null;
		          }
		        });
		// block on transaction operation using transaction.get()
	}
	
	public void deleteGrocery(Grocery g) {
		ApiFuture<WriteResult> writeResult = db.collection(c_GROCERIES).document(g.getDocID()).delete();

		try {
			System.out.println("Update time : " + writeResult.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	//FIREBASE ADD STAFF 
	public void createStaff(Staff s) {
		//This method adds the staff details in the Authentication
		CreateRequest request = new CreateRequest()
			    .setEmail(s.getEmail())
			    .setEmailVerified(true)
			    .setPassword("<123456>")
			    .setDisplayName(s.getFirstName() + " " + s.getLastName())
			    .setDisabled(false);

			
		try {
			UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
			//System.out.println("Successfully created new user: " + userRecord.getUid());
			s.setUID(userRecord.getUid());
			
			JOptionPane.showMessageDialog(null, "Profile Created" , "Add New Staff", 1);
	
			addStaff(s);		
		} catch (FirebaseAuthException e) {
			System.out.println("Something went wrong with the creation of a new Staff");
			e.printStackTrace();
		}
	}
		
	public void addStaff(Staff s) {
		//This method adds the staff into the collections
		DocumentReference docRef = db.collection(c_STAFF).document(s.getUID());

		//asynchronously write data
		
		Map<String, Object> data = new HashMap<>();
		data.put("firstName", s.getFirstName());
		data.put("lastName", s.getLastName());
		Map<String, String> address = new HashMap<>();
			address.put("street", s.getStreet());
			address.put("postalCode", s.getPostal());
			address.put("unit", s.getUnit());
		data.put("address", address);
		data.put("email", s.getEmail());
		data.put("contactNumber", s.getContactNumber());
		data.put("role", s.getRole());
		Map<String, String> dob = new HashMap<>();
			dob.put("day", s.getDD());
			dob.put("month", s.getMM());
			dob.put("year", s.getYY());
		data.put("dob", dob);
		data.put("gender", s.getGender());
		data.put("UID", s.getUID());
		ApiFuture<WriteResult> result = docRef.set(data);
		
		try {
			System.out.println("Update time : " + result.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

	}
	
	public void updateStaff(Staff s) {
		
		DocumentReference docRef = db.collection(c_STAFF).document(s.getDocID());

		//asynchronously write data
		
		Map<String, Object> data = new HashMap<>();
		data.put("firstName", s.getFirstName());
		data.put("lastName", s.getLastName());
		Map<String, String> address = new HashMap<>();
			address.put("street", s.getStreet());
			address.put("postalCode", s.getPostal());
			address.put("unit", s.getUnit());
		data.put("address", address);
		data.put("email", s.getEmail());
		data.put("contactNumber", s.getContactNumber());
		data.put("role", s.getRole());
		data.put("employeeID", s.getUID());
		Map<String, String> dob = new HashMap<>();
			dob.put("day", s.getDD());
			dob.put("month", s.getMM());
			dob.put("year", s.getYY());
		data.put("dob", dob);
		data.put("gender", s.getGender());
		data.put("UID", s.getUID());
		ApiFuture<WriteResult> result = docRef.set(data);
		
		try {
			System.out.println("Update time : " + result.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteStaff(Staff s) {
		
		try {
			
			ApiFuture<WriteResult> writeResult = db.collection(c_STAFF).document(s.getDocID()).delete();
			FirebaseAuth.getInstance().deleteUser(s.getUID()); 
			
			System.out.println("Update time : " + writeResult.get().getUpdateTime());
		
		} catch (InterruptedException | ExecutionException | FirebaseAuthException e) {
			System.out.println("Delete user error");
		}
	}
	
	//FIREBASE ADD IMAGE TO GROCERY
	public String storeImageToGCS(String name, String url) {
		return gcs.write(name, url);
	}

	// GETTER - Return a populated Arraylist of Objects
	public ArrayList<Grocery> getGrocery() {
		checkGroceryQty();
		return Groceries;
	}
	
	public ArrayList<Grocery> getGroceryMutedNotifications() {
		return Groceries;
	}
	
	public ArrayList<Delivery> getPendingDelivery() {
		return pendingDeliveries;
	}
	
	public ArrayList<Delivery> getPastDelivery() {
		return pastDeliveries;
	}
	
	@SuppressWarnings("unchecked")
	public void getAllStaff() {
		
		Staffs = new ArrayList<>();
		ApiFuture<QuerySnapshot> queryGrocery = db.collection(c_STAFF).get();

		QuerySnapshot querySnapshot;
		
		try {
			querySnapshot = queryGrocery.get();
			List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
			
			for (QueryDocumentSnapshot document : documents) {
			  Staff s = new Staff(	document.getString("firstName"), 				document.getString("lastName"), 
					  				(Map<String, String>)document.get("address") ,	document.getString("email"), 
					  				document.getString("role"), 					document.getString("gender"), 
					  				(Map<String, String>)document.get("dob"), 		document.getString("contactNumber"), 
					  				document.getId(), 								document.getString("UID"));
			  Staffs.add(s);
			}		
		} catch (InterruptedException | ExecutionException e) {
			System.out.println("No staff | Reset Connection");
		}
	}
	
	public ArrayList<Staff> getStaff() {
		getAllStaff();
		return Staffs;
	}
	
	public ArrayList<Customer> getCustomer() {
		ArrayList<Customer> Customers = new ArrayList<>();
		ApiFuture<QuerySnapshot> query = db.collection(c_CUSTOMERS).get();

		QuerySnapshot querySnapshot;
		
		try {
			querySnapshot = query.get();
			List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
			
			for (QueryDocumentSnapshot document : documents) {
				@SuppressWarnings("unchecked")
				Customer customer = new Customer(document.getString("firstName"), document.getString("lastName"), document.getString("id"), (HashMap<String, String>)document.get("dob"),  (HashMap<String, String>)document.get("address"), document.getString("contactNum") );
				Customers.add(customer);
			}		
		} catch (InterruptedException | ExecutionException e) {
			System.out.println("No Customers | Reset Connection");
		}
		return Customers;
	}
	
	public ArrayList<TransactionHistory> getTransactionHistory() {
		// Get the collections (Orders)
		
		ArrayList<Customer> Customers = getCustomer();
		ArrayList<TransactionHistory> TH = new ArrayList<>();
		
		ApiFuture<QuerySnapshot> query = db.collection(c_TRANSACTION_HISTORY).get();

		QuerySnapshot querySnapshot;
		
		try {
			querySnapshot = query.get();
			List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
			
			for (QueryDocumentSnapshot document : documents) {
			  TransactionHistory th = new TransactionHistory(document.getId(), document.getString("customerId"), document.getString("status"), document.getString("type"), document.getDouble("totalAmount"), document.getTimestamp("dateOfTransaction").toDate());
			  
				for(Customer c: Customers) {
					if (document.getString("customerId").equals(c.getUID())) {
						th.setCustomer(c);
						th.setArray(document.get("items"));
					}
				}
			  
			  
			  TH.add(th);
			}		
		} catch (InterruptedException | ExecutionException e) {
			System.out.println("No Transaction History | Reset Connection");
		}
		return TH;
	}
	
	public ArrayList<Invoice> getReplenishmentList() {
		//Retrieve specific invoices
		//Find the invoice and update the groceries from here
		ArrayList<Invoice> Invoices = new ArrayList<>();

		ApiFuture<QuerySnapshot> queryDelivery = db.collection(c_STOCK_DELIVERY).get();
		
		try {
			QuerySnapshot querySnapshot = queryDelivery.get();
			List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
			
			for (DocumentSnapshot document : documents) {
				 Invoice i = new Invoice(document.getString("Invoice Number"), document.getString("Status"), document.get("Replenishment List") , document.getTimestamp("Creation Date"), document.getId(), document.getString("Delivery Request Type"));
				 Invoices.add(i);
			}
		} catch (InterruptedException | ExecutionException e) {
			System.out.println("No replenishment list | Reset Connection");
		}
		
		Collections.sort(Invoices, new InvoiceSortByDate());
		Collections.reverse(Invoices);
		return Invoices;
	}

	// asynchronously retrieve data
	public void getAllGroceries() {
		Groceries = new ArrayList<>();
		
		ApiFuture<QuerySnapshot> queryGrocery = db.collection(c_GROCERIES).get();

		QuerySnapshot querySnapshot;
		
		try {
			querySnapshot = queryGrocery.get();
			List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
			
			for (QueryDocumentSnapshot document : documents) {
			  Grocery temp = new Grocery(document.getString("Name"), document.getString("Supplier"),document.getString("Item ID"), document.getLong("Quantity"),document.getDouble("Price"), document.getId(), document.getString("Description"), document.getString("Url"), document.getBoolean("toReplenish"), document.getString("Category"));
			  Groceries.add(temp);
			}			
		} catch (InterruptedException | ExecutionException e) {
			System.out.println("No Groceries | Reset Connection");
		}
	}
	
	// asynchronously retrieve data
	@SuppressWarnings("unchecked")
	public void getAllDeliveries() {
		int pendingAssignedDeliveriesCounter = 0;
		int pendingUnassignedDeliveriesCounter = 0;
		int pastDeliveriesCounter = 0;
		
		/*
		 * Remove Staff List to all the Delivery Table
		 * Assign the Delivery Staff Name and ID to each Delivery Object
		 * Filter Conditions to filter Late Status as well
		 */
		
		try {
			pastDeliveries = new ArrayList<>();
			pendingDeliveries = new ArrayList<>();
		
			//Group query for all Delivery Staff, to populate Pending Delivery Table
			final Query queryPendingDelivery = db.collectionGroup(c_PENDING_DELIVERIES);
			ApiFuture<QuerySnapshot> querySnapshot = queryPendingDelivery.get();
			
			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				Delivery temp = new Delivery(	document.getString("transactionId"), 
												document.getString("customerId"), 
												document.getString("employeeId"), 
												"Delivering", 
												document.getTimestamp("dateOfTransaction"), 
												document.getString("name"), 
												"Type",
												(Map<String,String>)document.get("address"),
												document.getDouble("totalAmount"),
												document.get("items"));
					temp.setFormattedDateToOrdinaryDateObject((Map<Object, Object>)document.get("timeArrival"), "expectedTime");
					temp.setEmployeeName(retrieveEmployeeObjectForDelivery(document.getString("employeeId")));
					pendingDeliveries.add(temp);
					pendingAssignedDeliveriesCounter++;
			}
			System.out.println("Assigned Pending Deliveries = " + pendingAssignedDeliveriesCounter);
			
			//Single Query to Collection - Delivery to populate the Deliveries not picked up by the delivery staff
			ApiFuture<QuerySnapshot> queryDelivery = db.collection(c_DELIVERIES).get();
			 
			QuerySnapshot queryDeliverySnapshot = queryDelivery.get();
			List<QueryDocumentSnapshot> documents = queryDeliverySnapshot.getDocuments();
			
			
			for (DocumentSnapshot document : documents) {
				
				//System.out.println("Gotcha - " + document.getString("transactionId"));
				Delivery temp = new Delivery(	document.getString("transactionId"), 
												document.getString("customerId"), 
												"", 
												"Packaged", 
												document.getTimestamp("dateOfTransaction"), 
												document.getString("name"), 
												"Type",
												(Map<String,String>)document.get("address"),
												document.getDouble("totalAmount"),
												document.get("items"));
					temp.setFormattedDateToOrdinaryDateObject((Map<Object, Object>)document.get("timeArrival"), "expectedTime");
					temp.setEmployeeName("<Waiting for pickup>");
					pendingDeliveries.add(temp);
					pendingUnassignedDeliveriesCounter++;
			}
			System.out.println("Unasssigned Pending Deliveries = " + pendingUnassignedDeliveriesCounter);
			
			//Group Query for all Past Deliveries, to populate Delivery History Table
			ApiFuture<QuerySnapshot> queryPastDelivery = db.collection(c_PAST_DELIVERIES).get();
			QuerySnapshot queryPastSnapshot = queryPastDelivery.get();
			documents = queryPastSnapshot.getDocuments();
				
			for (DocumentSnapshot document : documents) {
				Delivery temp = new Delivery(	document.getString("transactionId"), 
												document.getString("customerId"), 
												document.getString("employeeId"), 
												document.getString("status"),
												document.getTimestamp("dateOfTransaction"), 
												document.getString("name"), 
												"purchase",
												(Map<String,String>)document.get("address"),
												document.getDouble("totalAmount"),
												document.get("items"));
				temp.setFormattedDateToOrdinaryDateObject((Map<Object, Object>)document.get("actualTime"), "actualTime");
				temp.setFormattedDateToOrdinaryDateObject((Map<Object, Object>)document.get("expectedTime"), "expectedTime");
				temp.setEmployeeName(retrieveEmployeeObjectForDelivery(document.getString("employeeId")));

			
				pastDeliveries.add(temp);
				pastDeliveriesCounter++;

			}	
			System.out.println("Past Deliveries = " + pastDeliveriesCounter);
				
		} catch (NullPointerException ee) {
			System.out.println("No deliveries");
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("No deliveries | Reset Connection");
		} catch (ExecutionException e) {
			System.out.println("No deliveries | Restart Program");
		}
	}

	public String retrieveEmployeeObjectForDelivery(String eID) {
		for ( Staff s : Staffs) {
			if (s.getUID().equals(eID))
				return s.getFirstName() + " " + s.getLastName();
		}
		return "<Not Set>";
	}
	
	public String getLowStock() {
		String t = text;
		return t;
	}
	
	public void checkGroceryQty() {
		try {
			text = "";
			String text1 = "";
			int counter = 0;
			for(Grocery g : Groceries) {
				if (g.getQuantity() < 50  && g.getReplenishmentStatus() == true) {
					text += g.getName() + "\n";
					text1 += (counter+1) + ". " + g.getName() + "\n";
					counter++;
				}
			}
			if ( counter > 0)
				JOptionPane.showMessageDialog(iv.getMainPanel(), text1 , "LOW QUANTITY STOCKS", 1);
		} catch (NullPointerException e) {}
	}
	
	//FIREBASE ADD REPLENISHMENT REQUEST
	public String sendReplenishmentRequest(ArrayList<Grocery> sol, String status) {

		//Update the stock status in Firebase
		CollectionReference docGroceryRef = db.collection(c_GROCERIES);
		DocumentReference docRef = db.collection(c_STOCK_DELIVERY).document();

		//asynchronously write data		
		Map<String, Object> data = new HashMap<>();
	
		ArrayList<Object> temp = new ArrayList<>();
		for ( Grocery g : sol) {
			String a = g.getName() + "|" + g.getOrderingQty() + "|" + g.getPrice() + "|" + g.getSupplier();
			temp.add(a);
		}
		
		data.put("Creation Date", Timestamp.of(new Date()));
		
		data.put("Replenishment List", temp);
		data.put("Status", "Pending");
		String invoiceNo = generateInvoiceNumber();
		data.put("Invoice Number", invoiceNo);
		data.put("Delivery Request Type", status);
		
		ApiFuture<WriteResult> result = docRef.set(data);
		
		try {
			System.out.println("Update time : " + result.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		//Update the Database that a replenishment has been made.
		//Change toReplenish to false;
		for ( int i = 0; i < sol.size(); i++) {
			Map<String, Object> groceryMap = new HashMap<>();

			
			groceryMap.put("Name", sol.get(i).getName());
			groceryMap.put("Supplier", sol.get(i).getSupplier());
			groceryMap.put("Quantity", sol.get(i).getQuantity());
			groceryMap.put("Price", sol.get(i).getPrice());
			groceryMap.put("Item ID", sol.get(i).getItemID());
			groceryMap.put("Description", sol.get(i).getDescription());
			groceryMap.put("Url", sol.get(i).getImageURL());
			groceryMap.put("toReplenish", false);
			groceryMap.put("Category", sol.get(i).getCategory());
			ApiFuture<WriteResult> res = docGroceryRef.document(sol.get(i).getDocID()).set(groceryMap);
			
			try {
				System.out.println("Grocery Update time : " + res.get().getUpdateTime());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		return invoiceNo;

	}

	public void processReplenishmentRequest(Invoice i) {

		for ( String a : i.getReplenishmentList() ) {
			boolean toBreak = false;
			String [] temp = a.split("\\|");
			
				for ( Grocery g : Groceries) {
					if (g.getName().equalsIgnoreCase(temp[0])) {
						//Process Update with docID and Price
						updateGrocery(g.getDocID(), Long.parseLong(temp[1]));
						updateReplenishmentStatus(i.getDocID());
						toBreak = true;
					}
					
					if (toBreak == true)
						break;
				}
		}
	}
    
	@SuppressWarnings("unused")
	public void updateReplenishmentStatus(String docID) {
		//Set to true to ensure that if QTY < 50 then it will call the lowStock function
		
		System.out.println("Driver: Recieved an update call from DRIVER to Update Replenishment Status");
		
		final DocumentReference documentRef = db.collection(c_STOCK_DELIVERY).document(docID);

		// run an asynchronous transaction
		ApiFuture<Void> transaction =
		    db.runTransaction(
		        new Transaction.Function<Void>() {

				@Override
		          public Void updateCallback(Transaction transaction) throws Exception {
		            // retrieve document and update fields
		        	  
		            DocumentSnapshot snapshot = transaction.get(documentRef).get();
		            
		            transaction.update(documentRef, "Status", "Completed");

		            return null;
		          }
		        });
				// block on transaction operation using transaction.get()
	}

	public String generateInvoiceNumber() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		sdf.setTimeZone(TimeZone.getDefault());
		
		int hh = calendar.get(Calendar.HOUR_OF_DAY);
		int mm = calendar.get(Calendar.MINUTE);
		
		String invoice = "";
		
		
		if ( hh >= 10)
			invoice += sdf.format(calendar.getTime()) + hh;
		else 
			invoice += sdf.format(calendar.getTime()) + "0" + hh;
		
		if( mm >= 10 )
			invoice += mm;
		else
			invoice += "0" + mm;

		System.out.println("Invoice Number Generated = " + invoice);
		return invoice;
	}

	public void resetLowStock() {
		text = "";
	}
	
	//Google Cloud Storage Class for Image URL
	public class GCS {
		Storage storage;
		public void connect() {

			try {
				FileInputStream serviceAccount = new FileInputStream(FIREBASE_SERVICE);

				storage = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
						  .setProjectId("neutral-creep-dev").build().getService();
			
				System.out.println("Established a connection to GCS...");
			
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
				
		public String write(String blobName, String path) {
			
			InputStream inputStream;
			StorageClient s = StorageClient.getInstance();
			try {
				inputStream = new FileInputStream(new File(path));
				
			    Blob blob = s.bucket("neutral-creep-dev.appspot.com").create(blobName, inputStream , Bucket.BlobWriteOption.userProject("neutral-creep-dev"));
			    
			    blob.getStorage().createAcl(blob.getBlobId(), Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
			    
			    System.out.println("Media URL: " + blob.getMediaLink());
				
				return blob.getMediaLink();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			return "NOTHING";
		}
	}
	
	public void updateLoginStatus() {
		login.updateStatus(2);
	}
	
	public boolean checkInternetConnection () {
		
		try {
			final URL url = new URL("http://www.google.com");
			final URLConnection conn = url.openConnection();
			conn.getInputStream();
			return true;
		} catch (Exception e) {
			updateLoginStatus();
			return false;
		}
	}
}
