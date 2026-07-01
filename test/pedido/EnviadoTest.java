package pedido;

import Catalogo.ItemVendible;
import envio.MetodoEnvio;
import exceptions.PedidoExcepcion;
import metodoPago.MedioDePago;
import metodoPago.MetodoPago;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnviadoTest {

    private Enviado estadoEnviado;

    @Mock
    private Pedido pedidoMock;

    @Mock
    private ItemVendible itemMock;

    @Mock
    private MetodoPago<?> metodoPagoMock;

    @Mock
    private MedioDePago medioDePagoMock;

    @Mock
    private MetodoEnvio metodoEnvioMock;

    @BeforeEach
    void setUp() {
        estadoEnviado = new Enviado();
    }

    // --- Operaciones que deben lanzar excepción ---

    @Test
    void agregarVendible_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEnviado.agregarVendible(pedidoMock, itemMock));
        verifyNoInteractions(pedidoMock, itemMock);
    }

    @Test
    void quitarVendible_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEnviado.quitarVendible(pedidoMock, itemMock));
        verifyNoInteractions(pedidoMock, itemMock);
    }

    @Test
    void confirmar_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEnviado.confirmar(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    @Test
    void pasarAEnPreparacion_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEnviado.pasarAEnPreparacion(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    @Test
    void pasarAEnviado_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEnviado.pasarAEnviado(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    @Test
    void setMetodoDePago_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEnviado.setMetodoDePago(pedidoMock, metodoPagoMock, medioDePagoMock));
        verifyNoInteractions(pedidoMock, metodoPagoMock, medioDePagoMock);
    }

    @Test
    void setMetodoDeEnvio_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEnviado.setMetodoDeEnvio(pedidoMock, metodoEnvioMock));
        verifyNoInteractions(pedidoMock, metodoEnvioMock);
    }

    // --- Operaciones válidas ---

    @Test
    void cancelar_debeGenerarNotaCreditoSoloProductosYCambiarACancelado() {
        when(pedidoMock.calcularPrecioTotal()).thenReturn(200.0);

        estadoEnviado.cancelar(pedidoMock);

        verify(pedidoMock, times(1)).generarNotaCredito(200.0, "Cancelación desde estado Enviado (sin envío)");
        verify(pedidoMock, times(1)).setEstado(any(Cancelado.class));
        verify(pedidoMock, never()).calcularCostoEnvio();
    }

    @Test
    void pasarAEntregado_debeCambiarAEntregado() {
        estadoEnviado.pasarAEntregado(pedidoMock);
        verify(pedidoMock, times(1)).setEstado(any(Entregado.class));
    }
}