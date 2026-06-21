package notificaciones;

import org.junit.jupiter.api.Test;
import pedido.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ServicioFidelizacionTest {

    @Test
    void onCambioEstado_cuandoNuevoEstadoEsCancelado_debeEnviarCupon() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ServicioFidelizacion servicio = new ServicioFidelizacion();
        Pedido pedido = new Pedido();
        CambioEstadoEvento evento = new CambioEstadoEvento(new Confirmado(), new Cancelado());

        servicio.onCambioEstado(evento, pedido);

        String salida = outContent.toString();
        assertTrue(salida.contains("Enviando cupón de descuento del 5% al cliente."));

        System.setOut(System.out);
    }

    @Test
    void onCambioEstado_cuandoNuevoEstadoNoEsCancelado_noDebeEnviarCupon() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ServicioFidelizacion servicio = new ServicioFidelizacion();
        Pedido pedido = new Pedido();

        CambioEstadoEvento eventoConfirmado = new CambioEstadoEvento(new Borrador(), new Confirmado());
        servicio.onCambioEstado(eventoConfirmado, pedido);

        CambioEstadoEvento eventoPreparacion = new CambioEstadoEvento(new Confirmado(), new EnPreparacion());
        servicio.onCambioEstado(eventoPreparacion, pedido);

        CambioEstadoEvento eventoEnviado = new CambioEstadoEvento(new EnPreparacion(), new Enviado());
        servicio.onCambioEstado(eventoEnviado, pedido);

        CambioEstadoEvento eventoEntregado = new CambioEstadoEvento(new Enviado(), new Entregado());
        servicio.onCambioEstado(eventoEntregado, pedido);

        String salida = outContent.toString();
        assertFalse(salida.contains("cupón de descuento"));

        System.setOut(System.out);
    }

    @Test
    void onCambioEstado_conEstadoCancelado_noLanzaExcepcion() {
        ServicioFidelizacion servicio = new ServicioFidelizacion();
        Pedido pedido = new Pedido();
        CambioEstadoEvento evento = new CambioEstadoEvento(new Confirmado(), new Cancelado());

        assertDoesNotThrow(() -> servicio.onCambioEstado(evento, pedido));
    }
}