package pedido;

import Catalogo.ItemVendible;
import Catalogo.Vendible;
import exceptions.ExcepcionGeneral;

public class Enviado implements EstadoPedido {
    @Override
    public void agregarVendible(Pedido pedido, ItemVendible vendible) {
        throw new ExcepcionGeneral("No se puede modificar un pedido este estado");
    }

    @Override
    public void quitarVendible(Pedido pedido, ItemVendible vendible) {
        throw new ExcepcionGeneral("No se puede modificar un pedido este estado");
    }

    @Override
    public void confirmar(Pedido pedido) {
        throw new ExcepcionGeneral("El pedido ya fue confirmado");
    }

    @Override
    public void cancelar(Pedido pedido) {
        // Solo reembolso productos (sin envío), no se repone stock porque ya salió?
        // Según el enunciado: "solo se reembolsa el costo del producto pero no del envío"
        // y no menciona reposición de stock. Asumimos que no se repone porque ya está en camino.
        //pedido.generarNotaCredito(pedido.calcularTotal());
        pedido.setEstado(new Cancelado());
    }

    @Override
    public void pasarAEnPreparacion(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede pasar del estado actual a este");
    }

    @Override
    public void pasarAEnviado(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede pasar del estado actual a este");
    }

    @Override
    public void pasarAEntregado(Pedido pedido) {
        pedido.setEstado(new Entregado());
    }
}