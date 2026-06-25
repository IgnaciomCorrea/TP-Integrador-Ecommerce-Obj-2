package Catalogo;

import java.util.ArrayList;

import java.util.List;

public class Catalogo {

	private List<StockVendible> stock;
	
	public Catalogo() {
		stock = new ArrayList<StockVendible>();
	}
	 	
	public List<StockVendible> getStock(){
		return stock;
	}
	
	public void agregarVendible(StockVendible stockVendible) {
		stock.add(stockVendible);
	}
	


	// el Vendible debe existir en la lista stock.
	public StockVendible buscarVendible(String sku) {
		return stock.stream().filter(stockVendible -> stockVendible.getSku().equals(sku))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("El producto con ID: " + sku + " no existe en el stock."));
	}

	// si tiene stock disponible
	public boolean hayStockDisponible(String nombre, int cantidad) {
		return this.buscarVendible(nombre).getStock() >= cantidad;
	}
	
	// modificar el stock una vez se vende
	public void modificarStockDisponible(String nombre, int cantidad){
		this.buscarVendible(nombre).setStock(cantidad);
	}
}
