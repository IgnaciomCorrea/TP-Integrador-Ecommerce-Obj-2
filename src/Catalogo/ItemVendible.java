package Catalogo;


public class ItemVendible {
	
	private int cantidad;
	private Vendible vendible;
	
	public ItemVendible(int cantidad, Vendible vendible) {
		this.cantidad = cantidad;
		this.vendible = vendible;
	}
	
	public Double getPrecioBase() {
		return this.vendible.getPrecioBase() * cantidad;
	}
	
	public String getNombre() {
		return vendible.getNombre();
	}
	
}
