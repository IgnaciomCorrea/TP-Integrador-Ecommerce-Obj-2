package pedido;

import Catalogo.ItemVendible;
import envio.MetodoEnvio;
import exceptions.PedidoExcepcion;
import metodoPago.MedioDePago;
import metodoPago.MetodoPago;

public class Entregado implements EstadoPedido {
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

    @Override
    public void setMetodoDePago(Pedido pedido, MetodoPago<?> metodoPago, MedioDePago medioDePago){
        throw new PedidoExcepcion("No se puede modificar un pedido este estado");
    }

    @Override
    public void setMetodoDeEnvio(Pedido pedido, MetodoEnvio metodoEnvio){
        throw new PedidoExcepcion("No se puede modificar un pedido este estado");
    }
}