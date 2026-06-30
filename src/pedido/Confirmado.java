package pedido;

import Catalogo.ItemVendible;
import envio.MetodoEnvio;
import exceptions.PedidoExcepcion;
import metodoPago.MedioDePago;
import metodoPago.MetodoPago;

public class Confirmado implements EstadoPedido {

    @Override
    public void agregarVendible(Pedido pedido, ItemVendible vendible) {
        throw new PedidoExcepcion("No se puede agregar ítems a un pedido confirmado");
    }

    @Override
    public void quitarVendible(Pedido pedido, ItemVendible vendible) {
        throw new PedidoExcepcion("No se puede quitar ítems de un pedido confirmado");
    }

    @Override
    public void confirmar(Pedido pedido) {
        throw new PedidoExcepcion("El pedido ya está confirmado");
    }

    @Override
    public void cancelar(Pedido pedido) {
        double total = pedido.calcularPrecioTotal() + pedido.calcularCostoEnvio();
        pedido.generarNotaCredito(total, "Cancelación desde estado Confirmado");
        pedido.setEstado(new Cancelado());
    }

    @Override
    public void pasarAEnPreparacion(Pedido pedido) {
        pedido.setEstado(new EnPreparacion());
    }

    @Override
    public void pasarAEnviado(Pedido pedido) {
        throw new PedidoExcepcion("Debe estar en preparación para pasar a enviado");
    }

    @Override
    public void pasarAEntregado(Pedido pedido) {
        throw new PedidoExcepcion("Debe estar en enviado para pasar a entregado");
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