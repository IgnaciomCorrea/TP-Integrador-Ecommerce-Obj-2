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
class EntregadoTest {

    private Entregado estadoEntregado;

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
        estadoEntregado = new Entregado();
    }

    @Test
    void agregarVendible_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEntregado.agregarVendible(pedidoMock, itemMock));
        verifyNoInteractions(pedidoMock, itemMock);
    }

    @Test
    void quitarVendible_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEntregado.quitarVendible(pedidoMock, itemMock));
        verifyNoInteractions(pedidoMock, itemMock);
    }

    @Test
    void confirmar_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEntregado.confirmar(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    @Test
    void cancelar_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEntregado.cancelar(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    @Test
    void pasarAEnPreparacion_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEntregado.pasarAEnPreparacion(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    @Test
    void pasarAEnviado_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEntregado.pasarAEnviado(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    @Test
    void pasarAEntregado_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEntregado.pasarAEntregado(pedidoMock));
        verifyNoInteractions(pedidoMock);
    }

    @Test
    void setMetodoDePago_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEntregado.setMetodoDePago(pedidoMock, metodoPagoMock, medioDePagoMock));
        verifyNoInteractions(pedidoMock, metodoPagoMock, medioDePagoMock);
    }

    @Test
    void setMetodoDeEnvio_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEntregado.setMetodoDeEnvio(pedidoMock, metodoEnvioMock));
        verifyNoInteractions(pedidoMock, metodoEnvioMock);
    }

    @Test
    void agregarVendible_conPedidoNull_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEntregado.agregarVendible(null, itemMock));
    }

    @Test
    void confirmar_conPedidoNull_debeLanzarPedidoExcepcion() {
        assertThrows(PedidoExcepcion.class,
                () -> estadoEntregado.confirmar(null));
    }
}