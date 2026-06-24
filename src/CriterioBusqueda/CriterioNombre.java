package CriterioBusqueda;
import Catalogo.Vendible;


public class CriterioNombre extends Criterio{

	private String texto;
	
	public CriterioNombre(String nombre) {
		this.texto = nombre.toLowerCase(); 
	}
	
	public boolean validar(Vendible vendible) {
		return vendible.getNombre().toLowerCase().contains(this.texto);
	}
	
}
