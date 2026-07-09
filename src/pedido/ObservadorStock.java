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
        EstadoPedido nuevo = evento.getEstadoNuevo();
        EstadoPedido anterior = evento.getEstadoAnterior();

        if (nuevo.esConfirmado()) {
            catalogo.armarPedido(pedido);
        }

        if (nuevo.esCanceladoConReposicion(anterior)) {
            catalogo.reponerStock(pedido.getVendibles());
        }
    }
}