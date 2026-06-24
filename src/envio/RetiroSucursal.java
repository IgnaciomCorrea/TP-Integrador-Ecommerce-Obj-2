package envio;

import direccion.Direccion;
import pedido.Pedido;
import sucursal.Sucursal;

public class RetiroSucursal implements MetodoEnvio {
    @Override
    public double calcularCosto(Pedido pedido, Direccion direccion, Sucursal sucursal) {
        return 0;
    }

    @Override
    public int calcularTiempo(Pedido pedido, Direccion direccion, Sucursal sucursal) {
        if (sucursal.hayStock(pedido)) {
            return 0;
        } else {
            return 3;
        }
    }
}