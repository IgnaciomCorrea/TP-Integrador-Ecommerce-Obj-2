package Sistema;

import Catalogo.*;
import exceptions.PedidoExcepcion;
import pedido.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sistema.Sistema;
import testutils.PedidoFactory;

import static org.junit.jupiter.api.Assertions.*;

class SistemaArmarPedidoTest {

    private Catalogo catalogo;
    private Sistema sistema;
    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    void setUp() {
        catalogo = new Catalogo();
        sistema = new Sistema(catalogo);

        producto1 = new Producto("SKU001", "Teclado", "Logitech", Categoria.ELECTRONICA,
                "Teclado mecánico", 0.0, 100.0, 0.5);
        producto2 = new Producto("SKU002", "Mouse", "Logitech", Categoria.ELECTRONICA,
                "Mouse inalámbrico", 0.0, 50.0, 0.2);

        catalogo.agregarVendible(new StockVendible(10, producto1));
        catalogo.agregarVendible(new StockVendible(5, producto2));
    }

    @Test
    void armarPedido_conStockSuficiente_debeConfirmarYGuardar() {
        Pedido pedido = PedidoFactory.pedido();
        pedido.agregarVendible(new ItemVendible(3, producto1));
        pedido.agregarVendible(new ItemVendible(2, producto2));

        sistema.armarPedido(pedido);

        assertEquals(1, sistema.getPedidos().size());
        assertTrue(sistema.getPedidos().contains(pedido));
        assertTrue(pedido.getEstado() instanceof Confirmado);

        assertTrue(pedido.getObservadores().stream()
                .anyMatch(obs -> obs instanceof ObservadorStock));

        assertEquals(7, catalogo.buscarVendible("SKU001").getStock());
        assertEquals(3, catalogo.buscarVendible("SKU002").getStock());
    }

    @Test
    void armarPedido_sinStock_debeCancelarYGuardar() {
        Pedido pedido = PedidoFactory.pedido();
        pedido.agregarVendible(new ItemVendible(15, producto1));

        sistema.armarPedido(pedido);

        assertEquals(1, sistema.getPedidos().size());
        assertTrue(pedido.getEstado() instanceof Cancelado);
        assertEquals(10, catalogo.buscarVendible("SKU001").getStock());
    }

    @Test
    void armarPedido_conPedidoNull_debeLanzarExcepcion() {
        assertThrows(PedidoExcepcion.class, () -> sistema.armarPedido(null));
    }

    @Test
    void armarPedido_conVariosItemsYStockSuficiente_debeConfirmar() {
        Pedido pedido = PedidoFactory.pedido();
        pedido.agregarVendible(new ItemVendible(1, producto1));
        pedido.agregarVendible(new ItemVendible(1, producto2));

        sistema.armarPedido(pedido);

        assertTrue(pedido.getEstado() instanceof Confirmado);
        assertEquals(1, sistema.getPedidos().size());
        assertEquals(9, catalogo.buscarVendible("SKU001").getStock());
        assertEquals(4, catalogo.buscarVendible("SKU002").getStock());
    }

    @Test
    void armarPedido_conItemInexistenteEnCatalogo_debeLanzarExcepcion() {
        Producto p3 = new Producto("SKU999", "Monitor", "Samsung", Categoria.ELECTRONICA,
                "Monitor 4K", 0.0, 300.0, 2.0);
        Pedido pedido = PedidoFactory.pedido();
        pedido.agregarVendible(new ItemVendible(1, p3));

        assertThrows(RuntimeException.class, () -> sistema.armarPedido(pedido));
        assertEquals(1, sistema.getPedidos().size());
        assertTrue(pedido.getEstado() instanceof Borrador);
    }
}