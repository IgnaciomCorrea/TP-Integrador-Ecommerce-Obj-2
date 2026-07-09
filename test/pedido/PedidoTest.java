package pedido;

import Catalogo.Catalogo;
import Catalogo.Categoria;
import Catalogo.ItemVendible;
import Catalogo.Producto;
import direccion.Direccion;
import envio.MetodoEnvio;
import exceptions.ConstructorException;
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
    private Producto producto1;
    private Producto producto2;

    @Mock
    private Catalogo catalogoMock;

    @BeforeEach
    void setUp() {
        pedido = PedidoFactory.pedido();
        producto1 = new Producto("SKU001", "Teclado", "Logitech", Categoria.ELECTRONICA,
                "Teclado mecánico", 0.0, 100.0, 0.5);
        producto2 = new Producto("SKU002", "Mouse", "Logitech", Categoria.ELECTRONICA,
                "Mouse inalámbrico", 0.0, 50.0, 0.2);
    }

    @Nested
    class EstadoInicialYOperaciones {
        @Test
        void nuevoPedido_debeEstarEnBorrador() {
            assertTrue(pedido.getEstado() instanceof Borrador);
        }

        @Test
        void agregarVendible_enBorrador_debeAgregar() {
            pedido.agregarVendible(new ItemVendible(1, producto1));
            assertTrue(pedido.getVendibles().stream().anyMatch(i -> i.getSku().equals("SKU001")));
        }

        @Test
        void quitarVendible_enBorrador_debeQuitar() {
            ItemVendible item = new ItemVendible(1, producto1);
            pedido.agregarVendible(item);
            pedido.quitarVendible(item);
            assertTrue(pedido.getVendibles().isEmpty());
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
            pedidoConPago.agregarVendible(new ItemVendible(1, producto1));

            pedidoConPago.confirmarPedido();

            assertTrue(pedidoConPago.getEstado() instanceof Confirmado);
            assertEquals(1, pago.getValidaciones());
            assertEquals(1, pago.getReservas());
            assertEquals(1, pago.getTransacciones());
            assertEquals(1, pago.getNotificaciones());
        }

        @Test
        void confirmarPedido_desdeConfirmado_debeLanzarExcepcion() {
            pedido.agregarVendible(new ItemVendible(1, producto1));
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
            pedido.agregarVendible(new ItemVendible(1, producto1));
            pedido.confirmarPedido();

            pedido.cancelarPedido();

            assertTrue(pedido.getEstado() instanceof Cancelado);
            assertNotNull(pedido.getNotaCredito());
            assertEquals(100.0, pedido.getNotaCredito().getMonto(), 0.001);
        }

        @Test
        void cancelarPedido_desdeEnviado_debeReembolsarSoloProductos() {
            Producto productoConDescuento = new Producto("SKU003", "Producto con desc", "Marca",
                    Categoria.ELECTRONICA, "Desc", 20.0, 80.0, 0.5);
            pedido.agregarVendible(new ItemVendible(1, productoConDescuento));
            pedido.confirmarPedido();
            pedido.pasarAEnPreparacion();
            pedido.pasarAEnviado();

            pedido.cancelarPedido();

            assertTrue(pedido.getEstado() instanceof Cancelado);
            // El precio final con descuento: 80 - (80*0.20) = 64
            assertEquals(64.0, pedido.getNotaCredito().getMonto(), 0.001);
        }

        @Test
        void cancelarPedido_desdeEntregado_debeLanzarExcepcion() {
            pedido.agregarVendible(new ItemVendible(1, producto1));
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
            pedido.agregarVendible(new ItemVendible(1, producto1));

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
            pedido.agregarVendible(new ItemVendible(1, producto1));
            pedido.agregarVendible(new ItemVendible(1, producto2));
            assertEquals(150.0, pedido.calcularPrecioTotal(), 0.001);
        }

        @Test
        void calcularPesoTotal_debeSumarPesos() {
            pedido.agregarVendible(new ItemVendible(1, producto1));
            pedido.agregarVendible(new ItemVendible(1, producto2));
            assertEquals(0.7, pedido.calcularPesoTotal(), 0.001);
        }

        @Test
        void calcularPrecioTotal_conProductoConDescuento() {
            Producto pConDesc = new Producto("SKU003", "Producto con desc", "Marca",
                    Categoria.ELECTRONICA, "Desc", 20.0, 100.0, 0.5);
            pedido.agregarVendible(new ItemVendible(1, pConDesc));
            // Precio final: 100 - (100*0.20) = 80
            assertEquals(80.0, pedido.calcularPrecioTotal(), 0.001);
        }

        @Test
        void calcularPesoTotal_conItemsConCantidades() {
            // Agregamos 2 unidades del producto1 (peso 0.5 cada uno) y 3 del producto2 (peso 0.2)
            pedido.agregarVendible(new ItemVendible(2, producto1));
            pedido.agregarVendible(new ItemVendible(3, producto2));
            assertEquals(2 * 0.5 + 3 * 0.2, pedido.calcularPesoTotal(), 0.001);
        }
    }

    @Nested
    class ObservadorStockTest {
        private Pedido pedidoConStockObserver;
        private Producto producto1;

        @BeforeEach
        void setUp() {
            pedidoConStockObserver = PedidoFactory.pedido();
            pedidoConStockObserver.agregarObservador(new ObservadorStock(catalogoMock));
            producto1 = new Producto("SKU001", "Teclado", "Logitech", Categoria.ELECTRONICA,
                    "Teclado", 0.0, 100.0, 0.5);
        }

        @Test
        void alConfirmarPedido_debeLlamarAArmarPedido() {
            ItemVendible item = new ItemVendible(1, producto1);
            pedidoConStockObserver.agregarVendible(item);

            pedidoConStockObserver.confirmarPedido();

            // El observador llama a catalogo.armarPedido(pedido)
            verify(catalogoMock, times(1)).armarPedido(pedidoConStockObserver);
            // No se llama directamente a restarStock
            verify(catalogoMock, never()).restarStock(anyList());
        }

        @Test
        void alCancelarDesdeConfirmado_debeLlamarAReponerStock() {
            pedidoConStockObserver.agregarVendible(new ItemVendible(1, producto1));
            pedidoConStockObserver.confirmarPedido();

            pedidoConStockObserver.cancelarPedido();

            verify(catalogoMock).reponerStock(anyList());
        }

        @Test
        void alCancelarDesdeEnviado_noDebeReponerStock() {
            pedidoConStockObserver.agregarVendible(new ItemVendible(1, producto1));
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
        void constructorConParametrosNull_debeLanzarConstructorExcepcion() {
            PagoDummy pago = new PagoDummy();
            MetodoEnvio metodoEnvio = mock(MetodoEnvio.class);
            MedioDePago medioDePago = mock(MedioDePago.class);

            assertThrows(ConstructorException.class,
                    () -> new Pedido(null, metodoEnvio, medioDePago));
            assertThrows(ConstructorException.class,
                    () -> new Pedido(pago, null, medioDePago));
            assertThrows(ConstructorException.class,
                    () -> new Pedido(pago, metodoEnvio, null));
        }
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
            pedido.agregarVendible(new ItemVendible(1, producto1));
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
            pedido.agregarVendible(new ItemVendible(1, producto1));
            pedido.confirmarPedido();
            assertThrows(PedidoExcepcion.class,
                    () -> pedido.setMetodoDeEnvio(mock(MetodoEnvio.class)));
        }

        @Test
        void asignarMetodoDePago_packagePrivate_debeAsignarAtributos() {
            PagoDummy pago = new PagoDummy();
            MedioDePago medio = mock(MedioDePago.class);
            pedido.asignarMetodoDePago(pago, medio);

            Pedido pSpy = spy(pedido);
            doReturn(0.0).when(pSpy).calcularCostoEnvio();
            pSpy.cobrarPedido();

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

            // Productos con precios fijos
            Producto p1 = new Producto("SKU001", "Teclado", "Logitech", Categoria.ELECTRONICA,
                    "Teclado", 0.0, 120.0, 0.5);
            Producto p2 = new Producto("SKU002", "Mouse", "Logitech", Categoria.ELECTRONICA,
                    "Mouse", 0.0, 80.0, 0.2);
            p.agregarVendible(new ItemVendible(1, p1));
            p.agregarVendible(new ItemVendible(1, p2));

            Pedido pSpy = spy(p);
            doReturn(50.0).when(pSpy).calcularCostoEnvio();

            pSpy.cobrarPedido();

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