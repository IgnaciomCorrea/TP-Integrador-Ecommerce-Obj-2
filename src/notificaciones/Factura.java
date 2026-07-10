package notificaciones;

public class Factura {
    private double precioProducto;
    private double precioEnvio;

    public Factura(double precioProducto, double precioEnvio) {
        this.precioProducto = precioProducto;
        this.precioEnvio = precioEnvio;
    }

    public void imprimirFactura() {
        double total = this.precioProducto + this.precioEnvio;
        System.out.println("Precio Producto: " + this.precioProducto +
                "\nPrecio Envio: " + this.precioEnvio +
                "\nTotal = " + total);
    }
}