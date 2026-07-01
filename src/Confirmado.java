import exceptions.ExcepcionGeneral;

public class Confirmado implements EstadoPedido {
    @Override
    public void agregarVendible(Pedido pedido, Vendible vendible) {
        throw new ExcepcionGeneral("No se puede agregar items al pedido en este momento , ya esta confirmado");
    }

    @Override
    public void quitarVendible(Pedido pedido, Vendible vendible) {
        throw new ExcepcionGeneral("No se puede quitar ítems de un pedido en este estado actual");
    }

    @Override
    public void confirmar(Pedido pedido) {
        throw new ExcepcionGeneral("El pedido ya está confirmado");
    }

    @Override
    public void cancelar(Pedido pedido) {
        // Reembolso total (productos + envío aún no generado), reponer stock
        //pedido.reponerStock();
        //pedido.generarNotaCredito(pedido.calcularTotal() + pedido.calcularCostoEnvio());
        pedido.setEstado(new Cancelado());
    }

    @Override
    public void pasarAEnPreparacion(Pedido pedido) {
        // Al pasar a preparación, se decrementa el stock (si no se hizo antes)
        //pedido.decrementarStock();
        pedido.setEstado(new EnPreparacion());
    }

    @Override
    public void pasarAEnviado(Pedido pedido) {
        throw new ExcepcionGeneral("no se puede pasar a enviado en este estado actual");
    }

    @Override
    public void pasarAEntregado(Pedido pedido) {
        throw new ExcepcionGeneral("no se puede pasar a enviado en este estado actual);
    }
}