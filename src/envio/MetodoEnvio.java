package envio;

import direccion.Direccion;
import pedido.Pedido;
import sucursal.Sucursal;

public interface MetodoEnvio {
    double calcularCosto(Pedido pedido, Direccion direccion, Sucursal sucursal);
    int calcularTiempo(Pedido pedido, Direccion direccion, Sucursal sucursal);
}