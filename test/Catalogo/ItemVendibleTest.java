package Catalogo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reportes.ReportVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemVendibleTest {

    private Producto producto;
    private Paquete paquete;
    private ItemVendible itemProducto;
    private ItemVendible itemPaquete;

    @BeforeEach
    void setUp() {
        producto = new Producto(
                "SKU001", "Producto Test", "Marca", Categoria.ELECTRONICA,
                "Descripción", 0.0, 100.0, 0.5
        );

        paquete = new Paquete(
                "SKU002", "Paquete Test", "Marca", Categoria.ELECTRONICA,
                "Paquete descripción", 0.0
        );
        Producto interno = new Producto(
                "SKU-INT", "Interno", "Marca", Categoria.ELECTRONICA,
                "Interno desc", 0.0, 200.0, 1.2
        );
        paquete.agregarVendible(new ItemVendible(1, interno));

        itemProducto = new ItemVendible(3, producto);
        itemPaquete = new ItemVendible(2, paquete);
    }

    @Test
    @DisplayName("Constructor con cantidad válida debe crear el item")
    void constructor_valido() {
        assertNotNull(itemProducto);
        assertEquals(3, itemProducto.getCantidad());
        assertEquals("SKU001", itemProducto.getSku());
    }

    @Test
    @DisplayName("Constructor con cantidad < 1 debe lanzar IllegalArgumentException")
    void constructor_cantidadInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new ItemVendible(0, producto));
        assertThrows(IllegalArgumentException.class, () -> new ItemVendible(-1, producto));
    }

    @Test
    @DisplayName("getPrecioFinal debe multiplicar el precio del vendible por la cantidad")
    void getPrecioFinal() {
        assertEquals(300.0, itemProducto.getPrecioFinal(), 0.001); // 100 * 3
        assertEquals(400.0, itemPaquete.getPrecioFinal(), 0.001); // 200 * 2
    }

    @Test
    @DisplayName("getPeso debe multiplicar el peso del vendible por la cantidad")
    void getPeso() {
        assertEquals(1.5, itemProducto.getPeso(), 0.001); // 0.5 * 3
        assertEquals(2.4, itemPaquete.getPeso(), 0.001); // 1.2 * 2
    }

    @Test
    @DisplayName("getNombre debe delegar en el vendible")
    void getNombre() {
        assertEquals("Producto Test", itemProducto.getNombre());
        assertEquals("Paquete Test", itemPaquete.getNombre());
    }

    @Test
    @DisplayName("getSku debe delegar en el vendible")
    void getSku() {
        assertEquals("SKU001", itemProducto.getSku());
        assertEquals("SKU002", itemPaquete.getSku());
    }

    @Test
    @DisplayName("getCantidad debe retornar la cantidad")
    void getCantidad() {
        assertEquals(3, itemProducto.getCantidad());
        assertEquals(2, itemPaquete.getCantidad());
    }

}