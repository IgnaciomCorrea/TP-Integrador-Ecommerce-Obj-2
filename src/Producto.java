import java.util.HashSet;
import java.util.Set;

public class Producto extends Vendible{
    
	private Set<Atributo<?>> atributos = new HashSet<Atributo<?>>();
	private Double precio;

	
    public Producto(String sku, String nombre, String marca, Categoria categoria, String descripcion, Double descuento,Double precio) {
        super(sku, nombre, marca, categoria, descripcion, descuento);
    	cantidad = 1;
        this.precio = precio;
    }

    public Producto(String sku, String nombre, String marca, Categoria categoria, String descripcion, Double descuento, int cantidad, Double precio) {
    	super(sku, nombre, marca, categoria, descripcion, descuento);
    	this.cantidad = cantidad;
        this.precio = precio;
    }

    public Double getPrecioBase() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
    
    public Double getPrecioFinal(){
    	return this.getPrecioBase() - (this.getPrecioBase() * this.descuento);
    }
       
    public boolean validarAtributoDinamico(String nombreBuscado) {
    	return this.atributos.stream().anyMatch(atributo->atributo.getNombre().equals(nombreBuscado));
    }
    
    public <T> void agregarAtributo(String nombre, T valor) {
    	atributos.add(new Atributo(nombre, valor));
    }
    
   
}