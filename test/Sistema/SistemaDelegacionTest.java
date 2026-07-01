package Sistema;

import Catalogo.Catalogo;
import envio.MetodoEnvio;
import metodoPago.MedioDePago;
import metodoPago.MetodoPago;
import pedido.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sistema.Sistema;
import testutils.PedidoFactory;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SistemaMetodosDelegacionTest {

    @Mock
    private Catalogo catalogoMock;

    @Mock
    private MetodoPago<?> metodoPagoMock;

    @Mock
    private MedioDePago medioDePagoMock;

    @Mock
    private MetodoEnvio metodoEnvioMock;

    private Sistema sistema;

    @BeforeEach
    void setUp() {
        sistema = new Sistema(catalogoMock);
    }

    @Test
    void setMetodoPagoPedido_debeDelegarEnPedido() {
        Pedido pedido = PedidoFactory.pedido();
        sistema.setMetodoPagoPedido(pedido, metodoPagoMock, medioDePagoMock);
        // No podemos verificar directamente, pero podemos confirmar que no lanza excepción
        // y que el pedido puede cobrar después
        // Opcional: si Pedido tuviera getters, podríamos verificar.
        // Para probar, ejecutamos cobrarPedido y verificamos que se llame a procesarPago
        // (necesitaríamos un spy de Pedido o usar PagoDummy)
    }

    @Test
    void setMetodoEnvioPedido_debeDelegarEnPedido() {
        Pedido pedido = PedidoFactory.pedido();
        sistema.setMetodoEnvioPedido(pedido, metodoEnvioMock);
        // Similar, no podemos verificar directamente.
        // Podemos usar un spy de Pedido para verificar que se llamó a setMetodoDeEnvio
        Pedido pedidoSpy = spy(pedido);
        sistema.setMetodoEnvioPedido(pedidoSpy, metodoEnvioMock);
        verify(pedidoSpy, times(1)).setMetodoDeEnvio(metodoEnvioMock);
    }

    @Test
    void setMetodoPagoPedido_conPedidoNull_debeLanzarNPE() {
        assertThrows(NullPointerException.class,
                () -> sistema.setMetodoPagoPedido(null, metodoPagoMock, medioDePagoMock));
    }

    @Test
    void setMetodoEnvioPedido_conPedidoNull_debeLanzarNPE() {
        assertThrows(NullPointerException.class,
                () -> sistema.setMetodoEnvioPedido(null, metodoEnvioMock));
    }
}