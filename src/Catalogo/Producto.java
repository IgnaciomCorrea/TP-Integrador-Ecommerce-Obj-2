package Catalogo;
import exceptions.CatalogoExcepcion;

import java.util.HashSet;
import java.util.Set;

public class Producto extends Vendible{


	private Set<Atributo<?>> atributos = new HashSet<Atributo<?>>();
	private Double precio;
	private Double peso;

	// Constructor para el caso en que no se dé la cantidad de Producto que contiene, por default es uno.
    public Producto(String sku, String nombre, String marca, Categoria categoria, String descripcion, Double descuento,Double precio, Double peso) {
        super(sku, nombre, marca, categoria, descripcion, descuento);
        this.precio = precio;
        this.peso = peso;
    }

    //Set de atributos estaticos de Producto. Utilizados para evitar repetidos al agregar atributo.
    private static final Set<String> NOMBRES_ATRIBUTOS_FIJOS = Set.of(
            "sku", "nombre", "marca", "categoria", "descripcion", "descuento", "precio", "peso"
    );

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
    	return this.getPrecioBase() - (this.getPrecioBase() * this.descuento / 100 );
    }
    
    // Busca un atributo dinámico, en la lista atributos, con el nombre dado para validar su existencia.
    public boolean validarAtributoDinamico(String nombreBuscado) {
    	return this.atributos.stream().anyMatch(atributo->atributo.getNombre().equals(nombreBuscado));
    }

    public <T> void agregarAtributo(String nombre, T valor) {
        if (nombre == null || nombre.isBlank()) {
            throw new CatalogoExcepcion("El nombre del atributo no puede ser null o vacío");
        }
        if (valor == null) {
            throw new CatalogoExcepcion("El valor del atributo no puede ser null");
        }
        String nombreLower = nombre.toLowerCase();

        if (NOMBRES_ATRIBUTOS_FIJOS.contains(nombreLower)) {
            throw new CatalogoExcepcion("El nombre '" + nombre + "' está reservado para un atributo fijo del producto");
        }
        if (validarAtributoDinamico(nombre)) {
            throw new CatalogoExcepcion("Ya existe un atributo dinámico con el nombre: " + nombre);
        }
        atributos.add(new Atributo<>(nombre, valor));
    }

    public <T> T getAtributoNombre(String nombre) {
        return atributos.stream()
                .filter(atributo->atributo.getNombre().equals(nombre))
                .map(atributo -> (T) atributo.getValor()).findFirst()
                .orElse(null);
    }
    public Double getPeso() {
	   return this.peso;
   }
}