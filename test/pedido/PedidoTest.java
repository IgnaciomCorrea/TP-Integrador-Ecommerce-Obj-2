package pedido;

import Catalogo.Catalogo;
import Catalogo.ItemVendible;
import exceptions.PedidoExcepcion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testutils.PedidoFactory;
import testutils.PedidoFactory.PagoDummy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoTest {

    private Pedido pedido;

    @Mock
    private ItemVendible itemMock1;

    @Mock
    private ItemVendible itemMock2;

    @Mock
    private Catalogo catalogoMock;

    @BeforeEach
    void setUp() {
        pedido = PedidoFactory.pedido();
    }

    @Nested
    class EstadoInicialYOperaciones {
        @Test
        void nuevoPedido_debeEstarEnBorrador() {
            assertTrue(pedido.getEstado() instanceof Borrador);
        }

        @Test
        void agregarVendible_enBorrador_debeAgregar() {
            pedido.agregarVendible(itemMock1);
            assertTrue(pedido.getVendibles().contains(itemMock1));
        }

        @Test
        void quitarVendible_enBorrador_debeQuitar() {
            pedido.agregarVendible(itemMock1);
            pedido.quitarVendible(itemMock1);
            assertFalse(pedido.getVendibles().contains(itemMock1));
        }
    }

    @Nested
    class ConfirmarPedido {
        @Test
        void confirmarPedido_sinVendibles_debeLanzarExcepcion() {
            assertThrows(PedidoExcepcion.class, () -> pedido.confirmarPedido());
            assertTrue(pedido.getEstado() instanceof Borrador);
        }

        @Test
        void confirmarPedido_conVendibles_debeCobrarYPasarAConfirmado() {
            PagoDummy pago = new PagoDummy();
            Pedido pedidoConPago = PedidoFactory.pedidoConPago(pago);
            pedidoConPago.agregarVendible(itemMock1);

            pedidoConPago.confirmarPedido();

            assertTrue(pedidoConPago.getEstado() instanceof Confirmado);
            assertEquals(1, pago.getValidaciones());
            assertEquals(1, pago.getReservas());
            assertEquals(1, pago.getTransacciones());
            assertEquals(1, pago.getNotificaciones());
        }

        @Test
        void confirmarPedido_desdeConfirmado_debeLanzarExcepcion() {
            pedido.agregarVendible(itemMock1);
            pedido.confirmarPedido();

            assertThrows(PedidoExcepcion.class, () -> pedido.confirmarPedido());
        }
    }

    @Nested
    class CancelarPedido {
        @Test
        void cancelarPedido_desdeBorrador_debePasarACanceladoSinNotaCredito() {
            pedido.cancelarPedido();

            assertTrue(pedido.getEstado() instanceof Cancelado);
            assertNull(pedido.getNotaCredito());
        }

        @Test
        void cancelarPedido_desdeConfirmado_debeGenerarNotaCredito() {
            when(itemMock1.getPrecioFinal()).thenReturn(100.0);
            pedido.agregarVendible(itemMock1);
            pedido.confirmarPedido();

            pedido.cancelarPedido();

            assertTrue(pedido.getEstado() instanceof Cancelado);
            assertNotNull(pedido.getNotaCredito());
            assertEquals(100.0, pedido.getNotaCredito().getMonto(), 0.001);
        }

        @Test
        void cancelarPedido_desdeEnviado_debeReembolsarSoloProductos() {
            when(itemMock1.getPrecioFinal()).thenReturn(80.0);
            pedido.agregarVendible(itemMock1);
            pedido.confirmarPedido();
            pedido.pasarAEnPreparacion();
            pedido.pasarAEnviado();

            pedido.cancelarPedido();

            assertTrue(pedido.getEstado() instanceof Cancelado);
            assertEquals(80.0, pedido.getNotaCredito().getMonto(), 0.001);
        }

        @Test
        void cancelarPedido_desdeEntregado_debeLanzarExcepcion() {
            pedido.agregarVendible(itemMock1);
            pedido.confirmarPedido();
            pedido.pasarAEnPreparacion();
            pedido.pasarAEnviado();
            pedido.pasarAEntregado();

            assertThrows(PedidoExcepcion.class, () -> pedido.cancelarPedido());
        }
    }

    @Nested
    class Transiciones {
        @Test
        void debePermitirFlujoConfirmadoPreparacionEnviadoEntregado() {
            pedido.agregarVendible(itemMock1);

            pedido.confirmarPedido();
            pedido.pasarAEnPreparacion();
            pedido.pasarAEnviado();
            pedido.pasarAEntregado();

            assertTrue(pedido.getEstado() instanceof Entregado);
        }

        @Test
        void noDebePermitirSaltarDeBorradorAEnviado() {
            assertThrows(PedidoExcepcion.class, () -> pedido.pasarAEnviado());
        }
    }

    @Nested
    class Calculos {
        @Test
        void calcularPrecioTotal_debeSumarPreciosFinales() {
            when(itemMock1.getPrecioFinal()).thenReturn(100.0);
            when(itemMock2.getPrecioFinal()).thenReturn(50.0);

            pedido.agregarVendible(itemMock1);
            pedido.agregarVendible(itemMock2);

            assertEquals(150.0, pedido.calcularPrecioTotal(), 0.001);
        }

        @Test
        void calcularPesoTotal_debeSumarPesos() {
            when(itemMock1.getPeso()).thenReturn(0.5);
            when(itemMock2.getPeso()).thenReturn(0.2);

            pedido.agregarVendible(itemMock1);
            pedido.agregarVendible(itemMock2);

            assertEquals(0.7, pedido.calcularPesoTotal(), 0.001);
        }
    }

    @Nested
    class ObservadorStockTest {
        private Pedido pedidoConStockObserver;

        @BeforeEach
        void setUp() {
            pedidoConStockObserver = PedidoFactory.pedido();
            pedidoConStockObserver.agregarObservador(new ObservadorStock(catalogoMock));
        }

        @Test
        void alConfirmarPedido_debeLlamarARestarStock() {
            pedidoConStockObserver.agregarVendible(itemMock1);

            pedidoConStockObserver.confirmarPedido();

            ArgumentCaptor<List<ItemVendible>> captor = ArgumentCaptor.forClass(List.class);
            verify(catalogoMock).restarStock(captor.capture());
            assertTrue(captor.getValue().contains(itemMock1));
        }

        @Test
        void alCancelarDesdeConfirmado_debeLlamarAReponerStock() {
            pedidoConStockObserver.agregarVendible(itemMock1);
            pedidoConStockObserver.confirmarPedido();

            pedidoConStockObserver.cancelarPedido();

            verify(catalogoMock).reponerStock(anyList());
        }

        @Test
        void alCancelarDesdeEnviado_noDebeReponerStock() {
            pedidoConStockObserver.agregarVendible(itemMock1);
            pedidoConStockObserver.confirmarPedido();
            pedidoConStockObserver.pasarAEnPreparacion();
            pedidoConStockObserver.pasarAEnviado();

            pedidoConStockObserver.cancelarPedido();

            verify(catalogoMock, never()).reponerStock(anyList());
        }
    }
}
