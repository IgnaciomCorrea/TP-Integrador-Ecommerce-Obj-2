import exceptions.ExcepcionGeneral;

public class Confirmado implements EstadoPedido {
    @Override
    public void agregarVendible(Pedido pedido, Vendible vendible) {
        throw new ExcepcionGeneral("No se puede agregar ítems a un pedido confirmado");
    }

    @Override
    public void quitarVendible(Pedido pedido, Vendible vendible) {
        throw new ExcepcionGeneral("No se puede quitar ítems de un pedido confirmado");
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
        throw new ExcepcionGeneral("Debe estar en preparación para pasar a enviado");
    }

    @Override
    public void pasarAEntregado(Pedido pedido) {
        throw new ExcepcionGeneral("Debe estar en enviado para pasar a entregado");
    }
}