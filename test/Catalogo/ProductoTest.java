package Catalogo;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.CatalogoExcepcion;
import exceptions.CatalogoException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

class ProductoTest {

    private Producto producto;
    private final String SKU = "PROD-001";
    private final String NOMBRE = "Laptop Gaming";
    private final String MARCA = "TechCorp";
    private final Categoria CATEGORIA = Categoria.ELECTRONICA;
    private final String DESCRIPCION = "Laptop de �ltima generacion";
    private final Double PRECIO = 1500.0;
    private final Double DESCUENTO = 10.0; // 10%

    @BeforeEach
    void setUp() {
        producto = new Producto(SKU, NOMBRE, MARCA, CATEGORIA, DESCRIPCION, DESCUENTO, PRECIO, 0.2);
    }

    @Test
    @DisplayName("Deberia crear un producto con todos sus atributos correctamente")
    void testCrearProducto() {
        assertNotNull(producto);
        assertEquals(SKU, producto.getSku());
        assertEquals(NOMBRE, producto.getNombre());
        assertEquals(CATEGORIA, producto.getCategoria());
        assertEquals(DESCRIPCION, producto.getDescripcion());
    }

    @Test
    @DisplayName("Deberia calcular correctamente el precio base")
    void testGetPrecioBase() {
        assertEquals(PRECIO, producto.getPrecioBase());
    }

