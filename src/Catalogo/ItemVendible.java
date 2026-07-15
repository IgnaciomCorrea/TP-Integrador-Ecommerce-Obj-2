package Catalogo;


import reportes.ReportVisitor;

public class ItemVendible {
	
	private int cantidad;
	public Vendible vendible;
	
	public ItemVendible(int cantidad, Vendible vendible) {
		this.validarCantidad(cantidad);
		this.cantidad = cantidad;
		this.vendible = vendible;
	}

	public Double getPrecioBase() {
		return this.vendible.getPrecioBase() * cantidad;
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
			throw new IllegalArgumentException("La cantidad debe ser 1 o m�s!");
		}
	}

	public void accept(ReportVisitor visitor) {
		if (vendible instanceof Producto) {
			visitor.visitProducto((Producto) vendible, this.cantidad, this.getPrecioFinal());
		} else if (vendible instanceof Paquete) {
			visitor.visitPaquete((Paquete) vendible, this.cantidad, this.getPrecioFinal());
		}
	}
}
