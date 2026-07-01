package envio;

import direccion.Direccion;
import pedido.Pedido;
import sucursal.Sucursal;

public class ServicioEnvio {
    private MetodoEnvio estrategia;

    public ServicioEnvio(MetodoEnvio estrategia) {
        this.estrategia = estrategia;
    }

    public void setEstrategia(MetodoEnvio estrategia) {
        this.estrategia = estrategia;
    }

    public double calcularCosto(Pedido pedido, Direccion direccion, Sucursal sucursal) {
        return estrategia.calcularCosto(pedido, direccion, sucursal);
    }

    public int calcularTiempo(Pedido pedido, Direccion direccion, Sucursal sucursal) {
        return estrategia.calcularTiempo(pedido, direccion, sucursal);
    }
}