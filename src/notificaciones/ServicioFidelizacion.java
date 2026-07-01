package notificaciones;

import pedido.Cancelado;
import pedido.Pedido;

public class ServicioFidelizacion implements ObservadorPedido {
    @Override
    public void onCambioEstado(CambioEstadoEvento evento, Pedido pedido) {
        if (evento.getEstadoNuevo() instanceof Cancelado) {
            System.out.println("Enviando cupon de descuento del 5% al cliente.");
        }
    }
}
