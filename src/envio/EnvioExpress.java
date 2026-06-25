package envio;

import direccion.Direccion;
import pedido.Pedido;
import sucursal.Sucursal;

public class EnvioExpress implements MetodoEnvio {
    private final EnvioExpressCalculadora calculadora;

    public EnvioExpress(EnvioExpressCalculadora calculadora) {
        this.calculadora = calculadora;
    }

    @Override
    public double calcularCosto(Pedido pedido, Direccion direccion, Sucursal sucursal) {
        double precioTotal = pedido.calcularPrecioTotal();
        return calculadora.calcularCosto((float) precioTotal);
    }

    @Override
    public int calcularTiempo(Pedido pedido, Direccion direccion, Sucursal sucursal) {
        return 1;
    }
}