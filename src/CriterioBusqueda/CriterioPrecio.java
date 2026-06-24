package CriterioBusqueda;
import Catalogo.Vendible;

public class CriterioPrecio extends Criterio{

	private double precioMaximo;
	
	public CriterioPrecio(Double  precio) {
		this.precioMaximo = precio; 
	}
	
	
	public boolean validar(Vendible vendible) {
		return vendible.getPrecioBase() <= this.precioMaximo;
	}
}
