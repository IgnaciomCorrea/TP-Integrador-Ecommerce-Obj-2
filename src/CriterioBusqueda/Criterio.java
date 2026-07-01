package CriterioBusqueda;
import Catalogo.Vendible;
import exceptions.CatalogoExcepcion;

public abstract class Criterio {
	
	public void agregarCriterio(Criterio criterio) {
		throw new CatalogoExcepcion("Este Criterio no permite agregar criterios en �l.");
	}
	
	public void eliminarCriterio(Criterio criterio) {
		throw new CatalogoExcepcion("Este Criterio no permite eliminar criterios de �l.");
	}
	
	public abstract boolean validar(Vendible vendible);
}
