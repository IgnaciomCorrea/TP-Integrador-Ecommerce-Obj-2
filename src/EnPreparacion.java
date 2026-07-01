import exceptions.ExcepcionGeneral;

public class EnPreparacion implements EstadoPedido {
    @Override
    public void agregarVendible(Pedido pedido, Vendible vendible) {
        throw new ExcepcionGeneral("No se puede modificar un pedido que se esta preparando");
    }

    @Override
    public void quitarVendible(Pedido pedido, Vendible vendible) {
        throw new ExcepcionGeneral("No se puede quitar productos un pedido en preparación");
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
        throw new ExcepcionGeneral("El pedido se encuentra en el estado  preparación");
    }

    @Override
    public void pasarAEnviado(Pedido pedido) {
        pedido.setEstado(new Enviado());
    }

    @Override
    public void pasarAEntregado(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede pasar a entregado en el momento actual ");
    }
}