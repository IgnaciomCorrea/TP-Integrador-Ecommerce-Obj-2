package CriterioBusqueda;

import java.util.ArrayList;
import java.util.List;

public abstract class CriterioCompuesto extends Criterio{

	protected List<Criterio> criterios;
	
	public CriterioCompuesto() {
		criterios = new ArrayList<Criterio>();
	}
	
	@Override
	public void agregarCriterio(Criterio criterio) {
		criterios.add(criterio);
	}
	
	@Override
	public void eliminarCriterio(Criterio criterio) {
		criterios.remove(criterio);
	}
	
}
