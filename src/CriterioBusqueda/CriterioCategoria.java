package CriterioBusqueda;

import Catalogo.Categoria;
import Catalogo.Vendible;

public class CriterioCategoria extends Criterio{

	private Categoria categoria;
	
	public CriterioCategoria(Categoria categoria) {
		this.categoria = categoria; 
	}
	
	public boolean validar(Vendible vendible) {
		return vendible.getCategoria().equals(this.categoria);
	}
}