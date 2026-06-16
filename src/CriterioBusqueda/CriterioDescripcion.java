package CriterioBusqueda;

import Catalogo.Vendible;

public class CriterioDescripcion extends CriterioSimple<String>{

	public CriterioDescripcion(String descripcion) {
		super.criterioACumplir = descripcion.toLowerCase(); 
	}
	
	public boolean validar(Vendible vendible) {
		return vendible.getDescripcion().toLowerCase().contains(this.getCriterioABumplir());
	}
}
