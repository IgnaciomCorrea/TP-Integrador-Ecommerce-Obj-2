package CriterioBusqueda;
import Catalogo.Vendible;
import exceptions.CatalogoException;

public abstract class Criterio {
	
	public void agregarCriterio(Criterio criterio) {
		throw new CatalogoException("Este Criterio no permite agregar criterios en �l.");
	}
	
	public void eliminarCriterio(Criterio criterio) {
		throw new CatalogoException("Este Criterio no permite eliminar criterios de �l.");
	}
	
	public abstract boolean validar(Vendible vendible);
}
