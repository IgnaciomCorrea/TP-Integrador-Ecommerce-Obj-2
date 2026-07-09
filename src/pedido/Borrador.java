package pedido;

import Catalogo.ItemVendible;
import envio.MetodoEnvio;
import exceptions.PedidoExcepcion;
import metodoPago.MedioDePago;
import metodoPago.MetodoPago;

public class Borrador extends EstadoPedido {

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
        pedido.cobrarPedido();
        pedido.setEstado(new Confirmado());
    }

    @Override
    public void cancelar(Pedido pedido) {
        pedido.setEstado(new Cancelado());
    }

    @Override
    public void setMetodoDePago(Pedido pedido, MetodoPago<?> metodoPago, MedioDePago medioDePago) {
        pedido.asignarMetodoDePago(metodoPago, medioDePago);
    }

    @Override
    public void setMetodoDeEnvio(Pedido pedido, MetodoEnvio metodoEnvio) {
        pedido.asignarMetodoDeEnvio(metodoEnvio);
    }
}