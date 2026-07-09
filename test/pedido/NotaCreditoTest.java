package pedido;

import envio.MetodoEnvio;
import exceptions.CatalogoException;
import exceptions.ConstructorException;
import exceptions.PedidoExcepcion;
import metodoPago.MedioDePago;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testutils.PedidoFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class NotaCreditoTest {

    @Nested
    class ConstructorYGetters {
        @Test
        void constructorConParametros_debeInicializarAtributos() {
            PedidoFactory.PagoDummy pago = new PedidoFactory.PagoDummy();
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
            PedidoFactory.PagoDummy pago = new PedidoFactory.PagoDummy();
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

    @Test
    void crear_conParametrosValidos_debeCrearNota() {
        // ✅ Usamos PedidoFactory para crear un pedido sin necesidad de mockear todo
        Pedido pedido = PedidoFactory.pedido();
        NotaCredito nota = NotaCredito.crear(pedido, 100.0, "Reembolso");

        assertNotNull(nota);
        assertNotNull(nota.getId());
        assertSame(pedido, nota.getPedido());
        assertEquals(100.0, nota.getMonto());
        assertEquals("Reembolso", nota.getMotivo());
        assertNotNull(nota.getFechaEmision());
    }

    @Test
    void crear_conPedidoNull_debeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> NotaCredito.crear(null, 100.0, "Motivo"));
    }

    @Test
    void crear_conMontoNegativo_debeLanzarExcepcion() {
        Pedido pedido = PedidoFactory.pedido();
        assertThrows(IllegalArgumentException.class,
                () -> NotaCredito.crear(pedido, -50.0, "Motivo"));
    }

    @Test
    void crear_conMontoCero_debeCrearNota() {
        Pedido pedido = PedidoFactory.pedido();
        NotaCredito nota = NotaCredito.crear(pedido, 0.0, "Motivo");
        assertNotNull(nota);
        assertEquals(0.0, nota.getMonto());
    }

    @Test
    void crear_conMotivoNull_debeLanzarExcepcion() {
        Pedido pedido = PedidoFactory.pedido();
        assertThrows(IllegalArgumentException.class,
                () -> NotaCredito.crear(pedido, 100.0, null));
    }

    @Test
    void crear_conMotivoVacio_debeLanzarExcepcion() {
        Pedido pedido = PedidoFactory.pedido();
        assertThrows(IllegalArgumentException.class,
                () -> NotaCredito.crear(pedido, 100.0, ""));
    }

    @Test
    void crear_conMotivoSoloEspacios_debeLanzarExcepcion() {
        Pedido pedido = PedidoFactory.pedido();
        assertThrows(IllegalArgumentException.class,
                () -> NotaCredito.crear(pedido, 100.0, "   "));
    }

    @Test
    void toString_debeContenerDatos() {
        Pedido pedido = PedidoFactory.pedido();
        NotaCredito nota = NotaCredito.crear(pedido, 100.0, "Motivo");
        String str = nota.toString();
        assertTrue(str.contains("NotaCredito"));
        assertTrue(str.contains("monto=100.0"));
        assertTrue(str.contains("motivo='Motivo'"));
    }
}