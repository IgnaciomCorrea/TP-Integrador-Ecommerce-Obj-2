package Catalogo;

public abstract class Vendible {
    private String SKU;
    private String nombre;
    private String marca;
    private Categoria categoria;
    private String descripcion;
    protected Double descuento;

    public Vendible(String sku, String nombre, String marca, Categoria categoria, String descripcion, Double descuento) {
    	this.SKU = sku;
    	this.nombre = nombre;
    	this.marca = marca;
    	this.categoria = categoria;
    	this.descripcion = descripcion;
    	this.descuento = descuento;
    }

    
    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Categoria getCategoria() {
        return categoria;
    }
    
    
    
    public abstract Double getPrecioBase();

    public abstract Double getPrecioFinal();

    // preguntar
    public boolean validarVendible() {
    	// return sku != null && nombre != null && ...  
    	return true; // BORRAR!
    }
}
