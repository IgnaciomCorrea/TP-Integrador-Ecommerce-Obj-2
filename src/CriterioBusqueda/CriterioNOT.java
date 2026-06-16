package CriterioBusqueda;

import Catalogo.Vendible;

public class CriterioNOT extends Criterio{

	private Criterio criterio;
	
	public CriterioNOT(Criterio criterio) {
		this.criterio  = criterio;
	}
	
	// Aquello que retorne la validación del criterio sobre el vendible, 
	// retornado lo opuesto.
	public boolean validar(Vendible vendible){
		return !(criterio.validar(vendible));
	}
}
