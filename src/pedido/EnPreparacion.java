package pedido;

import Catalogo.ItemVendible;
import Catalogo.Vendible;
import exceptions.ExcepcionGeneral;

public class EnPreparacion implements EstadoPedido {
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
        // Se repone stock y se reembolsa productos + envío
        //pedido.reponerStock();
        //pedido.generarNotaCredito(pedido.calcularTotal() + pedido.calcularCostoEnvio());
        pedido.setEstado(new Cancelado());
    }

    @Override
    public void pasarAEnPreparacion(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede pasar del estado actual a este");
    }

    @Override
    public void pasarAEnviado(Pedido pedido) {
        pedido.setEstado(new Enviado());
    }

    @Override
    public void pasarAEntregado(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede pasar del estado actual a este");
    }
}