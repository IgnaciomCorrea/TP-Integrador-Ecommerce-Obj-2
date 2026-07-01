package Catalogo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import reportes.ReportVisitor;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemVendibleTest {

    private Producto productoMock;
    private Paquete paqueteMock;
    private ItemVendible itemProducto;
    private ItemVendible itemPaquete;

    @BeforeEach
    void setUp() {
        productoMock = mock(Producto.class);
        paqueteMock = mock(Paquete.class);

        when(productoMock.getPrecioFinal()).thenReturn(100.0);
        when(productoMock.getPeso()).thenReturn(0.5);
        when(productoMock.getNombre()).thenReturn("Producto Test");
        when(productoMock.getSku()).thenReturn("SKU001");

        when(paqueteMock.getPrecioFinal()).thenReturn(200.0);
        when(paqueteMock.getPeso()).thenReturn(1.2);
        when(paqueteMock.getNombre()).thenReturn("Paquete Test");
        when(paqueteMock.getSku()).thenReturn("SKU002");

        itemProducto = new ItemVendible(3, productoMock);
        itemPaquete = new ItemVendible(2, paqueteMock);
    }

    @Test
    @DisplayName("Constructor con cantidad válida debe crear el item")
    void constructor_valido() {
        assertNotNull(itemProducto);
        assertEquals(3, itemProducto.getCantidad());
        assertEquals(productoMock, itemProducto.vendible); // acceso directo, mejor usar getters
    }

    @Test
    @DisplayName("Constructor con cantidad < 1 debe lanzar IllegalArgumentException")
    void constructor_cantidadInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new ItemVendible(0, productoMock));
        assertThrows(IllegalArgumentException.class, () -> new ItemVendible(-1, productoMock));
    }

    @Test
    @DisplayName("getPrecioFinal debe multiplicar el precio del vendible por la cantidad")
    void getPrecioFinal() {
        assertEquals(300.0, itemProducto.getPrecioFinal(), 0.001);
        assertEquals(400.0, itemPaquete.getPrecioFinal(), 0.001);
        verify(productoMock, times(1)).getPrecioFinal();
        verify(paqueteMock, times(1)).getPrecioFinal();
    }

    @Test
    @DisplayName("getPeso debe multiplicar el peso del vendible por la cantidad")
    void getPeso() {
        assertEquals(1.5, itemProducto.getPeso(), 0.001);
        assertEquals(2.4, itemPaquete.getPeso(), 0.001);
        verify(productoMock, times(1)).getPeso();
        verify(paqueteMock, times(1)).getPeso();
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

    @Test
    @DisplayName("accept debe llamar a visitProducto si el vendible es Producto")
    void accept_producto() {
        ReportVisitor visitor = mock(ReportVisitor.class);
        itemProducto.accept(visitor);
        verify(visitor, times(1)).visitProducto(productoMock, 3, 300.0);
        verify(visitor, never()).visitPaquete(any(), anyInt(), anyDouble());
    }

    @Test
    @DisplayName("accept debe llamar a visitPaquete si el vendible es Paquete")
    void accept_paquete() {
        ReportVisitor visitor = mock(ReportVisitor.class);
        itemPaquete.accept(visitor);
        verify(visitor, times(1)).visitPaquete(paqueteMock, 2, 400.0);
        verify(visitor, never()).visitProducto(any(), anyInt(), anyDouble());
    }
}