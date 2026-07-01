package notificaciones;

import pedido.Entregado;
import pedido.Pedido;

public class GeneradorFactura implements ObservadorPedido {
    @Override
    public void onCambioEstado(CambioEstadoEvento evento, Pedido pedido) {
        if (evento.getEstadoNuevo() instanceof Entregado) {
            System.out.println("Generando factura para el pedido.");
        }
    }
}
