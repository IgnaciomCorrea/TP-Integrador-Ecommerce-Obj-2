package Catalogo;

public abstract class Vendible {
    private String SKU;
    private String nombre;
    private String marca;
    private Categoria categoria;
    private String descripcion;
    protected Double descuento;

    public Vendible(String sku, String nombre, String marca, Categoria categoria, String descripcion, Double descuento) {
    	this.validarAtributos(sku, nombre, marca, categoria, descripcion, descuento);
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

    public abstract Double getPeso();
    
    private void validarAtributos(String sku, String nombre, String marca, Categoria categoria, String descripcion, Double descuento) {
    	this.validarString(sku, "sku"); 
    	this.validarString(nombre, "nombre");
    	this.validarString(marca, "marca");
    	this.validarCategoria(categoria, "categoria");
    	this.validarString(descripcion, "descripcion");
    	this.validarDescuento(descuento, "descuento");
    }
        
    private void validarString(String texto, String nombre) {
    	if (texto == null || texto.isBlank()) {
    		throw new IllegalArgumentException("El atributo " + nombre + " es inválido.");
    	}
    }
    
    private void validarCategoria(Categoria categoria, String nombre) {
    	if (categoria == null) {
    		throw new IllegalArgumentException("El atributo " + nombre + " es inválido.");
    	}
    }
    
    private void validarDescuento(Double descuento, String nombre) {
    	if (descuento == null || descuento < 0 || descuento > 100) {
    		throw new IllegalArgumentException("El atributo " + nombre + " es inválido.");
    	}
    }
    
}
