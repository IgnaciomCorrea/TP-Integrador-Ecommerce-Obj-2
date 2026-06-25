package Catalogo;
import java.util.ArrayList;
import java.util.List;

public class Paquete extends Vendible{
   
	private List<Vendible> paquetes;

    public Paquete(String sku, String nombre, String marca, Categoria categoria, String descripcion, Double descuento) {
    	super(sku, nombre, marca, categoria, descripcion, descuento);
    	paquetes = new ArrayList<Vendible>();
    }    
    
    // Itera por todos los elementos de la lista paquetes y pide su precioBase, ya sea otro
    // paquete o un producto. 
    @Override
    public Double getPrecioBase() {
    	return paquetes.stream()
    					.mapToDouble(paquete->paquete.getPrecioBase())
        				.sum();
    }

    @Override
    public Double getPrecioFinal(){
    	Double precioBase = this.getPrecioBase();
    	return precioBase - (precioBase * this.descuento);
    }

    public void agregarVendible(Vendible vendible){
    	paquetes.add(vendible);
    }
    
    public void eliminarVendible(Vendible vendible){
    	paquetes.remove(vendible);
    }

    // Retorna el primer producto encontrado con el nombre dado, caso contrario retorna null.
    public Vendible obtenerProducto(String nombre) {
        return paquetes.stream()
                       .filter(paquete -> paquete.getNombre().equals(nombre))
                       .findFirst()                                         
                       .orElse(null);
    }
    
    public Double getPeso() {
    	return paquetes.stream().mapToDouble(paquete-> paquete.getPeso()).sum();
    }
}