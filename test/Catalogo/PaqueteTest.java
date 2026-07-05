package Catalogo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaqueteTest {

    private Paquete paquete;
    private final String SKU = "PAQ-001";
    private final String NOMBRE = "Kit Gaming";
    private final String MARCA = "TechCorp";
    private final Categoria CATEGORIA = Categoria.ELECTRONICA;
    private final String DESCRIPCION = "Kit completo para gaming";
    private final Double DESCUENTO = 15.0; // 15%

    @BeforeEach
    void setUp() {
        paquete = new Paquete(SKU, NOMBRE, MARCA, CATEGORIA, DESCRIPCION, DESCUENTO);
    }

    @Test
    @DisplayName("Debería crear un paquete vacío correctamente")
    void testCrearPaqueteVacio() {
        assertNotNull(paquete);
        assertEquals(NOMBRE, paquete.getNombre());
        assertEquals(CATEGORIA, paquete.getCategoria());
        assertEquals(DESCRIPCION, paquete.getDescripcion());
        assertEquals(0.0, paquete.getPrecioBase(), 0.001);
    }

    @Test
    @DisplayName("Debería calcular correctamente el precio base de un paquete con items")
    void testGetPrecioBaseConItems() {
        // Crear productos con precios finales específicos
        Producto p1 = new Producto("P1", "Prod 1", "Marca", Categoria.ELECTRONICA,
                "Desc1", 0.0, 500.0, 0.5); // precio final = 500
        Producto p2 = new Producto("P2", "Prod 2", "Marca", Categoria.ELECTRONICA,
                "Desc2", 0.0, 600.0, 0.3); // precio final = 600
        Producto p3 = new Producto("P3", "Prod 3", "Marca", Categoria.ELECTRONICA,
                "Desc3", 0.0, 400.0, 0.2); // precio final = 400

        paquete.agregarVendible(new ItemVendible(1, p1));
        paquete.agregarVendible(new ItemVendible(1, p2));
        paquete.agregarVendible(new ItemVendible(1, p3));

        assertEquals(1500.0, paquete.getPrecioBase(), 0.001);
    }

    @Test
    @DisplayName("Debería calcular correctamente el precio final de un paquete con descuento")
    void testGetPrecioFinalConDescuento() {
        Producto p1 = new Producto("P1", "Prod 1", "Marca", Categoria.ELECTRONICA,
                "Desc1", 0.0, 500.0, 0.5);
        Producto p2 = new Producto("P2", "Prod 2", "Marca", Categoria.ELECTRONICA,
                "Desc2", 0.0, 600.0, 0.3);
        Producto p3 = new Producto("P3", "Prod 3", "Marca", Categoria.ELECTRONICA,
                "Desc3", 0.0, 400.0, 0.2);

        paquete.agregarVendible(new ItemVendible(1, p1));
        paquete.agregarVendible(new ItemVendible(1, p2));
        paquete.agregarVendible(new ItemVendible(1, p3));

        // Precio base: 1500, descuento: 15% → 1275
        assertEquals(1275.0, paquete.getPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("Debería calcular correctamente el precio final de un paquete sin descuento")
    void testGetPrecioFinalSinDescuento() {
        Paquete paqueteSinDescuento = new Paquete(
                "PAQ-002", "Kit Básico", "Genérica",
                Categoria.HOGAR, "Kit básico",
                0.0
        );

        Producto p1 = new Producto("P1", "Prod 1", "Marca", Categoria.HOGAR,
                "Desc1", 0.0, 100.0, 0.5);
        Producto p2 = new Producto("P2", "Prod 2", "Marca", Categoria.HOGAR,
                "Desc2", 0.0, 50.0, 0.3);

        paqueteSinDescuento.agregarVendible(new ItemVendible(1, p1));
        paqueteSinDescuento.agregarVendible(new ItemVendible(1, p2));

        assertEquals(150.0, paqueteSinDescuento.getPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("Debería calcular correctamente el precio de un paquete anidado (Composite)")
    void testGetPrecioBaseConPaqueteAnidado() {
        Producto p1 = new Producto("P1", "Prod 1", "Marca", Categoria.ELECTRONICA,
                "Desc1", 0.0, 500.0, 0.5);
        Producto p2 = new Producto("P2", "Prod 2", "Marca", Categoria.ELECTRONICA,
                "Desc2", 0.0, 300.0, 0.3);

        // Subpaquete con precio final 800 (suma de sus items)
        Paquete subPaquete = new Paquete("SUB", "Subpaquete", "Marca",
                Categoria.ELECTRONICA, "Sub", 0.0);
        subPaquete.agregarVendible(new ItemVendible(1, p1));
        subPaquete.agregarVendible(new ItemVendible(1, p2)); // 500 + 300 = 800

        paquete.agregarVendible(new ItemVendible(1, p1));
        paquete.agregarVendible(new ItemVendible(1, p2));
        paquete.agregarVendible(new ItemVendible(1, subPaquete)); // 500 + 300 + 800 = 1600

        assertEquals(1600.0, paquete.getPrecioBase(), 0.001);
    }

    @Test
    @DisplayName("Debería calcular el precio final de un paquete anidado aplicando descuentos")
    void testGetPrecioFinalConPaqueteAnidado() {
        Producto p1 = new Producto("P1", "Prod 1", "Marca", Categoria.ELECTRONICA,
                "Desc1", 0.0, 500.0, 0.5);
        Producto p2 = new Producto("P2", "Prod 2", "Marca", Categoria.ELECTRONICA,
                "Desc2", 0.0, 300.0, 0.3);

        Paquete subPaquete = new Paquete("SUB", "Subpaquete", "Marca",
                Categoria.ELECTRONICA, "Sub", 0.0);
        subPaquete.agregarVendible(new ItemVendible(1, p1));
        subPaquete.agregarVendible(new ItemVendible(1, p2)); // 800

        paquete.agregarVendible(new ItemVendible(1, p1));
        paquete.agregarVendible(new ItemVendible(1, p2));
        paquete.agregarVendible(new ItemVendible(1, subPaquete));

        // Precio base: 1600, descuento: 15% → 1360
        assertEquals(1360.0, paquete.getPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("Debería agregar y eliminar vendibles correctamente")
    void testAgregarYEliminarVendibles() {
        assertEquals(0.0, paquete.getPrecioBase(), 0.001);

        Producto p1 = new Producto("P1", "Prod 1", "Marca", Categoria.ELECTRONICA,
                "Desc1", 0.0, 500.0, 0.5);
        Producto p2 = new Producto("P2", "Prod 2", "Marca", Categoria.ELECTRONICA,
                "Desc2", 0.0, 300.0, 0.3);

        ItemVendible item1 = new ItemVendible(1, p1);
        ItemVendible item2 = new ItemVendible(1, p2);

        paquete.agregarVendible(item1);
        assertEquals(500.0, paquete.getPrecioBase(), 0.001);

        paquete.agregarVendible(item2);
        assertEquals(800.0, paquete.getPrecioBase(), 0.001);

        paquete.eliminarVendible(item1);
        assertEquals(300.0, paquete.getPrecioBase(), 0.001);
    }

    @Test
    @DisplayName("Debería permitir agregar cualquier tipo de ItemVendible (Producto o Paquete)")
    void testAgregarVendiblesDeDiferentesTipos() {
        Producto productoReal = new Producto(
                "PROD-003", "Teclado", "Logitech",
                Categoria.ELECTRONICA, "Teclado mecánico",
                0.0, 100.0, 0.2);
        ItemVendible itemProducto = new ItemVendible(1, productoReal);

        Paquete subPaquete = new Paquete(
                "SUB-001", "SubPaquete", "Marca",
                Categoria.ELECTRONICA, "Subpaquete de prueba",
                0.0
        );
        Producto productoInterno = new Producto(
                "PROD-004", "Mouse", "Logitech",
                Categoria.ELECTRONICA, "Mouse inalámbrico",
                0.0, 50.0, 0.2);
        subPaquete.agregarVendible(new ItemVendible(1, productoInterno));
        ItemVendible itemSubPaquete = new ItemVendible(1, subPaquete);

        paquete.agregarVendible(itemProducto);
        paquete.agregarVendible(itemSubPaquete);

        // Precio total: 100 + 50 = 150
        assertEquals(150.0, paquete.getPrecioBase(), 0.001);
    }

    @Test
    @DisplayName("Debería validar atributos obligatorios al crear un paquete")
    void testValidacionAtributosPaquete() {
        assertThrows(IllegalArgumentException.class, () ->
                new Paquete(null, NOMBRE, MARCA, CATEGORIA, DESCRIPCION, DESCUENTO)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new Paquete(SKU, "", MARCA, CATEGORIA, DESCRIPCION, DESCUENTO)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new Paquete(SKU, NOMBRE, null, CATEGORIA, DESCRIPCION, DESCUENTO)
        );
    }

    // ============================================================
    // PESO Y ELIMINACIÓN
    // ============================================================

    @Nested
    class PesoYEliminacion {

        @Test
        @DisplayName("Debería calcular el peso total sumando pesos de items")
        void testGetPeso() {
            Producto p1 = new Producto("P1", "Prod 1", "Marca", Categoria.ELECTRONICA,
                    "Desc1", 0.0, 100.0, 0.5);
            Producto p2 = new Producto("P2", "Prod 2", "Marca", Categoria.ELECTRONICA,
                    "Desc2", 0.0, 100.0, 0.3);
            Producto p3 = new Producto("P3", "Prod 3", "Marca", Categoria.ELECTRONICA,
                    "Desc3", 0.0, 100.0, 0.7);

            paquete.agregarVendible(new ItemVendible(1, p1));
            paquete.agregarVendible(new ItemVendible(1, p2));
            paquete.agregarVendible(new ItemVendible(1, p3));

            assertEquals(1.5, paquete.getPeso(), 0.001);
        }

        @Test
        @DisplayName("Debería tener peso 0 cuando está vacío")
        void testGetPesoVacio() {
            assertEquals(0.0, paquete.getPeso());
        }

        @Test
        @DisplayName("Eliminar item inexistente no debe lanzar excepción")
        void testEliminarInexistente() {
            Producto p1 = new Producto("P1", "Prod 1", "Marca", Categoria.ELECTRONICA,
                    "Desc1", 0.0, 100.0, 0.5);
            ItemVendible item = new ItemVendible(1, p1);
            assertDoesNotThrow(() -> paquete.eliminarVendible(item));
            assertEquals(0.0, paquete.getPrecioBase());
        }

        @Test
        @DisplayName("Debería calcular precio base con items con descuentos individuales")
        void testGetPrecioBaseConItemsConDescuentos() {
            Producto p1 = new Producto("P1", "Prod 1", "Marca", Categoria.ELECTRONICA,
                    "Desc1", 20.0, 100.0, 0.5); // precio final = 80
            Producto p2 = new Producto("P2", "Prod 2", "Marca", Categoria.ELECTRONICA,
                    "Desc2", 10.0, 50.0, 0.3); // precio final = 45

            paquete.agregarVendible(new ItemVendible(1, p1));
            paquete.agregarVendible(new ItemVendible(1, p2));

            assertEquals(125.0, paquete.getPrecioBase(), 0.001);
        }
    }

    // ============================================================
    // ANIDACIÓN PROFUNDA
    // ============================================================

    @Nested
    class AnidacionProfunda {

        @Test
        @DisplayName("Debería calcular precio de paquete anidado de 3 niveles")
        void testGetPrecioBaseAnidadoProfundo() {
            Producto p1 = new Producto("P1", "Producto 1", "Marca", Categoria.ELECTRONICA,
                    "Desc1", 0.0, 100.0, 0.5);
            Producto p2 = new Producto("P2", "Producto 2", "Marca", Categoria.ELECTRONICA,
                    "Desc2", 0.0, 50.0, 0.3);

            Paquete subSubPaquete = new Paquete("SUB-SUB", "Sub-sub paquete", "Marca",
                    Categoria.ELECTRONICA, "Sub-sub", 0.0);
            subSubPaquete.agregarVendible(new ItemVendible(1, p1));

            Paquete subPaquete = new Paquete("SUB", "Sub paquete", "Marca",
                    Categoria.ELECTRONICA, "Sub", 10.0);
            subPaquete.agregarVendible(new ItemVendible(1, p2));
            subPaquete.agregarVendible(new ItemVendible(1, subSubPaquete));

            Paquete principal = new Paquete("PRINC", "Principal", "Marca",
                    Categoria.ELECTRONICA, "Principal", 5.0);
            principal.agregarVendible(new ItemVendible(1, subPaquete));

            // Cálculo esperado:
            // subSubPaquete: base=100, desc=0% → final=100
            // subPaquete: items = p2(50) + subSub(100) = 150, desc=10% → final=135
            // principal: item = subPaquete(135), desc=5% → final=128.25
            assertEquals(128.25, principal.getPrecioFinal(), 0.001);
        }
    }
}