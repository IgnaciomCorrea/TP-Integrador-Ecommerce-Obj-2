package notificaciones;

import pedido.Entregado;
import pedido.Pedido;

public class GeneradorFactura implements ObservadorPedido {
    @Override
    public void onCambioEstado(CambioEstadoEvento evento, Pedido pedido) {
        if (evento.getEstadoNuevo() instanceof Entregado) {
            // acá implementar lógica de creación de comprobante fiscal
        }
    }
}