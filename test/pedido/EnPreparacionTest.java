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
class EnPreparacionTest {

    private EnPreparacion estadoEnPreparacion;

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
        estadoEnPreparacion = new EnPreparacion();
    }

    // --- Operaciones que deben lanzar excepción ---

    @Test
    void agregarVendible_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEnPreparacion.agregarVendible(pedidoMock, itemMock));
        verifyNoInteractions(pedidoMock, itemMock);
    }

    @Test
    void quitarVendible_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEnPreparacion.quitarVendible(pedidoMock, itemMock));
        verifyNoInteractions(pedidoMock, itemMock);
    }

    @Test
    void confirmar_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEnPreparacion.confirmar(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    @Test
    void pasarAEnPreparacion_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEnPreparacion.pasarAEnPreparacion(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    @Test
    void pasarAEntregado_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEnPreparacion.pasarAEntregado(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    @Test
    void setMetodoDePago_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEnPreparacion.setMetodoDePago(pedidoMock, metodoPagoMock, medioDePagoMock));
        verifyNoInteractions(pedidoMock, metodoPagoMock, medioDePagoMock);
    }

    @Test
    void setMetodoDeEnvio_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEnPreparacion.setMetodoDeEnvio(pedidoMock, metodoEnvioMock));
        verifyNoInteractions(pedidoMock, metodoEnvioMock);
    }

    // --- Operaciones válidas ---

    @Test
    void cancelar_debeGenerarNotaCreditoYCambiarACancelado() {
        when(pedidoMock.calcularPrecioTotal()).thenReturn(150.0);
        when(pedidoMock.calcularCostoEnvio()).thenReturn(30.0);

        estadoEnPreparacion.cancelar(pedidoMock);

        verify(pedidoMock, times(1)).generarNotaCredito(180.0, "Cancelación desde estado EnPreparacion");
        verify(pedidoMock, times(1)).setEstado(any(Cancelado.class));
    }

    @Test
    void pasarAEnviado_debeCambiarAEnviado() {
        estadoEnPreparacion.pasarAEnviado(pedidoMock);
        verify(pedidoMock, times(1)).setEstado(any(Enviado.class));
    }
}