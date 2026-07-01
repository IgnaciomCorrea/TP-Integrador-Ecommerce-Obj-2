import exceptions.ExcepcionGeneral;

public class Entregado implements EstadoPedido {
    @Override
    public void agregarVendible(Pedido pedido, Vendible vendible) {
        throw new ExcepcionGeneral("Pedido entregado, no se debe agregar productos");
    }
    @Override
    public void quitarVendible(Pedido pedido, Vendible vendible) {
        throw new ExcepcionGeneral("El pedido esta en el momento entregado no se puede quitar producto");
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
        throw new ExcepcionGeneral("No se puede volver hacia atras cuando esta  entregado");
    }
    @Override
    public void pasarAEnviado(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede retroceder desde entregado");
    }
    @Override
    public void pasarAEntregado(Pedido pedido) {
        throw new ExcepcionGeneral("El pedido yase encuentra  entregado");
    }
}