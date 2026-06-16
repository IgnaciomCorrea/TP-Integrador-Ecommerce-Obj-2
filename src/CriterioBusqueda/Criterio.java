package CriterioBusqueda;
import Catalogo.Vendible;

public abstract class Criterio {
	
	public void agregarCriterio(Criterio criterio) {
		throw new RuntimeException("Este Criterio no permite agregar criterios en él.");
	}
	
	public void eliminarCriterio(Criterio criterio) {
		throw new RuntimeException("Este Criterio no permite eliminar criterios de él.");
	}
	
	public abstract boolean validar(Vendible vendible);
}
