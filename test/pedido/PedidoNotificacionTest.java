package pedido;

import notificaciones.CambioEstadoEvento;
import notificaciones.ObservadorPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoNotificacionTest {

    private Pedido pedido;

    @Mock
    private ObservadorPedido observadorMock;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
    }

    @Test
    void setEstado_debeNotificarAObservadoresConEventoCorrecto() {
        pedido.agregarObservador(observadorMock);

        EstadoPedido estadoAnterior = pedido.getEstado();
        EstadoPedido nuevoEstado = new Confirmado();

        pedido.setEstado(nuevoEstado);

        ArgumentCaptor<CambioEstadoEvento> captorEvento = ArgumentCaptor.forClass(CambioEstadoEvento.class);
        verify(observadorMock, times(1)).onCambioEstado(captorEvento.capture(), eq(pedido));

        CambioEstadoEvento evento = captorEvento.getValue();
        assertSame(estadoAnterior, evento.getEstadoAnterior());
        assertSame(nuevoEstado, evento.getEstadoNuevo());
    }

    @Test
    void setEstado_debeNotificarATodosLosObservadoresRegistrados() {
        ObservadorPedido otroMock = mock(ObservadorPedido.class);
        pedido.agregarObservador(observadorMock);
        pedido.agregarObservador(otroMock);

        pedido.setEstado(new Confirmado());

        verify(observadorMock, times(1)).onCambioEstado(any(CambioEstadoEvento.class), eq(pedido));
        verify(otroMock, times(1)).onCambioEstado(any(CambioEstadoEvento.class), eq(pedido));
    }

    @Test
    void setEstado_sinObservadores_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> pedido.setEstado(new Confirmado()));
    }

    @Test
    void transicionDeEstado_debeNotificarObservadores() {
        pedido.agregarObservador(observadorMock);

        pedido.confirmarPedido();

        verify(observadorMock, times(1)).onCambioEstado(any(CambioEstadoEvento.class), eq(pedido));

        pedido.pasarAEnPreparacion();

        verify(observadorMock, times(2)).onCambioEstado(any(CambioEstadoEvento.class), eq(pedido));
    }
}