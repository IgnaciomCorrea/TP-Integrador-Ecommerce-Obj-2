package pedido;

import Catalogo.ItemVendible;
import exceptions.PedidoExcepcion;

public class Cancelado implements EstadoPedido {
    @Override
    public void agregarVendible(Pedido pedido, ItemVendible vendible) {
        throw new PedidoExcepcion("No se puede modificar un pedido este estado");
    }
    @Override
    public void quitarVendible(Pedido pedido, ItemVendible vendible) {
        throw new PedidoExcepcion("No se puede modificar un pedido este estado");
    }
    @Override
    public void confirmar(Pedido pedido) {
        throw new PedidoExcepcion("No se puede modificar un pedido este estado");
    }
    @Override
    public void cancelar(Pedido pedido) {
        throw new PedidoExcepcion("No se puede modificar un pedido este estado");
    }
    @Override
    public void pasarAEnPreparacion(Pedido pedido) {
        throw new PedidoExcepcion("No se puede modificar un pedido este estado");
    }
    @Override
    public void pasarAEnviado(Pedido pedido) {
        throw new PedidoExcepcion("No se puede modificar un pedido este estado");
    }
    @Override
    public void pasarAEntregado(Pedido pedido) {
        throw new PedidoExcepcion("No se puede modificar un pedido este estado");
    }
}