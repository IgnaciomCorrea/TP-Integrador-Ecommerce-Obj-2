import exceptions.ExcepcionGeneral;

public class Cancelado implements EstadoPedido {
    @Override
    public void agregarVendible(Pedido pedido, Vendible vendible) {
        throw new ExcepcionGeneral("Pedido cancelado, no se puede modificar");
    }
    @Override
    public void quitarVendible(Pedido pedido, Vendible vendible) {
        throw new ExcepcionGeneral("El pedido ha sido cancelado , no se modificar el stock ");
    }
    @Override
    public void confirmar(Pedido pedido) {
        throw new ExcepcionGeneral("No se  debe  confirmar un pedido cancelado");
    }
    @Override
    public void cancelar(Pedido pedido) {
        throw new ExcepcionGeneral("El pedido ya está cancelado , no se entregara ");
    }
    @Override
    public void pasarAEnPreparacion(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede reanudar un pedido cancelado  ");
    }
    @Override
    public void pasarAEnviado(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede preparar  un pedido que ya esta cancelado ");
    }
    @Override
    public void pasarAEntregado(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede enviar un pedido que ya este cancelado ");
    }
}