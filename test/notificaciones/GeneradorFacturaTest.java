package notificaciones;

import org.junit.jupiter.api.Test;
import pedido.*;
import testutils.PedidoFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class GeneradorFacturaTest {

    @Test
    void onCambioEstado_cuandoNuevoEstadoEsEntregado_debeGenerarFactura() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            GeneradorFactura generador = new GeneradorFactura();
            Pedido pedido = PedidoFactory.pedido();
            CambioEstadoEvento evento = new CambioEstadoEvento(new Enviado(), new Entregado());

            generador.onCambioEstado(evento, pedido);

            assertTrue(outContent.toString().contains("Generando factura para el pedido."));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void onCambioEstado_cuandoNuevoEstadoNoEsEntregado_noDebeGenerarFactura() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            GeneradorFactura generador = new GeneradorFactura();
            Pedido pedido = PedidoFactory.pedido();

            generador.onCambioEstado(new CambioEstadoEvento(new Borrador(), new Confirmado()), pedido);
            generador.onCambioEstado(new CambioEstadoEvento(new Confirmado(), new EnPreparacion()), pedido);
            generador.onCambioEstado(new CambioEstadoEvento(new EnPreparacion(), new Enviado()), pedido);
            generador.onCambioEstado(new CambioEstadoEvento(new Confirmado(), new Cancelado()), pedido);

            assertFalse(outContent.toString().contains("Generando factura"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void onCambioEstado_conEstadoEntregado_noLanzaExcepcion() {
        GeneradorFactura generador = new GeneradorFactura();
        Pedido pedido = PedidoFactory.pedido();
        CambioEstadoEvento evento = new CambioEstadoEvento(new Enviado(), new Entregado());

        assertDoesNotThrow(() -> generador.onCambioEstado(evento, pedido));
    }
}
