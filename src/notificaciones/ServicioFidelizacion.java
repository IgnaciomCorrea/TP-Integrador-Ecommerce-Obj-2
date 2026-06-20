package notificaciones;

import pedido.Cancelado;
import pedido.Pedido;

public class ServicioFidelizacion implements ObservadorPedido {
    @Override
    public void onCambioEstado(CambioEstadoEvento evento, Pedido pedido) {
        if (evento.getEstadoNuevo() instanceof Cancelado) {
            // acá implementar lógica de enviar cupón del 5%
        }
    }
}