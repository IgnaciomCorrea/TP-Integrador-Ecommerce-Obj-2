package Catalogo;


public class ItemVendible {
	
	private int cantidad;
	private Vendible vendible;
	
	public ItemVendible(int cantidad, Vendible vendible) {
		this.validarCantidad(cantidad);
		this.cantidad = cantidad;
		this.vendible = vendible;
	}
	
	public Double getPrecioBase() {
		return this.vendible.getPrecioBase() * cantidad;
	}
	
	public String getNombre() {
		return vendible.getNombre();
	}
	
	public Double getPeso() {
		return this.vendible.getPeso() * cantidad;
	}
	
	private void validarCantidad(int cantidad) {
		if (cantidad < 1) {
			throw new IllegalArgumentException("La cantidad debe ser 1 o más!");
		}
	}
}
