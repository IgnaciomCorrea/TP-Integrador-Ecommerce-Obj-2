package Catalogo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import pedido.Pedido;
import pedido.Cancelado;
import pedido.Borrador;
import testutils.PedidoFactory;

import static org.junit.jupiter.api.Assertions.*;

class CatalogoTest {

    private Catalogo catalogo;
    private Producto producto1;
    private Producto producto2;
    private StockVendible stock1;
    private StockVendible stock2;

    @BeforeEach
    void setUp() {
        catalogo = new Catalogo();

        producto1 = new Producto("SKU001", "Teclado", "Logitech", Categoria.ELECTRONICA,
                "Teclado mecánico", 0.0, 100.0, 0.5);
        producto2 = new Producto("SKU002", "Mouse", "Logitech", Categoria.ELECTRONICA,
                "Mouse inalámbrico", 0.0, 50.0, 0.2);

        stock1 = new StockVendible(10, producto1);
        stock2 = new StockVendible(5, producto2);

        catalogo.agregarVendible(stock1);
        catalogo.agregarVendible(stock2);
    }

    @Test
    @DisplayName("buscarVendible debe retornar el StockVendible para un SKU existente")
    void buscarVendible_existente() {
        StockVendible encontrado = catalogo.buscarVendible("SKU001");
        assertNotNull(encontrado);
        assertEquals(producto1, encontrado.getVendible());
        assertEquals(10, encontrado.getStock());

        StockVendible encontrado2 = catalogo.buscarVendible("SKU002");
        assertNotNull(encontrado2);
        assertEquals(producto2, encontrado2.getVendible());
        assertEquals(5, encontrado2.getStock());
    }

    @Test
    @DisplayName("buscarVendible con SKU inexistente debe lanzar RuntimeException")
    void buscarVendible_inexistente() {
        assertThrows(RuntimeException.class, () -> catalogo.buscarVendible("SKU999"));
    }

    @Test
    @DisplayName("hayStockDisponible debe retornar true si hay suficiente stock")
    void hayStockDisponible_suficiente() {
        assertTrue(catalogo.hayStockDisponible("SKU001", 5));
        assertTrue(catalogo.hayStockDisponible("SKU002", 3));
    }

    @Test
    @DisplayName("hayStockDisponible debe retornar false si no hay suficiente stock")
    void hayStockDisponible_insuficiente() {
        assertFalse(catalogo.hayStockDisponible("SKU001", 15));
        assertFalse(catalogo.hayStockDisponible("SKU002", 10));
    }

    @Test
    @DisplayName("hayStockDisponible debe lanzar RuntimeException si el SKU no existe")
    void hayStockDisponible_skuInexistente() {
        assertThrows(RuntimeException.class, () -> catalogo.hayStockDisponible("SKU999", 1));
    }

    @Test
    @DisplayName("verificarStockPedido debe retornar true si todos los items tienen stock suficiente")
    void verificarStockPedido_true() {
        Pedido pedido = PedidoFactory.pedido();
        pedido.agregarVendible(new ItemVendible(3, producto1));
        pedido.agregarVendible(new ItemVendible(2, producto2));

        assertTrue(catalogo.verificarStockPedido(pedido));
    }

    @Test
    @DisplayName("verificarStockPedido debe retornar false si algún item no tiene stock suficiente")
    void verificarStockPedido_false() {
        Pedido pedido = PedidoFactory.pedido();
        pedido.agregarVendible(new ItemVendible(12, producto1)); // solo hay 10
        pedido.agregarVendible(new ItemVendible(2, producto2));

        assertFalse(catalogo.verificarStockPedido(pedido));
    }

    @Test
    @DisplayName("verificarStockPedido debe lanzar RuntimeException si algún SKU no existe en stock")
    void verificarStockPedido_skuInexistente() {
        Producto p3 = new Producto("SKU999", "Monitor", "Samsung", Categoria.ELECTRONICA,
                "Monitor 4K", 0.0, 300.0, 2.0);
        Pedido pedido = PedidoFactory.pedido();
        pedido.agregarVendible(new ItemVendible(1, p3));

        assertThrows(RuntimeException.class, () -> catalogo.verificarStockPedido(pedido));
    }

