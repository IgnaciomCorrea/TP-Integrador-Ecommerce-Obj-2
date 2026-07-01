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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CanceladoTest {

    private Cancelado estadoCancelado;

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
        estadoCancelado = new Cancelado();
    }

    // --- Operaciones con vendibles ---

    @Test
    void agregarVendible_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoCancelado.agregarVendible(pedidoMock, itemMock));
        verifyNoInteractions(pedidoMock, itemMock);
    }

    @Test
    void quitarVendible_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoCancelado.quitarVendible(pedidoMock, itemMock));
        verifyNoInteractions(pedidoMock, itemMock);
    }

    // --- Transiciones de estado ---

    @Test
    void confirmar_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoCancelado.confirmar(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    @Test
    void cancelar_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoCancelado.cancelar(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    @Test
    void pasarAEnPreparacion_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoCancelado.pasarAEnPreparacion(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    @Test
    void pasarAEnviado_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoCancelado.pasarAEnviado(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    @Test
    void pasarAEntregado_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoCancelado.pasarAEntregado(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    // --- Configuración de métodos de pago y envío ---

    @Test
    void setMetodoDePago_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoCancelado.setMetodoDePago(pedidoMock, metodoPagoMock, medioDePagoMock));
        verifyNoInteractions(pedidoMock, metodoPagoMock, medioDePagoMock);
    }

    @Test
    void setMetodoDeEnvio_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoCancelado.setMetodoDeEnvio(pedidoMock, metodoEnvioMock));
        verifyNoInteractions(pedidoMock, metodoEnvioMock);
    }

    // --- Caso borde: si el pedido es null, igual debe lanzar excepción ---

    @Test
    void agregarVendible_conPedidoNull_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoCancelado.agregarVendible(null, itemMock));
    }

    @Test
    void confirmar_conPedidoNull_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoCancelado.confirmar(null));
    }
}