package CriterioBusqueda;

public abstract class CriterioSimple<T> extends Criterio{
	
	protected T criterioACumplir;
	
	public T getCriterioABumplir() {
		return this.criterioACumplir;
	}
}