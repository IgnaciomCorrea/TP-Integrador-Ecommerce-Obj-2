package notificaciones;

import pedido.EstadoPedido;

public class CambioEstadoEvento {
    private final EstadoPedido estadoAnterior;
    private final EstadoPedido estadoNuevo;

    public CambioEstadoEvento(EstadoPedido estadoAnterior, EstadoPedido estadoNuevo) {
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
    }

    public EstadoPedido getEstadoAnterior() { return estadoAnterior; }
    public EstadoPedido getEstadoNuevo() { return estadoNuevo; }
}