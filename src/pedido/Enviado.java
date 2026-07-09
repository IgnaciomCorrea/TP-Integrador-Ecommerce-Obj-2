package pedido;

public class Enviado extends EstadoPedido {

    @Override
    public void cancelar(Pedido pedido) {
        double totalProductos = pedido.calcularPrecioTotal();
        pedido.generarNotaCredito(totalProductos, "Cancelación desde estado Enviado (sin envío)");
        pedido.setEstado(new Cancelado());
    }

    @Override
    public void pasarAEntregado(Pedido pedido) {
        pedido.setEstado(new Entregado());
    }

    @Override
    public boolean debeEnviarEmail() {
        return true;
    }

    @Override
    public boolean debeReponerStockAlCancelar() {
        return true;
    }
}