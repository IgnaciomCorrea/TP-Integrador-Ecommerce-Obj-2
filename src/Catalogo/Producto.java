package Catalogo;
import java.util.HashSet;
import java.util.Set;

public class Producto extends Vendible{
    
	private Set<Atributo<?>> atributos = new HashSet<Atributo<?>>();
	private Double precio;

	// Constructor para el caso en que no se dé la cantidad de Producto que contiene, por default es uno.
    public Producto(String sku, String nombre, String marca, Categoria categoria, String descripcion, Double descuento,Double precio) {
        super(sku, nombre, marca, categoria, descripcion, descuento);
        this.precio = precio;
    }

    // getPrecioBase retorna el precio sin aplicar el descuento del Producto.
    @Override
    public Double getPrecioBase() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    // getPrecioFinal retorna el precio habiendo aplicado el descuento particular del Producto.
    @Override
    public Double getPrecioFinal(){
    	return this.getPrecioBase() - (this.getPrecioBase() * this.descuento);
    }
    
    // Busca un atributo dinámico, en la lista atributos, con el nombre dado para validar su existencia.
    public boolean validarAtributoDinamico(String nombreBuscado) {
    	return this.atributos.stream().anyMatch(atributo->atributo.getNombre().equals(nombreBuscado));
    }
    
    public <T> void agregarAtributo(String nombre, T valor) {
    	atributos.add(new Atributo(nombre, valor));
    }
    
   
}