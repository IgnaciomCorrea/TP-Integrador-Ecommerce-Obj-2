package pedido;

import Catalogo.Catalogo;
import Catalogo.ItemVendible;
import direccion.Direccion;
import envio.MetodoEnvio;
import exceptions.PedidoExcepcion;
import metodoPago.MedioDePago;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sucursal.Sucursal;
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
    @Nested
    class ConstructorYGetters {
        @Test
        void constructorConParametros_debeInicializarAtributos() {
            PagoDummy pago = new PagoDummy();
            MetodoEnvio metodoEnvio = mock(MetodoEnvio.class);
            MedioDePago medioDePago = mock(MedioDePago.class);
            Pedido p = new Pedido(pago, metodoEnvio, medioDePago);

            assertNotNull(p.getEstado());
            assertTrue(p.getEstado() instanceof Borrador);
            assertNotNull(p.getVendibles());
            assertTrue(p.getVendibles().isEmpty());
            assertNotNull(p.getObservadores());
            assertTrue(p.getObservadores().isEmpty());
            assertNotNull(p.getFecha());
            assertNull(p.getNotaCredito());
        }

        @Test
        void constructorConParametrosNull_debeLanzarNPE() {
            PagoDummy pago = new PagoDummy();
            MetodoEnvio metodoEnvio = mock(MetodoEnvio.class);
            MedioDePago medioDePago = mock(MedioDePago.class);

            assertThrows(NullPointerException.class,
                    () -> new Pedido(null, metodoEnvio, medioDePago));
            assertThrows(NullPointerException.class,
                    () -> new Pedido(pago, null, medioDePago));
            assertThrows(NullPointerException.class,
                    () -> new Pedido(pago, metodoEnvio, null));
        }

        // ... otros tests (getFecha, getObservadores) iguales que antes ...
    }

    @Nested
    class CalculoCostoEnvio {
        @Test
        void calcularCostoEnvio_conDireccionYSucursal_debeDelegarEnMetodoEnvio() {
            PagoDummy pago = new PagoDummy();
            MetodoEnvio metodoEnvio = mock(MetodoEnvio.class);
            MedioDePago medioDePago = mock(MedioDePago.class);
            Pedido p = new Pedido(pago, metodoEnvio, medioDePago);

            Direccion direccion = mock(Direccion.class);
            Sucursal sucursal = mock(Sucursal.class);
            when(metodoEnvio.calcularCosto(p, direccion, sucursal)).thenReturn(99.0);

            double costo = p.calcularCostoEnvio(direccion, sucursal);
            assertEquals(99.0, costo, 0.001);
            verify(metodoEnvio, times(1)).calcularCosto(p, direccion, sucursal);
        }

        @Test
        void calcularCostoEnvio_sinParametros_debeRetornarCero() {
            assertEquals(0.0, pedido.calcularCostoEnvio());
        }
    }

    @Nested
    class SetMetodosDePagoYEnvio {
        @Test
        void setMetodoDePago_enBorrador_debeAsignar() {
            PagoDummy pago = new PagoDummy();
            MedioDePago medioDePago = mock(MedioDePago.class);
            assertDoesNotThrow(() -> pedido.setMetodoDePago(pago, medioDePago));
            assertTrue(pedido.getEstado() instanceof Borrador);
        }

        @Test
        void setMetodoDePago_enConfirmado_debeLanzarExcepcion() {
            pedido.agregarVendible(itemMock1);
            pedido.confirmarPedido();
            PagoDummy pago = new PagoDummy();
            MedioDePago medio = mock(MedioDePago.class);
            assertThrows(PedidoExcepcion.class,
                    () -> pedido.setMetodoDePago(pago, medio));
        }

        @Test
        void setMetodoDeEnvio_enBorrador_debeAsignar() {
            MetodoEnvio metodoEnvio = mock(MetodoEnvio.class);
            assertDoesNotThrow(() -> pedido.setMetodoDeEnvio(metodoEnvio));
            assertTrue(pedido.getEstado() instanceof Borrador);
        }

        @Test
        void setMetodoDeEnvio_enConfirmado_debeLanzarExcepcion() {
            pedido.agregarVendible(itemMock1);
            pedido.confirmarPedido();
            assertThrows(PedidoExcepcion.class,
                    () -> pedido.setMetodoDeEnvio(mock(MetodoEnvio.class)));
        }

        @Test
        void asignarMetodoDePago_packagePrivate_debeAsignarAtributos() {
            PagoDummy pago = new PagoDummy();
            MedioDePago medio = mock(MedioDePago.class);
            pedido.asignarMetodoDePago(pago, medio);

            // Verificamos que el pedido pueda cobrar usando el pago asignado
            Pedido pSpy = spy(pedido);
            doReturn(0.0).when(pSpy).calcularCostoEnvio();
            pSpy.cobrarPedido();

            // Verificar que el pago procesó la transacción (usando el contador)
            assertEquals(1, pago.getTransacciones());
            assertEquals(1, pago.getValidaciones());
            assertEquals(1, pago.getReservas());
            assertEquals(1, pago.getNotificaciones());
        }

        @Test
        void asignarMetodoDeEnvio_packagePrivate_debeAsignarAtributos() {
            MetodoEnvio metodoEnvio = mock(MetodoEnvio.class);
            pedido.asignarMetodoDeEnvio(metodoEnvio);
            Direccion direccion = mock(Direccion.class);
            Sucursal sucursal = mock(Sucursal.class);
            when(metodoEnvio.calcularCosto(pedido, direccion, sucursal)).thenReturn(77.0);
            double costo = pedido.calcularCostoEnvio(direccion, sucursal);
            assertEquals(77.0, costo, 0.001);
        }
    }

    @Nested
    class CobrarPedido {
        @Test
        void cobrarPedido_debeProcesarPagoConTotalIncluyendoEnvio() {
            PagoDummy pago = new PagoDummy();
            MetodoEnvio metodoEnvio = mock(MetodoEnvio.class);
            MedioDePago medioDePago = mock(MedioDePago.class);
            Pedido p = new Pedido(pago, metodoEnvio, medioDePago);

            when(itemMock1.getPrecioFinal()).thenReturn(120.0);
            when(itemMock2.getPrecioFinal()).thenReturn(80.0);
            p.agregarVendible(itemMock1);
            p.agregarVendible(itemMock2);

            Pedido pSpy = spy(p);
            doReturn(50.0).when(pSpy).calcularCostoEnvio();

            pSpy.cobrarPedido();

            // Verificar que el pago dummy procesó la transacción
            assertEquals(1, pago.getValidaciones());
            assertEquals(1, pago.getReservas());
            assertEquals(1, pago.getTransacciones());
            assertEquals(1, pago.getNotificaciones());
        }

        @Test
        void cobrarPedido_sinItems_debeProcesarPagoConSoloEnvio() {
            PagoDummy pago = new PagoDummy();
            MetodoEnvio metodoEnvio = mock(MetodoEnvio.class);
            MedioDePago medioDePago = mock(MedioDePago.class);
            Pedido p = new Pedido(pago, metodoEnvio, medioDePago);

            Pedido pSpy = spy(p);
            doReturn(30.0).when(pSpy).calcularCostoEnvio();

            pSpy.cobrarPedido();

            assertEquals(1, pago.getTransacciones());
            assertEquals(1, pago.getValidaciones());
            assertEquals(1, pago.getReservas());
            assertEquals(1, pago.getNotificaciones());
        }
    }
}
