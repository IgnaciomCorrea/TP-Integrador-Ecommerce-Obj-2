package sucursal;

import pedido.Pedido;

public interface Sucursal {
    boolean hayStock(Pedido pedido);
}