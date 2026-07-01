package notificaciones;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pedido.*;
import testutils.PedidoFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificadorEmailTest {

    @Mock
    private MailSender mailSenderMock;

    private NotificadorEmail notificador;
    private final String direccionCliente = "cliente@test.com";

    @BeforeEach
    void setUp() {
        notificador = new NotificadorEmail(mailSenderMock, direccionCliente);
    }

    @Test
    void onCambioEstado_cuandoNuevoEstadoEsConfirmado_debeEnviarEmail() {
        Pedido pedido = PedidoFactory.pedido();
        CambioEstadoEvento evento = new CambioEstadoEvento(new Borrador(), new Confirmado());

        notificador.onCambioEstado(evento, pedido);

        ArgumentCaptor<String> captorDestino = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> captorTitulo = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> captorMensaje = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> captorAdjunto = ArgumentCaptor.forClass(Object.class);

        verify(mailSenderMock, times(1)).enviarMail(
                captorDestino.capture(),
                captorTitulo.capture(),
                captorMensaje.capture(),
                captorAdjunto.capture()
        );

        assertEquals(direccionCliente, captorDestino.getValue());
        assertEquals("Cambio de estado de tu pedido", captorTitulo.getValue());
        assertTrue(captorMensaje.getValue().contains("Confirmado"));
        assertNull(captorAdjunto.getValue());
    }

    @Test
    void onCambioEstado_cuandoNuevoEstadoEsEnviado_debeEnviarEmail() {
        Pedido pedido = PedidoFactory.pedido();
        CambioEstadoEvento evento = new CambioEstadoEvento(new EnPreparacion(), new Enviado());

        notificador.onCambioEstado(evento, pedido);

        verify(mailSenderMock, times(1)).enviarMail(anyString(), anyString(), anyString(), isNull());
    }

    @Test
    void onCambioEstado_cuandoNuevoEstadoEsEntregado_debeEnviarEmail() {
        Pedido pedido = PedidoFactory.pedido();
        CambioEstadoEvento evento = new CambioEstadoEvento(new Enviado(), new Entregado());

        notificador.onCambioEstado(evento, pedido);

        verify(mailSenderMock, times(1)).enviarMail(anyString(), anyString(), anyString(), isNull());
    }

    @Test
    void onCambioEstado_cuandoNuevoEstadoNoEsConfirmadoEnviadoNiEntregado_noDebeEnviarEmail() {
        Pedido pedido = PedidoFactory.pedido();

        CambioEstadoEvento eventoBorrador = new CambioEstadoEvento(new Confirmado(), new Borrador());
        notificador.onCambioEstado(eventoBorrador, pedido);

        CambioEstadoEvento eventoPreparacion = new CambioEstadoEvento(new Confirmado(), new EnPreparacion());
        notificador.onCambioEstado(eventoPreparacion, pedido);

        CambioEstadoEvento eventoCancelado = new CambioEstadoEvento(new Confirmado(), new Cancelado());
        notificador.onCambioEstado(eventoCancelado, pedido);

        verify(mailSenderMock, never()).enviarMail(anyString(), anyString(), anyString(), any());
    }
}
