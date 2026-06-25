package Catalogo;


public class ItemVendible {
	
	private int cantidad;
	private Vendible vendible;
	
	public ItemVendible(int cantidad, Vendible vendible) {
		this.validarCantidad(cantidad);
		this.cantidad = cantidad;
		this.vendible = vendible;
	}
	
	public Double getPrecioFinal() {
		return this.vendible.getPrecioFinal() * cantidad;
	}
	
	public String getNombre() {
		return vendible.getNombre();
	}
	
	public Double getPeso() {
		return this.vendible.getPeso() * cantidad;
	}

	public String getSku(){
		return vendible.getSku();
	}

	public int getCantidad(){
		return this.cantidad;
	}

	private void validarCantidad(int cantidad) {
		if (cantidad < 1) {
			throw new IllegalArgumentException("La cantidad debe ser 1 o más!");
		}
	}
}
