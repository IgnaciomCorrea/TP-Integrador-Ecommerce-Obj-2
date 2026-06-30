package pedido;

import Catalogo.ItemVendible;
import envio.MetodoEnvio;
import exceptions.PedidoExcepcion;
import metodoPago.MedioDePago;
import metodoPago.MetodoPago;

public class Enviado implements EstadoPedido {

    @Override
    public void agregarVendible(Pedido pedido, ItemVendible vendible) {
        throw new PedidoExcepcion("No se puede modificar un pedido enviado");
    }

    @Override
    public void quitarVendible(Pedido pedido, ItemVendible vendible) {
        throw new PedidoExcepcion("No se puede modificar un pedido enviado");
    }

    @Override
    public void confirmar(Pedido pedido) {
        throw new PedidoExcepcion("El pedido ya fue confirmado");
    }

    @Override
    public void cancelar(Pedido pedido) {
        double totalProductos = pedido.calcularPrecioTotal();
        pedido.generarNotaCredito(totalProductos, "Cancelación desde estado Enviado (sin envío)");
        pedido.setEstado(new Cancelado());
    }

    @Override
    public void pasarAEnPreparacion(Pedido pedido) {
        throw new PedidoExcepcion("No se puede volver a preparación desde enviado");
    }

    @Override
    public void pasarAEnviado(Pedido pedido) {
        throw new PedidoExcepcion("El pedido ya está en enviado");
    }

    @Override
    public void pasarAEntregado(Pedido pedido) {
        pedido.setEstado(new Entregado());
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