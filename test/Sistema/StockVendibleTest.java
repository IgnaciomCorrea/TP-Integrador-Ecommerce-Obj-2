package Sistema;

import Catalogo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockVendibleTest {

    private Vendible vendibleMock;
    private StockVendible stockVendible;

    @BeforeEach
    void setUp() {
        vendibleMock = mock(Vendible.class);
        when(vendibleMock.getNombre()).thenReturn("Producto Test");
        when(vendibleMock.getPrecioBase()).thenReturn(100.0);

        stockVendible = new StockVendible(10, vendibleMock);
    }

    @Test
    @DisplayName("Debería crear StockVendible con stock y vendible correctos")
    void testConstructor() {
        assertNotNull(stockVendible);
        assertEquals(10, stockVendible.getStock());
        assertEquals(vendibleMock, stockVendible.getVendible());
        assertEquals("Producto Test", stockVendible.getNombre());
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
        assertEquals(vendibleMock, vendible);
        assertEquals("Producto Test", vendible.getNombre());
    }

    @Test
    @DisplayName("Debería obtener el nombre del vendible")
    void testGetNombre() {
        assertEquals("Producto Test", stockVendible.getNombre());

        // Verificar que usa el nombre del vendible
        verify(vendibleMock, times(1)).getNombre();
    }

    @Test
    @DisplayName("Debería funcionar con stock 0")
    void testStockCero() {
        StockVendible sinStock = new StockVendible(0, vendibleMock);
        assertEquals(0, sinStock.getStock());
    }
}