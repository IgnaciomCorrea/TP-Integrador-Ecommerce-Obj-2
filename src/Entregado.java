import exceptions.ExcepcionGeneral;

public class Entregado implements EstadoPedido {
    @Override
    public void agregarVendible(Pedido pedido, Vendible vendible) {
        throw new ExcepcionGeneral("Pedido entregado, no se puede modificar");
    }
    @Override
    public void quitarVendible(Pedido pedido, Vendible vendible) {
        throw new ExcepcionGeneral("Pedido entregado, no se puede modificar");
    }
    @Override
    public void confirmar(Pedido pedido) {
        throw new ExcepcionGeneral("Pedido ya entregado");
    }
    @Override
    public void cancelar(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede cancelar un pedido entregado");
    }
    @Override
    public void pasarAEnPreparacion(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede retroceder desde entregado");
    }
    @Override
    public void pasarAEnviado(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede retroceder desde entregado");
    }
    @Override
    public void pasarAEntregado(Pedido pedido) {
        throw new ExcepcionGeneral("El pedido ya está entregado");
    }
}