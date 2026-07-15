package notificaciones;

import Catalogo.Categoria;
import Catalogo.ItemVendible;
import Catalogo.Producto;
import org.junit.jupiter.api.Test;
import pedido.*;
import testutils.PedidoFactory;

import static org.junit.jupiter.api.Assertions.*;

class GeneradorFacturaTest {

    @Test
    void onCambioEstado_cuandoNuevoEstadoEsEntregado_debeAgregarFactura() {
        GeneradorFactura generador = new GeneradorFactura();
        Pedido pedido = PedidoFactory.pedido();
        
        Producto p1 = new Producto("SKU1", "Producto1", "Marca", Categoria.ELECTRONICA,
                "Desc", 0.0, 100.0, 0.5);
        Producto p2 = new Producto("SKU2", "Producto2", "Marca", Categoria.ELECTRONICA,
                "Desc", 0.0, 50.0, 0.3);
        pedido.agregarVendible(new ItemVendible(2, p1)); // 200
        pedido.agregarVendible(new ItemVendible(1, p2)); // 50

        CambioEstadoEvento evento = new CambioEstadoEvento(new Enviado(), new Entregado());

        generador.onCambioEstado(evento, pedido);

        assertEquals(1, generador.getFacturas().size());
        Factura factura = generador.getFacturas().get(0);
        assertNotNull(factura);
    }

    @Test
    void onCambioEstado_cuandoNuevoEstadoNoEsEntregado_noDebeAgregarFactura() {
        GeneradorFactura generador = new GeneradorFactura();
        Pedido pedido = PedidoFactory.pedido();

        generador.onCambioEstado(new CambioEstadoEvento(new Borrador(), new Confirmado()), pedido);
        generador.onCambioEstado(new CambioEstadoEvento(new Confirmado(), new EnPreparacion()), pedido);
        generador.onCambioEstado(new CambioEstadoEvento(new EnPreparacion(), new Enviado()), pedido);
        generador.onCambioEstado(new CambioEstadoEvento(new Confirmado(), new Cancelado()), pedido);

        assertTrue(generador.getFacturas().isEmpty());
    }

    @Test
    void onCambioEstado_conEstadoEntregado_noLanzaExcepcionYAgregaFactura() {
        GeneradorFactura generador = new GeneradorFactura();
        Pedido pedido = PedidoFactory.pedido();
        CambioEstadoEvento evento = new CambioEstadoEvento(new Enviado(), new Entregado());

        assertDoesNotThrow(() -> generador.onCambioEstado(evento, pedido));
        assertEquals(1, generador.getFacturas().size());
    }
}