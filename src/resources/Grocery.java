package resources;

public class Grocery {

	String name, supplier;
	long quantity, itemID;
	double price;
	
	public Grocery() {
		name = "";
		supplier ="";
		itemID = 0;
		quantity = 0;
		price = 0.00;
	}
	public Grocery(String newName, String newSupplier, long newItemID, long startQty, double newPrice) {
		name = newName;
		supplier = newSupplier;
		quantity = startQty;
		price = newPrice;
		itemID = newItemID;
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
	
	public void setPrice(double newPrice) {
		price = newPrice;
	}
	
	public double getPrice() {
		return price;
	}
	
	public long getItemID() {
		return itemID;
	}
	
	public String getSupplier() {
		return supplier;
	}
	public String getName() {
		return name;
	}
	
	public String toString() {
		return "Name: " + name + " Supplier: " + supplier + " Amount: " + quantity + " Price: " + price;
	}
}
