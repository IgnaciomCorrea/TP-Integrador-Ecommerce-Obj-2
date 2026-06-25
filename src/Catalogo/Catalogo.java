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
	
	// agregar funcionalidades de:
	// recorrer lista para buscar X producto y si tiene stock disponible
	
	// modificar el stock una vez se vende.
}
