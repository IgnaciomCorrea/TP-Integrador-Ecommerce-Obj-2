package pedido;


public class EnPreparacion extends EstadoPedido {

    @Override
    public void cancelar(Pedido pedido) {
        double total = pedido.calcularPrecioTotal() + pedido.calcularCostoEnvio();
        pedido.generarNotaCredito(total, "Cancelación desde estado EnPreparacion");
        pedido.setEstado(new Cancelado());
    }

    @Override
    public void pasarAEnviado(Pedido pedido) {
        pedido.setEstado(new Enviado());
    }

    @Override
    public boolean debeReponerStockAlCancelar() {
        return true;
    }
}