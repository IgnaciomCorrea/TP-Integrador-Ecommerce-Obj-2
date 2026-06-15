public class Producto extends Vendible{
    private Double precio;

    public Producto(Double precio) {
        this.cantidad = 1;
        this.precio = precio;
    }

    public Producto(Double precio, int cantidad) {
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}