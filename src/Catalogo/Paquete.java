package Catalogo;
import java.util.ArrayList;
import java.util.List;

public class Paquete extends Vendible{

	private List<ItemVendible> vendibles;

    public Paquete(String sku, String nombre, String marca, Categoria categoria, String descripcion, Double descuento) {
    	super(sku, nombre, marca, categoria, descripcion, descuento);
    	vendibles = new ArrayList<ItemVendible>();
    }

    // Itera por todos los elementos de la lista paquetes y pide su precioBase, ya sea otro
    // paquete o un producto.
    @Override
    public Double getPrecioBase() {
    	return vendibles.stream()
    					.mapToDouble(vendible->vendible.getPrecioBase())
        				.sum();
    }

    @Override
    public Double getPrecioFinal(){
    	Double precioBase = this.getPrecioBase();
    	return precioBase - (precioBase * this.descuento / 100);
    }

    public void agregarVendible(ItemVendible vendible){
    	vendibles.add(vendible);
    }

    public void eliminarVendible(ItemVendible vendible){
    	vendibles.remove(vendible);
    }

    //ToDo es necesario que paquete calcule el peso de sus items.

}