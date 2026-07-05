package Catalogo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockVendibleTest {

    private Producto producto;
    private StockVendible stockVendible;

    @BeforeEach
    void setUp() {
        producto = new Producto(
                "SKU-TEST", "Producto Test", "Marca Test", Categoria.ELECTRONICA,
                "Descripción del producto", 0.0, 100.0, 0.5
        );
        stockVendible = new StockVendible(10, producto);
    }

    @Test
    @DisplayName("Debería crear StockVendible con stock y vendible correctos")
    void testConstructor() {
        assertNotNull(stockVendible);
        assertEquals(10, stockVendible.getStock());
        assertEquals(producto, stockVendible.getVendible());
        assertEquals("Producto Test", stockVendible.getNombre());
        assertEquals("SKU-TEST", stockVendible.getSku());
    }

    @Test
    @DisplayName("Debería obtener el stock correctamente")
    void testGetStock() {
        assertEquals(10, stockVendible.getStock());
    }

    @Test
    @DisplayName("Debería modificar el stock correctamente")
    void testSetStock() {
        stockVendible.setStock(25);
        assertEquals(25, stockVendible.getStock());

        stockVendible.setStock(0);
        assertEquals(0, stockVendible.getStock());
    }

    @Test
    @DisplayName("Debería obtener el vendible correctamente")
    void testGetVendible() {
        Vendible vendible = stockVendible.getVendible();
        assertNotNull(vendible);
        assertEquals(producto, vendible);
        assertEquals("Producto Test", vendible.getNombre());
    }

    @Test
    @DisplayName("Debería obtener el nombre del vendible")
    void testGetNombre() {
        assertEquals("Producto Test", stockVendible.getNombre());
    }

    @Test
    @DisplayName("Debería obtener el SKU del vendible")
    void testGetSku() {
        assertEquals("SKU-TEST", stockVendible.getSku());
    }

    @Test
    @DisplayName("Debería funcionar con stock 0")
    void testStockCero() {
        StockVendible sinStock = new StockVendible(0, producto);
        assertEquals(0, sinStock.getStock());
    }

    @Test
    @DisplayName("Debería permitir stock negativo (aunque no sea deseable)")
    void testStockNegativo() {
        stockVendible.setStock(-5);
        assertEquals(-5, stockVendible.getStock());
    }
}