
public class Atributo<T> {

	private String nombre;
	private T valor;
	
	public Atributo(String nombre, T valor) {
		this.nombre = nombre;
		this.valor = valor;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public T getValor() {
		return valor;
	}
	
	public void setValor(T valor) {
		this.valor = valor;
	}
}
