package pedido;

public class Entregado extends EstadoPedido {

    @Override
    public boolean debeEnviarEmail() {
        return true;
    }

    @Override
    public boolean debeGenerarFactura() {
        return true;
    }

    @Override
    public boolean esEntregado() {
        return true;
    }
}