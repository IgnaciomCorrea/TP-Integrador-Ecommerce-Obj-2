package reportes;

public class ItemVenta {
    private String sku;
    private String nombre;
    private int cantidad;
    private double precioPromedio;

    public ItemVenta(String sku, String nombre, int cantidad, double precioPromedio) {
        this.sku = sku;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioPromedio = precioPromedio;
    }

    public String getSku() { return sku; }
    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public double getPrecioPromedio() { return precioPromedio; }
}