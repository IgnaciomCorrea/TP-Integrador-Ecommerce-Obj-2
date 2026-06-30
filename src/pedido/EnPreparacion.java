package pedido;

import Catalogo.ItemVendible;
import exceptions.PedidoExcepcion;

public class EnPreparacion implements EstadoPedido {

    @Override
    public void agregarVendible(Pedido pedido, ItemVendible vendible) {
        throw new PedidoExcepcion("No se puede modificar un pedido en preparación");
    }

    @Override
    public void quitarVendible(Pedido pedido, ItemVendible vendible) {
        throw new PedidoExcepcion("No se puede modificar un pedido en preparación");
    }

    @Override
    public void confirmar(Pedido pedido) {
        throw new PedidoExcepcion("El pedido ya fue confirmado");
    }

    @Override
    public void cancelar(Pedido pedido) {
        double total = pedido.calcularPrecioTotal() + pedido.calcularCostoEnvio();
        pedido.generarNotaCredito(total, "Cancelación desde estado EnPreparacion");
        pedido.setEstado(new Cancelado());
    }

    @Override
    public void pasarAEnPreparacion(Pedido pedido) {
        throw new PedidoExcepcion("El pedido ya está en preparación");
    }

    @Override
    public void pasarAEnviado(Pedido pedido) {
        pedido.setEstado(new Enviado());
    }

    @Override
    public void pasarAEntregado(Pedido pedido) {
        throw new PedidoExcepcion("Debe estar en enviado para pasar a entregado");
    }
}