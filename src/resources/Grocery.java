package resources;

public class Grocery {

	String name, supplier, docID, itemID, description, imageURL, category;
	long orderingQty, quantity;
	double price;
	boolean toReplenish;
	//Why do i have 3 constructors that differs alot?
	public Grocery() {
		name = "";
		supplier ="";
		docID = "";
		itemID = "";
		description = "";
		quantity = 0;
		imageURL = "www.sample.com";
		category = "Others";
		price = 0.00;
		toReplenish = false;
		orderingQty = 500;

	}
	
	public Grocery (String name, long qty, double price) {
		this.name=name;
		this.quantity=qty;
		this.price=price;
	}
	
	public Grocery(String newName, String newSupplier, String newItemID, long startQty, double newPrice, String documentID, String newDesc, String newURL, boolean replenishmentStatus , String cat) {
		name = newName;
		supplier = newSupplier;
		quantity = startQty;
		price = newPrice;
		itemID = newItemID;
		description = newDesc;
		docID = documentID;
		imageURL = newURL;
		toReplenish = replenishmentStatus;
		category = cat;
		orderingQty = 500;
	}
	public Grocery(String newName, String newSupplier, String newItemID, long startQty, double newPrice, String documentID, String newDesc, boolean replenishmentStatus, String cat) {
		name = newName;
		supplier = newSupplier;
		quantity = startQty;
		price = newPrice;
		itemID = newItemID;
		description = newDesc;
		docID = documentID;
		imageURL = "www.sample.com";
		toReplenish = replenishmentStatus;
		category = cat;
		orderingQty = 500;
	}
	
	public void addStock(int qty) {
		quantity += qty;
	}
	
	public void removeStock(int qty) {
		quantity -= qty;
	}
	
	public long getQuantity() {
		return quantity;
	}
	
	public double getPrice() {
		return price;
	}
	
	public String getItemID() {
		return itemID;
	}
	
	public String getSupplier() {
		return supplier;
	}
	
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public String getImageURL() {
		return imageURL;
	}
	public boolean getReplenishmentStatus() {
		return toReplenish;
	}
	public String getCategory() {
		return category;
	}
	
	public void setPrice(double newPrice) {
		price = newPrice;
	}
	
	public void setQuantity(long qty) {
		quantity = qty;
	}
	
	public void setItemID(String id) {
		itemID = id;
	}
	
	public void setSupplier(String newSupp) {
		supplier = newSupp;
	}
	
	public void setName(String newName) {
		name = newName;
	}
	public void setOrderingQty(long a) {
		orderingQty = a;
	}
	
	public long getOrderingQty() { 
		return orderingQty;
	}
	public String getDocID() {
		return docID;
	}
	
	public void setDocID(String doc) {
		docID = doc;
	}
	public void setDescription(String d) {
		description = d;
	}
	public void setImageUrl(String url) {
		imageURL = url;
	}
	public void setReplenishmentStatus(boolean input) {
		toReplenish = input;
	}
	public void setCategory(String input) {
		category = input;
	}
	
	public String toString() {
		return "Name: " + name + " Supplier: " + supplier + " Amount: " + quantity + " Price: " + price;
	}
}
