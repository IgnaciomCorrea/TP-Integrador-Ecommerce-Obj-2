package CriterioBusqueda;

import Catalogo.Vendible;

public class CriterioAND extends CriterioCompuesto{

	// Verifica que todos los criterios de la lista validan correctamente al vendible (sean true)
	@Override
	public boolean validar(Vendible vendible) {
	    return this.criterios.stream()
	                         .allMatch(criterio -> criterio.validar(vendible));
	}
	
}
