package resources;

public class Grocery {

	String name, supplier, docID, itemID, description, imageURL;
	long quantity;
	double price;
	
	public Grocery() {
		name = "";
		supplier ="";
		itemID = "";
		quantity = 0;
		description = "";
		price = 0.00;
		imageURL = "www.sample.com";
	}
	public Grocery(String newName, String newSupplier, String newItemID, long startQty, double newPrice, String documentID, String newDesc, String newURL) {
		name = newName;
		supplier = newSupplier;
		quantity = startQty;
		price = newPrice;
		itemID = newItemID;
		description = newDesc;
		docID = documentID;
		imageURL = newURL;
	}
	public Grocery(String newName, String newSupplier, String newItemID, long startQty, double newPrice, String documentID, String newDesc) {
		name = newName;
		supplier = newSupplier;
		quantity = startQty;
		price = newPrice;
		itemID = newItemID;
		description = newDesc;
		docID = documentID;
		imageURL = "www.sample.com";
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
	
	public String toString() {
		return "Name: " + name + " Supplier: " + supplier + " Amount: " + quantity + " Price: " + price;
	}
}
