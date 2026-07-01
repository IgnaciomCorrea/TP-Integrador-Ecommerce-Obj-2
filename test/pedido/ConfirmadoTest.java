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
class ConfirmadoTest {

    private Confirmado estadoConfirmado;

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
        estadoConfirmado = new Confirmado();
    }

    // --- Operaciones que deben lanzar excepción ---

    @Test
    void agregarVendible_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoConfirmado.agregarVendible(pedidoMock, itemMock));
        verifyNoInteractions(pedidoMock, itemMock);
    }

    @Test
    void quitarVendible_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoConfirmado.quitarVendible(pedidoMock, itemMock));
        verifyNoInteractions(pedidoMock, itemMock);
    }

    @Test
    void confirmar_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoConfirmado.confirmar(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    @Test
    void pasarAEnviado_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoConfirmado.pasarAEnviado(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    @Test
    void pasarAEntregado_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoConfirmado.pasarAEntregado(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    @Test
    void setMetodoDePago_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoConfirmado.setMetodoDePago(pedidoMock, metodoPagoMock, medioDePagoMock));
        verifyNoInteractions(pedidoMock, metodoPagoMock, medioDePagoMock);
    }

    @Test
    void setMetodoDeEnvio_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoConfirmado.setMetodoDeEnvio(pedidoMock, metodoEnvioMock));
        verifyNoInteractions(pedidoMock, metodoEnvioMock);
    }

    // --- Operaciones válidas ---

    @Test
    void cancelar_debeGenerarNotaCreditoYCambiarACancelado() {
        when(pedidoMock.calcularPrecioTotal()).thenReturn(100.0);
        when(pedidoMock.calcularCostoEnvio()).thenReturn(20.0);

        estadoConfirmado.cancelar(pedidoMock);

        verify(pedidoMock, times(1)).generarNotaCredito(120.0, "Cancelación desde estado Confirmado");
        verify(pedidoMock, times(1)).setEstado(any(Cancelado.class));
    }

    @Test
    void pasarAEnPreparacion_debeCambiarAEnPreparacion() {
        estadoConfirmado.pasarAEnPreparacion(pedidoMock);
        verify(pedidoMock, times(1)).setEstado(any(EnPreparacion.class));
    }
}