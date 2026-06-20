package notificaciones;

import pedido.Pedido;

public interface ObservadorPedido {
    void onCambioEstado(CambioEstadoEvento evento, Pedido pedido);
}