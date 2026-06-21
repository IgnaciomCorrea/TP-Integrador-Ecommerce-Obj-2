package notificaciones;

import org.junit.jupiter.api.Test;
import pedido.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class GeneradorFacturaTest {

    @Test
    void onCambioEstado_cuandoNuevoEstadoEsEntregado_debeGenerarFactura() {

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        GeneradorFactura generador = new GeneradorFactura();
        Pedido pedido = new Pedido();
        CambioEstadoEvento evento = new CambioEstadoEvento(new Enviado(), new Entregado());

        generador.onCambioEstado(evento, pedido);

        String salida = outContent.toString();
        assertTrue(salida.contains("Generando factura para el pedido."));

        System.setOut(System.out);
    }

    @Test
    void onCambioEstado_cuandoNuevoEstadoNoEsEntregado_noDebeGenerarFactura() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        GeneradorFactura generador = new GeneradorFactura();
        Pedido pedido = new Pedido();

        CambioEstadoEvento eventoConfirmado = new CambioEstadoEvento(new Borrador(), new Confirmado());
        generador.onCambioEstado(eventoConfirmado, pedido);

        CambioEstadoEvento eventoPreparacion = new CambioEstadoEvento(new Confirmado(), new EnPreparacion());
        generador.onCambioEstado(eventoPreparacion, pedido);

        CambioEstadoEvento eventoEnviado = new CambioEstadoEvento(new EnPreparacion(), new Enviado());
        generador.onCambioEstado(eventoEnviado, pedido);

        CambioEstadoEvento eventoCancelado = new CambioEstadoEvento(new Confirmado(), new Cancelado());
        generador.onCambioEstado(eventoCancelado, pedido);

        String salida = outContent.toString();
        assertFalse(salida.contains("Generando factura"));

        System.setOut(System.out);
    }

    @Test
    void onCambioEstado_conEstadoEntregado_noLanzaExcepcion() {
        GeneradorFactura generador = new GeneradorFactura();
        Pedido pedido = new Pedido();
        CambioEstadoEvento evento = new CambioEstadoEvento(new Enviado(), new Entregado());

        assertDoesNotThrow(() -> generador.onCambioEstado(evento, pedido));
    }
}