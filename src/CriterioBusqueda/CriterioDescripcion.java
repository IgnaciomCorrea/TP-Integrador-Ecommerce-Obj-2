package CriterioBusqueda;

import Catalogo.Vendible;

public class CriterioDescripcion extends Criterio{

	private String texto;
	
	public CriterioDescripcion(String descripcion) {
		this.texto = descripcion.toLowerCase(); 
	}
	
	public boolean validar(Vendible vendible) {
		return vendible.getDescripcion().toLowerCase().contains(this.texto);
	}
}
