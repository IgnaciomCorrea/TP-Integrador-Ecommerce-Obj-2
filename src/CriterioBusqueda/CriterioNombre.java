package CriterioBusqueda;
import Catalogo.Vendible;


public class CriterioNombre extends CriterioSimple<String>{

	public CriterioNombre(String nombre) {
		super.criterioACumplir = nombre.toLowerCase(); 
	}
	
	public boolean validar(Vendible vendible) {
		return vendible.getNombre().toLowerCase().contains(this.getCriterioABumplir());
	}
}
