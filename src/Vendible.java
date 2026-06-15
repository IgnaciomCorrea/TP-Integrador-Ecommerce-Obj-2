public abstract class Vendible {
    public String SKU;
    public String nombre;
    public String marca;
    public Categoria categoria;
    public String descripcion;
    public Double descuento;
    public int cantidad;

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

    public void validarProducto();

    public void validarAtributoDinamico(String atributo);

    public agregarAtributo(Atributo atributo);
}
