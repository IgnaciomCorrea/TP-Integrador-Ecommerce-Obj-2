package pedido;

import Catalogo.Catalogo;
import Catalogo.ItemVendible;
import exceptions.ExcepcionGeneral;
import notificaciones.ObservadorPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        pedido = new Pedido();
        // No se hace ningún stub en setUp para evitar UnnecessaryStubbingException
    }

    // =========================================================================
    // 1. ESTADO INICIAL Y OPERACIONES BÁSICAS (no requieren stubs)
    // =========================================================================

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

        @Test
        void quitarVendible_inexistente_enBorrador_noLanzaExcepcion() {
            assertDoesNotThrow(() -> pedido.quitarVendible(itemMock1));
        }
    }

    // =========================================================================
    // 2. CONFIRMAR PEDIDO (no requiere stubs de métodos de precio/peso)
    // =========================================================================

    @Nested
    class ConfirmarPedido {
        @Test
        void confirmarPedido_sinVendibles_debeLanzarExcepcion() {
            assertThrows(ExcepcionGeneral.class, () -> pedido.confirmarPedido());
            assertTrue(pedido.getEstado() instanceof Borrador);
        }

        @Test
        void confirmarPedido_conVendibles_debePasarAConfirmado() {
            pedido.agregarVendible(itemMock1);
            pedido.confirmarPedido();
            assertTrue(pedido.getEstado() instanceof Confirmado);
        }

        @Test
        void confirmarPedido_desdeConfirmado_debeLanzarExcepcion() {
            pedido.agregarVendible(itemMock1);
            pedido.confirmarPedido();
            assertThrows(ExcepcionGeneral.class, () -> pedido.confirmarPedido());
        }

        @Test
        void confirmarPedido_desdeCancelado_debeLanzarExcepcion() {
            pedido.cancelarPedido();
            assertThrows(ExcepcionGeneral.class, () -> pedido.confirmarPedido());
        }
    }

    // =========================================================================
    // 3. CANCELAR PEDIDO (no requiere stubs, solo el item para agregar)
    // =========================================================================

    @Nested
    class CancelarPedido {
        @Test
        void cancelarPedido_desdeBorrador_debePasarACancelado() {
            pedido.cancelarPedido();
            assertTrue(pedido.getEstado() instanceof Cancelado);
            assertNull(pedido.getNotaCredito());
        }

        @Test
        void cancelarPedido_desdeConfirmado_debePasarACanceladoYGenerarNota() {
            pedido.agregarVendible(itemMock1);
            pedido.confirmarPedido();
            pedido.cancelarPedido();
            assertTrue(pedido.getEstado() instanceof Cancelado);
            assertNotNull(pedido.getNotaCredito());
        }

        @Test
        void cancelarPedido_desdeEnPreparacion_debePasarACanceladoYGenerarNota() {
            pedido.agregarVendible(itemMock1);
            pedido.confirmarPedido();
            pedido.pasarAEnPreparacion();
            pedido.cancelarPedido();
            assertTrue(pedido.getEstado() instanceof Cancelado);
            assertNotNull(pedido.getNotaCredito());
        }

        @Test
        void cancelarPedido_desdeEnviado_debePasarACanceladoYGenerarNotaSinEnvio() {
            pedido.agregarVendible(itemMock1);
            pedido.confirmarPedido();
            pedido.pasarAEnPreparacion();
            pedido.pasarAEnviado();
            pedido.cancelarPedido();
            assertTrue(pedido.getEstado() instanceof Cancelado);
            assertNotNull(pedido.getNotaCredito());
        }

        @Test
        void cancelarPedido_desdeEntregado_debeLanzarExcepcion() {
            pedido.agregarVendible(itemMock1);
            pedido.confirmarPedido();
            pedido.pasarAEnPreparacion();
            pedido.pasarAEnviado();
            pedido.pasarAEntregado();
            assertThrows(ExcepcionGeneral.class, () -> pedido.cancelarPedido());
        }

        @Test
        void cancelarPedido_desdeCancelado_debeLanzarExcepcion() {
            pedido.cancelarPedido();
            assertThrows(ExcepcionGeneral.class, () -> pedido.cancelarPedido());
        }
    }

    // =========================================================================
    // 4. TRANSICIONES VÁLIDAS (no requieren stubs)
    // =========================================================================

    @Nested
    class TransicionesValidas {
        @Test
        void pasarAEnPreparacion_desdeConfirmado_debePasarAEnPreparacion() {
            pedido.agregarVendible(itemMock1);
            pedido.confirmarPedido();
            pedido.pasarAEnPreparacion();
            assertTrue(pedido.getEstado() instanceof EnPreparacion);
        }

        @Test
        void pasarAEnviado_desdeEnPreparacion_debePasarAEnviado() {
            pedido.agregarVendible(itemMock1);
            pedido.confirmarPedido();
            pedido.pasarAEnPreparacion();
            pedido.pasarAEnviado();
            assertTrue(pedido.getEstado() instanceof Enviado);
        }

        @Test
        void pasarAEntregado_desdeEnviado_debePasarAEntregado() {
            pedido.agregarVendible(itemMock1);
            pedido.confirmarPedido();
            pedido.pasarAEnPreparacion();
            pedido.pasarAEnviado();
            pedido.pasarAEntregado();
            assertTrue(pedido.getEstado() instanceof Entregado);
        }
    }

    // =========================================================================
    // 5. TRANSICIONES INVÁLIDAS (no requieren stubs)
    // =========================================================================

    @Nested
    class TransicionesInvalidas {
        // ... (todos los tests, igual que antes, pero sin stubs)
        // Los omito por brevedad, pero están en la versión completa que te pasé.
    }

    // =========================================================================
    // 6. OPERACIONES EN ESTADOS TERMINALES (no requieren stubs)
    // =========================================================================

    @Nested
    class EstadosTerminales {
        // ... (tests)
    }

    // =========================================================================
    // 7. CÁLCULOS (PRECIO Y PESO) – requieren stubs de getPrecioFinal y getPeso
    // =========================================================================

    @Nested
    class Calculos {
        @Test
        void calcularPrecioTotal_debeSumarPreciosFinales() {
            when(itemMock1.getPrecioFinal()).thenReturn(100.0);
            when(itemMock2.getPrecioFinal()).thenReturn(50.0);

            pedido.agregarVendible(itemMock1);
            pedido.agregarVendible(itemMock2);
            assertEquals(150.0, pedido.calcularPrecioTotal());
        }

        @Test
        void calcularPesoTotal_debeSumarPesos() {
            when(itemMock1.getPeso()).thenReturn(0.5);
            when(itemMock2.getPeso()).thenReturn(0.2);

            pedido.agregarVendible(itemMock1);
            pedido.agregarVendible(itemMock2);
            assertEquals(0.7, pedido.calcularPesoTotal());
        }

        @Test
        void calcularPrecioTotal_conPedidoVacio_debeRetornarCero() {
            assertEquals(0.0, new Pedido().calcularPrecioTotal());
        }

        @Test
        void calcularPesoTotal_conPedidoVacio_debeRetornarCero() {
            assertEquals(0.0, new Pedido().calcularPesoTotal());
        }
    }

    // =========================================================================
    // 8. OBSERVADOR DE STOCK – requieren stubs de getSku() y getCantidad()
    // =========================================================================

    @Nested
    class ObservadorStockTest {
        private Pedido pedidoConStockObserver;

        @BeforeEach
        void setUp() {
            pedidoConStockObserver = new Pedido();
            pedidoConStockObserver.agregarObservador(new ObservadorStock(catalogoMock));
        }

        @Test
        void alConfirmarPedido_debeLlamarARestarStock() {
            when(itemMock1.getSku()).thenReturn("SKU001");
            when(itemMock1.getCantidad()).thenReturn(2);

            pedidoConStockObserver.agregarVendible(itemMock1);
            pedidoConStockObserver.confirmarPedido();

            ArgumentCaptor<List<ItemVendible>> captor = ArgumentCaptor.forClass(List.class);
            verify(catalogoMock, times(1)).restarStock(captor.capture());
            List<ItemVendible> itemsRestados = captor.getValue();
            assertTrue(itemsRestados.contains(itemMock1));
        }

        @Test
        void alCancelarDesdeConfirmado_debeLlamarAReponerStock() {
            when(itemMock1.getSku()).thenReturn("SKU001");
            when(itemMock1.getCantidad()).thenReturn(2);

            pedidoConStockObserver.agregarVendible(itemMock1);
            pedidoConStockObserver.confirmarPedido();
            pedidoConStockObserver.cancelarPedido();

            verify(catalogoMock, times(1)).reponerStock(anyList());
        }

        @Test
        void alCancelarDesdeEnPreparacion_debeLlamarAReponerStock() {
            when(itemMock1.getSku()).thenReturn("SKU001");
            when(itemMock1.getCantidad()).thenReturn(2);

            pedidoConStockObserver.agregarVendible(itemMock1);
            pedidoConStockObserver.confirmarPedido();
            pedidoConStockObserver.pasarAEnPreparacion();
            pedidoConStockObserver.cancelarPedido();

            verify(catalogoMock, times(1)).reponerStock(anyList());
        }

        @Test
        void alCancelarDesdeBorrador_noDebeLlamarAReponerStock() {
            pedidoConStockObserver.agregarVendible(itemMock1);
            pedidoConStockObserver.cancelarPedido();
            verify(catalogoMock, never()).reponerStock(anyList());
        }

        @Test
        void alCancelarDesdeEnviado_noDebeLlamarAReponerStock() {
            when(itemMock1.getSku()).thenReturn("SKU001");
            when(itemMock1.getCantidad()).thenReturn(2);

            pedidoConStockObserver.agregarVendible(itemMock1);
            pedidoConStockObserver.confirmarPedido();
            pedidoConStockObserver.pasarAEnPreparacion();
            pedidoConStockObserver.pasarAEnviado();
            pedidoConStockObserver.cancelarPedido();

            verify(catalogoMock, never()).reponerStock(anyList());
        }

        @Test
        void alConfirmarPedido_conVariosItems_debeLlamarARestarStockConTodos() {
            when(itemMock1.getSku()).thenReturn("SKU001");
            when(itemMock1.getCantidad()).thenReturn(2);
            when(itemMock2.getSku()).thenReturn("SKU002");
            when(itemMock2.getCantidad()).thenReturn(3);

            pedidoConStockObserver.agregarVendible(itemMock1);
            pedidoConStockObserver.agregarVendible(itemMock2);
            pedidoConStockObserver.confirmarPedido();

            ArgumentCaptor<List<ItemVendible>> captor = ArgumentCaptor.forClass(List.class);
            verify(catalogoMock, times(1)).restarStock(captor.capture());
            List<ItemVendible> itemsRestados = captor.getValue();
            assertEquals(2, itemsRestados.size());
            assertTrue(itemsRestados.contains(itemMock1));
            assertTrue(itemsRestados.contains(itemMock2));
        }
    }

    // =========================================================================
    // 9. NOTA DE CRÉDITO (no requiere stubs, solo el item para agregar)
    // =========================================================================

    @Nested
    class NotaCreditoTest {
        private Pedido pedidoConItems;

        @BeforeEach
        void setUp() {
            pedidoConItems = new Pedido();
            pedidoConItems.agregarVendible(itemMock1);
        }

        // ... (tests iguales que antes, sin stubs)
    }

    // =========================================================================
    // 10. GETTERS Y UTILIDADES
    // =========================================================================

    @Nested
    class Getters {
        // ... (tests)
    }
}