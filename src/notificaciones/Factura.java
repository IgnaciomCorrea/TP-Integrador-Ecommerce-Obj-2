package notificaciones;

public class Factura {
    private double precioProducto;
    private double precioEnvio;

    public Factura(double precioProducto, double precioEnvio) {
        this.precioProducto = precioProducto;
        this.precioEnvio = precioEnvio;
    }

    public void imprimirFactura(){
        System.out.println("Precio Producto: " + this.precioProducto + "\n Precio Envio: " + +this.precioEnvio + "\nTotal =" + this.precioProducto + this.precioEnvio);
    }


}
