package pedido;

import Catalogo.ItemVendible;
import exceptions.PedidoExcepcion;

public class Borrador implements EstadoPedido {

    @Override
    public void agregarVendible(Pedido pedido, ItemVendible vendible) {
        pedido.getVendibles().add(vendible);
    }

    @Override
    public void quitarVendible(Pedido pedido, ItemVendible vendible) {
        pedido.getVendibles().remove(vendible);
    }

    @Override
    public void confirmar(Pedido pedido) {
        if (pedido.getVendibles().isEmpty()) {
            throw new PedidoExcepcion("No se puede confirmar un pedido sin ítems");
        }
        pedido.setEstado(new Confirmado());
    }

    @Override
    public void cancelar(Pedido pedido) {
        // No hay stock que reponer ni reembolso porque no se confirmó
        pedido.setEstado(new Cancelado());
    }

    @Override
    public void pasarAEnPreparacion(Pedido pedido) {
        throw new PedidoExcepcion("No se puede pasar a preparación desde Borrador");
    }

    @Override
    public void pasarAEnviado(Pedido pedido) {
        throw new PedidoExcepcion("No se puede pasar a enviado desde Borrador");
    }

    @Override
    public void pasarAEntregado(Pedido pedido) {
        throw new PedidoExcepcion("No se puede pasar a entregado desde Borrador");
    }
}