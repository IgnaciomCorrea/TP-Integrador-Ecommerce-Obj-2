package pedido;

import Catalogo.Vendible;
import exceptions.ExcepcionGeneral;

public class Cancelado implements EstadoPedido {
    @Override
    public void agregarVendible(Pedido pedido, Vendible vendible) {
        throw new ExcepcionGeneral("No se puede modificar un pedido este estado");
    }
    @Override
    public void quitarVendible(Pedido pedido, Vendible vendible) {
        throw new ExcepcionGeneral("No se puede modificar un pedido este estado");
    }
    @Override
    public void confirmar(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede modificar un pedido este estado");
    }
    @Override
    public void cancelar(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede modificar un pedido este estado");
    }
    @Override
    public void pasarAEnPreparacion(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede modificar un pedido este estado");
    }
    @Override
    public void pasarAEnviado(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede modificar un pedido este estado");
    }
    @Override
    public void pasarAEntregado(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede modificar un pedido este estado");
    }
}