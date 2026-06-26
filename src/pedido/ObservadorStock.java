package pedido;

import Catalogo.Catalogo;
import notificaciones.CambioEstadoEvento;
import notificaciones.ObservadorPedido;


public class ObservadorStock implements ObservadorPedido {
    private Catalogo catalogo;

    public ObservadorStock(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    @Override
    public void onCambioEstado(CambioEstadoEvento evento, Pedido pedido) {
        if (evento.getEstadoNuevo() instanceof Confirmado) {
            catalogo.modificarStockDisponible(pedido.getVendibles());
        }
        if (evento.getEstadoNuevo() instanceof Cancelado
                && evento.getEstadoAnterior() instanceof Confirmado) {
            // catalogo.reponerStock(pedido.getVendibles());
        }
    }
}