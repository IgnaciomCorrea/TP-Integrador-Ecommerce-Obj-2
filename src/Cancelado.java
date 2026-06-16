import exceptions.ExcepcionGeneral;

public class Cancelado implements EstadoPedido {
    @Override
    public void agregarVendible(Pedido pedido, Vendible vendible) {
        throw new ExcepcionGeneral("Pedido cancelado, no se puede modificar");
    }
    @Override
    public void quitarVendible(Pedido pedido, Vendible vendible) {
        throw new ExcepcionGeneral("Pedido cancelado, no se puede modificar");
    }
    @Override
    public void confirmar(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede confirmar un pedido cancelado");
    }
    @Override
    public void cancelar(Pedido pedido) {
        throw new ExcepcionGeneral("El pedido ya está cancelado");
    }
    @Override
    public void pasarAEnPreparacion(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede reanudar un pedido cancelado");
    }
    @Override
    public void pasarAEnviado(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede reanudar un pedido cancelado");
    }
    @Override
    public void pasarAEntregado(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede reanudar un pedido cancelado");
    }
}