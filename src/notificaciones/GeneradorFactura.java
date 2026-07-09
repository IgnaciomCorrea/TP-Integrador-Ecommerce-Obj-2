package notificaciones;

import pedido.Pedido;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneradorFactura implements ObservadorPedido {
    private Map<Pedido, Factura> facturas;

    public GeneradorFactura() {
        this.facturas = new HashMap<>();
    }

    @Override
    public void onCambioEstado(CambioEstadoEvento evento, Pedido pedido) {
        if (evento.getEstadoNuevo().debeGenerarFactura()) {
            this.facturas.put(pedido, new Factura(pedido.calcularPrecioTotal(), pedido.calcularCostoEnvio()));
        }
    }

    public List<Factura> getFacturas() {
        return new ArrayList<>(facturas.values());
    }
}