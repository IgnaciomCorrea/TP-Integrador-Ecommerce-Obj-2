package notificaciones;

import pedido.Pedido;

public class ServicioFidelizacion implements ObservadorPedido {
    @Override
    public void onCambioEstado(CambioEstadoEvento evento, Pedido pedido) {
        if (evento.getEstadoNuevo().debeEnviarCupon()) {
            System.out.println("Enviando cupon de descuento del 5% al cliente.");
        }
    }
}