    @Test
    @DisplayName("Deberia calcular correctamente el precio final con descuento")
    void testGetPrecioFinalConDescuento() {
        Double precioEsperado = PRECIO - (PRECIO * DESCUENTO / 100); // 1500 - 150 = 1350
        assertEquals(precioEsperado, producto.getPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("Deberia devolver el precio base cuando el descuento es 0%")
    void testGetPrecioFinalSinDescuento() {
        Producto productoSinDescuento = new Producto(
                "PROD-002", "Mouse", "TechCorp",
                Categoria.ELECTRONICA, "Mouse inalambrico",
                0.0, 50.0,
                0.2);
        assertEquals(50.0, productoSinDescuento.getPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("Deberia validar que los atributos obligatorios no sean nulos o vac�os")
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
    @DisplayName("Deberia validar que el descuento esta entre 0 y 100")
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
    @DisplayName("Deberia validar la categoria no nula")
    void testValidacionCategoria() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Producto(SKU, NOMBRE, MARCA, null, DESCRIPCION, DESCUENTO, PRECIO, 0.2);
        });
    }

    @Test
    @DisplayName("Deberia permitir modificar el precio")
    void testSetPrecio() {
        Double nuevoPrecio = 2000.0;
        producto.setPrecio(nuevoPrecio);
        assertEquals(nuevoPrecio, producto.getPrecioBase());
    }

    @Test
    @DisplayName("Deberia validar la existencia de atributos dinamicos")
    void testValidarAtributoDinamico() {
        assertFalse(producto.validarAtributoDinamico("color"));

        producto.agregarAtributo("color", "negro");
        assertTrue(producto.validarAtributoDinamico("color"));

    }

    @Test
    @DisplayName("Deberia agregar atributos dinamicos de diferentes tipos")
    void testAgregarAtributosDinamicos() {
        producto.agregarAtributo("libras", 2.5);        // Double
        producto.agregarAtributo("garantia", 12);     // Integer
        producto.agregarAtributo("esNuevo", true);    // Boolean

        assertEquals(2.5, producto.getAtributoNombre("libras"));
        assertTrue(producto.validarAtributoDinamico("libras"));
        assertTrue(producto.validarAtributoDinamico("garantia"));
        assertTrue(producto.validarAtributoDinamico("esNuevo"));
    }

    @Test
    void testConstructorAsignaValoresCorrectamente() {
        // Crear un Atributo con valores concretos
        Atributo<String> atributo = new Atributo<>("color", "rojo");

        // Verificar que el constructor asign� correctamente los valores
        assertEquals("color", atributo.getNombre());
        assertEquals("rojo", atributo.getValor());

    }

    @Nested
    class PesoYConstructores {

        @Test
        @DisplayName("Debería obtener el peso correctamente")
        void testGetPeso() {
            assertEquals(0.2, producto.getPeso());
        }

        @Test
        @DisplayName("Debería crear producto con el constructor que incluye cantidad")
        void testConstructorConCantidad() {
            Producto p = new Producto("SKU-CANT", "Producto con cantidad", "Marca",
                    Categoria.ELECTRONICA, "Descripción", 5.0, 100.0, 0.5);
            // No podemos verificar cantidad directamente porque no hay getter,
            // pero podemos verificar que el precio final se calcula correctamente
            // (la cantidad no afecta el precio final del producto individual)
            assertEquals(100.0, p.getPrecioBase());
            assertEquals(0.5, p.getPeso());
            // El descuento se aplica correctamente
            assertEquals(95.0, p.getPrecioFinal(), 0.001);
        }

        @Test
        @DisplayName("Debería calcular precio final con descuento del 100%")
        void testGetPrecioFinalConDescuentoTotal() {
            Producto p = new Producto("SKU-DESC", "Producto gratis", "Marca",
                    Categoria.ELECTRONICA, "Desc", 100.0, 100.0, 0.2);
            assertEquals(0.0, p.getPrecioFinal(), 0.001);
        }

        @Test
        @DisplayName("Debería retornar null para atributo dinámico inexistente")
        void testGetAtributoNombreInexistente() {
            assertNull(producto.getAtributoNombre("inexistente"));
        }

        @Test
        @DisplayName("Debería validar la existencia de atributos dinámicos")
        void testValidarAtributoDinamico() {
            assertFalse(producto.validarAtributoDinamico("color"));

            producto.agregarAtributo("color", "negro");
            assertTrue(producto.validarAtributoDinamico("color"));
        }

        @Test
        @DisplayName("Debería agregar atributos dinámicos de diferentes tipos")
        void testAgregarAtributosDinamicos() {
            producto.agregarAtributo("libras", 2.5);
            producto.agregarAtributo("garantia", 12);
            producto.agregarAtributo("esNuevo", true);

            assertEquals(2.5, producto.getAtributoNombre("libras"));
            assertTrue(producto.validarAtributoDinamico("libras"));
            assertTrue(producto.validarAtributoDinamico("garantia"));
            assertTrue(producto.validarAtributoDinamico("esNuevo"));
        }

        @Test
        @DisplayName("Agregar atributo con nombre existente debe lanzar CatalogoExcepcion")
        void agregarAtributo_conNombreExistente_debeLanzarExcepcion() {
            producto.agregarAtributo("color", "negro");
            assertThrows(CatalogoException.class,
                    () -> producto.agregarAtributo("color", "rojo"));
        }

        @Test
        @DisplayName("Agregar atributo con nombre null debe lanzar CatalogoExcepcion")
        void agregarAtributo_conNombreNull_debeLanzarExcepcion() {
            assertThrows(CatalogoException.class,
                    () -> producto.agregarAtributo(null, "valor"));
        }

        @Test
        @DisplayName("Agregar atributo con nombre vacío debe lanzar CatalogoExcepcion")
        void agregarAtributo_conNombreVacio_debeLanzarExcepcion() {
            assertThrows(CatalogoException.class,
                    () -> producto.agregarAtributo("", "valor"));
        }

        @Test
        @DisplayName("Agregar atributo con nombre solo espacios debe lanzar CatalogoExcepcion")
        void agregarAtributo_conNombreSoloEspacios_debeLanzarExcepcion() {
            assertThrows(CatalogoException.class,
                    () -> producto.agregarAtributo("   ", "valor"));
        }

        @Test
        @DisplayName("Agregar atributo con valor null debe lanzar CatalogoExcepcion")
        void agregarAtributo_conValorNull_debeLanzarExcepcion() {
            assertThrows(CatalogoException.class,
                    () -> producto.agregarAtributo("peso", null));
        }

        @Test
        @DisplayName("Agregar atributo con nombre y valor válidos debe agregar correctamente")
        void agregarAtributo_conDatosValidos_debeAgregar() {
            assertDoesNotThrow(() -> producto.agregarAtributo("alto", 2.5));
            assertTrue(producto.validarAtributoDinamico("alto"));
            assertEquals(2.5, producto.getAtributoNombre("alto"));
        }

        @Test
        @DisplayName("Agregar atributo con nombre 'sku' debe lanzar excepción (reservado)")
        void agregarAtributo_conNombreSku_debeLanzarExcepcion() {
            assertThrows(CatalogoException.class,
                    () -> producto.agregarAtributo("sku", "valor"));
        }

        @Test
        @DisplayName("Agregar atributo con nombre 'nombre' debe lanzar excepción (reservado)")
        void agregarAtributo_conNombreNombre_debeLanzarExcepcion() {
            assertThrows(CatalogoException.class,
                    () -> producto.agregarAtributo("nombre", "valor"));
        }

        @Test
        @DisplayName("Agregar atributo con nombre 'marca' debe lanzar excepción (reservado)")
        void agregarAtributo_conNombreMarca_debeLanzarExcepcion() {
            assertThrows(CatalogoException.class,
                    () -> producto.agregarAtributo("marca", "valor"));
        }

        @Test
        @DisplayName("Agregar atributo con nombre 'categoria' debe lanzar excepción (reservado)")
        void agregarAtributo_conNombreCategoria_debeLanzarExcepcion() {
            assertThrows(CatalogoException.class,
                    () -> producto.agregarAtributo("categoria", "valor"));
        }

        @Test
        @DisplayName("Agregar atributo con nombre 'descripcion' debe lanzar excepción (reservado)")
        void agregarAtributo_conNombreDescripcion_debeLanzarExcepcion() {
            assertThrows(CatalogoException.class,
                    () -> producto.agregarAtributo("descripcion", "valor"));
        }

        @Test
        @DisplayName("Agregar atributo con nombre 'descuento' debe lanzar excepción (reservado)")
        void agregarAtributo_conNombreDescuento_debeLanzarExcepcion() {
            assertThrows(CatalogoException.class,
                    () -> producto.agregarAtributo("descuento", 10.0));
        }

        @Test
        @DisplayName("Agregar atributo con nombre 'precio' debe lanzar excepción (reservado)")
        void agregarAtributo_conNombrePrecio_debeLanzarExcepcion() {
            assertThrows(CatalogoException.class,
                    () -> producto.agregarAtributo("precio", 100.0));
        }

        @Test
        @DisplayName("Agregar atributo con nombre 'peso' debe lanzar excepción (reservado)")
        void agregarAtributo_conNombrePeso_debeLanzarExcepcion() {
            assertThrows(CatalogoException.class,
                    () -> producto.agregarAtributo("peso", 2.5));
        }

        @Test
        @DisplayName("Agregar atributo con nombre en mayúsculas como 'SKU' debe lanzar excepción (case insensitive)")
        void agregarAtributo_conNombreMayusculasReservado_debeLanzarExcepcion() {
            assertThrows(CatalogoException.class,
                    () -> producto.agregarAtributo("SKU", "valor"));
            assertThrows(CatalogoException.class,
                    () -> producto.agregarAtributo("PESO", 5.0));
        }

    }
}