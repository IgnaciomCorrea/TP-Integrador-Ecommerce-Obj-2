package pedido;

public class Confirmado extends EstadoPedido {

    @Override
    public void cancelar(Pedido pedido) {
        double total = pedido.calcularPrecioTotal() + pedido.calcularCostoEnvio();
        pedido.generarNotaCredito(total, "Cancelación desde estado Confirmado");
        pedido.setEstado(new Cancelado());
    }

    @Override
    public void pasarAEnPreparacion(Pedido pedido) {
        pedido.setEstado(new EnPreparacion());
    }

    @Override
    public boolean debeEnviarEmail() {
        return true;
    }

    @Override
    public boolean esConfirmado() {
        return true;
    }

    @Override
    public boolean debeReponerStockAlCancelar() {
        return true;
    }
}