    @Test
    @DisplayName("restarStock debe disminuir el stock de los items del pedido")
    void restarStock() {
        Pedido pedido = PedidoFactory.pedido();
        pedido.agregarVendible(new ItemVendible(3, producto1));
        pedido.agregarVendible(new ItemVendible(2, producto2));

        catalogo.restarStock(pedido.getVendibles());

        StockVendible s1 = catalogo.buscarVendible("SKU001");
        assertEquals(7, s1.getStock()); // 10 - 3 = 7

        StockVendible s2 = catalogo.buscarVendible("SKU002");
        assertEquals(3, s2.getStock()); // 5 - 2 = 3
    }

    @Test
    @DisplayName("restarStock debe permitir stock negativo (sin validación)")
    void restarStock_negativo() {
        Pedido pedido = PedidoFactory.pedido();
        pedido.agregarVendible(new ItemVendible(15, producto1)); // más que el stock

        catalogo.restarStock(pedido.getVendibles());

        StockVendible s1 = catalogo.buscarVendible("SKU001");
        assertEquals(-5, s1.getStock()); // 10 - 15 = -5
    }

    @Test
    @DisplayName("reponerStock debe aumentar el stock de los items del pedido")
    void reponerStock() {
        Pedido pedido = PedidoFactory.pedido();
        pedido.agregarVendible(new ItemVendible(3, producto1));
        pedido.agregarVendible(new ItemVendible(2, producto2));

        catalogo.reponerStock(pedido.getVendibles());

        StockVendible s1 = catalogo.buscarVendible("SKU001");
        assertEquals(13, s1.getStock()); // 10 + 3 = 13

        StockVendible s2 = catalogo.buscarVendible("SKU002");
        assertEquals(7, s2.getStock()); // 5 + 2 = 7
    }

    @Test
    @DisplayName("armarPedido con stock suficiente debe restar stock y no cambiar estado")
    void armarPedido_stockSuficiente() {
        Pedido pedido = PedidoFactory.pedido();
        pedido.agregarVendible(new ItemVendible(3, producto1));
        pedido.agregarVendible(new ItemVendible(2, producto2));

        catalogo.armarPedido(pedido);

        // Verificar que el stock se restó
        assertEquals(7, catalogo.buscarVendible("SKU001").getStock());
        assertEquals(3, catalogo.buscarVendible("SKU002").getStock());

        // El pedido debería seguir en Borrador (no se confirma en Catalogo.armarPedido)
        assertTrue(pedido.getEstado() instanceof Borrador);
    }

    @Test
    @DisplayName("armarPedido sin stock suficiente debe lanzar excepción y cambiar estado a Cancelado")
    void armarPedido_stockInsuficiente() {
        Pedido pedido = PedidoFactory.pedido();
        pedido.agregarVendible(new ItemVendible(15, producto1)); // stock insuficiente

        assertThrows(IllegalArgumentException.class, () -> catalogo.armarPedido(pedido));

        // El pedido debe estar en Cancelado
        assertTrue(pedido.getEstado() instanceof Cancelado);
    }

    @Test
    @DisplayName("armarPedido con SKU inexistente debe lanzar RuntimeException")
    void armarPedido_skuInexistente() {
        Producto p3 = new Producto("SKU999", "Monitor", "Samsung", Categoria.ELECTRONICA,
                "Monitor 4K", 0.0, 300.0, 2.0);
        Pedido pedido = PedidoFactory.pedido();
        pedido.agregarVendible(new ItemVendible(1, p3));

        assertThrows(RuntimeException.class, () -> catalogo.armarPedido(pedido));
        // El estado no se cambia porque la excepción ocurre antes de setEstado
        // (en el stream de verificación)
    }
}