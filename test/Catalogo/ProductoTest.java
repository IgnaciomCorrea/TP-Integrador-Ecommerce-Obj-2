package Catalogo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

class ProductoTest {
    
    private Producto producto;
    private final String SKU = "PROD-001";
    private final String NOMBRE = "Laptop Gaming";
    private final String MARCA = "TechCorp";
    private final Categoria CATEGORIA = Categoria.ELECTRONICA;
    private final String DESCRIPCION = "Laptop de �ltima generaci�n";
    private final Double PRECIO = 1500.0;
    private final Double DESCUENTO = 10.0; // 10%
    
    @BeforeEach
    void setUp() {
        producto = new Producto(SKU, NOMBRE, MARCA, CATEGORIA, DESCRIPCION, DESCUENTO, PRECIO, 0.2);
    }
    
    @Test
    @DisplayName("Deber�a crear un producto con todos sus atributos correctamente")
    void testCrearProducto() {
        assertNotNull(producto);
        // assertEquals(SKU, producto.getSKU());
        assertEquals(NOMBRE, producto.getNombre());
        assertEquals(CATEGORIA, producto.getCategoria());
        assertEquals(DESCRIPCION, producto.getDescripcion());
    }
    
    @Test
    @DisplayName("Deber�a calcular correctamente el precio base")
    void testGetPrecioBase() {
        assertEquals(PRECIO, producto.getPrecioBase());
    }
    
    @Test
    @DisplayName("Deber�a calcular correctamente el precio final con descuento")
    void testGetPrecioFinalConDescuento() {
        Double precioEsperado = PRECIO - (PRECIO * DESCUENTO / 100); // 1500 - 150 = 1350
        assertEquals(precioEsperado, producto.getPrecioFinal(), 0.001);
    }
    
    @Test
    @DisplayName("Deber�a devolver el precio base cuando el descuento es 0%")
    void testGetPrecioFinalSinDescuento() {
        Producto productoSinDescuento = new Producto(
            "PROD-002", "Mouse", "TechCorp", 
            Categoria.ELECTRONICA, "Mouse inal�mbrico", 
            0.0, 50.0,
                0.2);
        assertEquals(50.0, productoSinDescuento.getPrecioFinal(), 0.001);
    }
    
    @Test
    @DisplayName("Deber�a validar que los atributos obligatorios no sean nulos o vac�os")
    void testValidacionAtributos() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Producto(null, NOMBRE, MARCA, CATEGORIA, DESCRIPCION, DESCUENTO, PRECIO, 0.2);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Producto(SKU, "", MARCA, CATEGORIA, DESCRIPCION, DESCUENTO, PRECIO, 0.2);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Producto(SKU, NOMBRE, null, CATEGORIA, DESCRIPCION, DESCUENTO, PRECIO, 0.2);
        });
    }
    
    @Test
    @DisplayName("Deber�a validar que el descuento est� entre 0 y 100")
    void testValidacionDescuento() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Producto(SKU, NOMBRE, MARCA, CATEGORIA, DESCRIPCION, -10.0, PRECIO, 0.2);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Producto(SKU, NOMBRE, MARCA, CATEGORIA, DESCRIPCION, 150.0, PRECIO, 0.2);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Producto(SKU, NOMBRE, MARCA, CATEGORIA, DESCRIPCION, null, PRECIO, 0.2);
        });
    }
    
    @Test
    @DisplayName("Deber�a validar la categor�a no nula")
    void testValidacionCategoria() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Producto(SKU, NOMBRE, MARCA, null, DESCRIPCION, DESCUENTO, PRECIO, 0.2);
        });
    }
    
    @Test
    @DisplayName("Deber�a permitir modificar el precio")
    void testSetPrecio() {
        Double nuevoPrecio = 2000.0;
        producto.setPrecio(nuevoPrecio);
        assertEquals(nuevoPrecio, producto.getPrecioBase());
    }
    
    @Test
    @DisplayName("Deber�a validar la existencia de atributos din�micos")
    void testValidarAtributoDinamico() {
        assertFalse(producto.validarAtributoDinamico("color"));
        
        producto.agregarAtributo("color", "negro");
        assertTrue(producto.validarAtributoDinamico("color"));
    }
    
    @Test
    @DisplayName("Deber�a agregar atributos din�micos de diferentes tipos")
    void testAgregarAtributosDinamicos() {
        producto.agregarAtributo("peso", 2.5);        // Double
        producto.agregarAtributo("garantia", 12);     // Integer
        producto.agregarAtributo("esNuevo", true);    // Boolean
        
        assertTrue(producto.validarAtributoDinamico("peso"));
        assertTrue(producto.validarAtributoDinamico("garantia"));
        assertTrue(producto.validarAtributoDinamico("esNuevo"));
    }
}
