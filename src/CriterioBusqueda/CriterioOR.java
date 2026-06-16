package CriterioBusqueda;

import Catalogo.Vendible;

public class CriterioOR extends CriterioCompuesto{

	// Verifica que al menos un criterio de la lista valide correctamente al vendible (al menos un true)
	@Override
	public boolean validar(Vendible vendible) {
	    return this.criterios.stream()
	                         .anyMatch(criterio -> criterio.validar(vendible));
	}

}
