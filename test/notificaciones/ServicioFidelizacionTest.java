package notificaciones;

import org.junit.jupiter.api.Test;
import pedido.*;
import testutils.PedidoFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ServicioFidelizacionTest {

    @Test
    void onCambioEstado_cuandoNuevoEstadoEsCancelado_debeEnviarCupon() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            ServicioFidelizacion servicio = new ServicioFidelizacion();
            Pedido pedido = PedidoFactory.pedido();
            CambioEstadoEvento evento = new CambioEstadoEvento(new Confirmado(), new Cancelado());

            servicio.onCambioEstado(evento, pedido);

            assertTrue(outContent.toString().contains("Enviando cupon de descuento del 5% al cliente."));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void onCambioEstado_cuandoNuevoEstadoNoEsCancelado_noDebeEnviarCupon() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            ServicioFidelizacion servicio = new ServicioFidelizacion();
            Pedido pedido = PedidoFactory.pedido();

            servicio.onCambioEstado(new CambioEstadoEvento(new Borrador(), new Confirmado()), pedido);
            servicio.onCambioEstado(new CambioEstadoEvento(new Confirmado(), new EnPreparacion()), pedido);
            servicio.onCambioEstado(new CambioEstadoEvento(new EnPreparacion(), new Enviado()), pedido);
            servicio.onCambioEstado(new CambioEstadoEvento(new Enviado(), new Entregado()), pedido);

            assertFalse(outContent.toString().contains("cupon de descuento"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void onCambioEstado_conEstadoCancelado_noLanzaExcepcion() {
        ServicioFidelizacion servicio = new ServicioFidelizacion();
        Pedido pedido = PedidoFactory.pedido();
        CambioEstadoEvento evento = new CambioEstadoEvento(new Confirmado(), new Cancelado());

        assertDoesNotThrow(() -> servicio.onCambioEstado(evento, pedido));
    }
}
