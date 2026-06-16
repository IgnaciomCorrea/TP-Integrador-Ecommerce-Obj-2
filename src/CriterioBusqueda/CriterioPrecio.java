package CriterioBusqueda;
import Catalogo.Vendible;

public class CriterioPrecio extends CriterioSimple<Double>{

	public CriterioPrecio(Double  precio) {
		super.criterioACumplir = precio; 
	}
	
	
	public boolean validar(Vendible vendible) {
		return vendible.getPrecioBase() <= this.getCriterioABumplir();
	}
}
