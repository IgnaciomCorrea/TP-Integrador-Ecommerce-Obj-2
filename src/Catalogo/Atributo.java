package Catalogo;

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
	
	public T getValor() {
		return valor;
	}
	

}
