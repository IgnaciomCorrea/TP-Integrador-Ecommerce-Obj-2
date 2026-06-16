package Catalogo;
public class StockVendible {

	private int stock;
	private Vendible vendible;
	
	public StockVendible(int stock, Vendible vendible) {
		this.stock = stock;
		this.vendible = vendible;
	}
	
	public int getStock() {
		return this.stock;
	}
	
	public void setStock(int cantidad){
		this.stock = cantidad;
	}
	
	// ver si necesitamos getVendible o getNombreVendible.
}
