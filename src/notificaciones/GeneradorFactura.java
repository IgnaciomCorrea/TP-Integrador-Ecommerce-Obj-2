package notificaciones;

import pedido.Pedido;

public class GeneradorFactura implements ObservadorPedido {
    @Override
    public void onCambioEstado(CambioEstadoEvento evento, Pedido pedido) {
        if (evento.getEstadoNuevo().debeGenerarFactura()) {
            System.out.println("Generando factura para el pedido.");
        }
    }
}