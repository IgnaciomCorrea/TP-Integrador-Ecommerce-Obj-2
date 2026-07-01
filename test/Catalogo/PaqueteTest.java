package Catalogo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaqueteTest {
    
    private Paquete paquete;
    private ItemVendible mockItem1;
    private ItemVendible mockItem2;
    private ItemVendible mockItem3;
    private ItemVendible mockSubPaqueteItem;
    
    private final String SKU = "PAQ-001";
    private final String NOMBRE = "Kit Gaming";
    private final String MARCA = "TechCorp";
    private final Categoria CATEGORIA = Categoria.ELECTRONICA;
    private final String DESCRIPCION = "Kit completo para gaming";
    private final Double DESCUENTO = 15.0; // 15%


    @BeforeEach
    void setUp() {
        paquete = new Paquete(SKU, NOMBRE, MARCA, CATEGORIA, DESCRIPCION, DESCUENTO);
        
        // Crear mocks de ItemVendible (NO de Producto directamente)
        mockItem1 = mock(ItemVendible.class);
        mockItem2 = mock(ItemVendible.class);
        mockItem3 = mock(ItemVendible.class);
        mockSubPaqueteItem = mock(ItemVendible.class);
    }
    
    @Test
    @DisplayName("Deber�a crear un paquete vac�o correctamente")
    void testCrearPaqueteVacio() {
        assertNotNull(paquete);
        // assertEquals(SKU, paquete.getSKU());
        assertEquals(NOMBRE, paquete.getNombre());
        
        assertEquals(CATEGORIA, paquete.getCategoria());
        assertEquals(DESCRIPCION, paquete.getDescripcion());
        assertEquals(0.0, paquete.getPrecioBase(), 0.001);
    }
    
    @Test
    @DisplayName("Deber�a calcular correctamente el precio base de un paquete con items")
    void testGetPrecioBaseConItems() {
        // Configurar mocks de ItemVendible (ya incluyen la cantidad en su precio)
        when(mockItem1.getPrecioFinal()).thenReturn(500.0);  // 1 x 500
        when(mockItem2.getPrecioFinal()).thenReturn(600.0);  // 2 x 300
        when(mockItem3.getPrecioFinal()).thenReturn(400.0);  // 1 x 400
        
        // Agregar items al paquete
        paquete.agregarVendible(mockItem1);
        paquete.agregarVendible(mockItem2);
        paquete.agregarVendible(mockItem3);
        
        // Precio total esperado: 500 + 600 + 400 = 1500
        assertEquals(1500.0, paquete.getPrecioBase(), 0.001);
        
        // Verificar que se llamaron los m�todos de los mocks
        verify(mockItem1, times(1)).getPrecioFinal();
        verify(mockItem2, times(1)).getPrecioFinal();
        verify(mockItem3, times(1)).getPrecioFinal();
    }
    
    @Test
    @DisplayName("Deber�a calcular correctamente el precio final de un paquete con descuento")
    void testGetPrecioFinalConDescuento() {
        when(mockItem1.getPrecioFinal()).thenReturn(500.0);
        when(mockItem2.getPrecioFinal()).thenReturn(600.0);
        when(mockItem3.getPrecioFinal()).thenReturn(400.0);
        
        paquete.agregarVendible(mockItem1);
        paquete.agregarVendible(mockItem2);
        paquete.agregarVendible(mockItem3);
        
        // Precio base: 1500, descuento: 15%
        // Precio final: 1500 - (1500 * 0.15) = 1275
        assertEquals(1275.0, paquete.getPrecioFinal(), 0.001);
    }
    
    @Test
    @DisplayName("Deber�a calcular correctamente el precio final de un paquete sin descuento")
    void testGetPrecioFinalSinDescuento() {
        Paquete paqueteSinDescuento = new Paquete(
            "PAQ-002", "Kit B�sico", "Gen�rica", 
            Categoria.HOGAR, "Kit b�sico", 
            0.0
        );
        
        when(mockItem1.getPrecioFinal()).thenReturn(100.0);
        when(mockItem2.getPrecioFinal()).thenReturn(50.0);
        
        paqueteSinDescuento.agregarVendible(mockItem1);
        paqueteSinDescuento.agregarVendible(mockItem2);
        
        assertEquals(150.0, paqueteSinDescuento.getPrecioFinal(), 0.001);
    }
    
    @Test
    @DisplayName("Deber�a calcular correctamente el precio de un paquete anidado (Composite)")
    void testGetPrecioBaseConPaqueteAnidado() {
        // Configurar mocks
        when(mockItem1.getPrecioFinal()).thenReturn(500.0);
        when(mockItem2.getPrecioFinal()).thenReturn(300.0);
        when(mockSubPaqueteItem.getPrecioFinal()).thenReturn(800.0);
        
        // Agregar items al paquete principal
        paquete.agregarVendible(mockItem1);
        paquete.agregarVendible(mockItem2);
        paquete.agregarVendible(mockSubPaqueteItem);
        
        // Precio total esperado: 500 + 300 + 800 = 1600
        assertEquals(1600.0, paquete.getPrecioBase(), 0.001);
        
        verify(mockItem1).getPrecioFinal();
        verify(mockItem2).getPrecioFinal();
        verify(mockSubPaqueteItem).getPrecioFinal();
    }
    
    @Test
    @DisplayName("Deber�a calcular el precio final de un paquete anidado aplicando descuentos")
    void testGetPrecioFinalConPaqueteAnidado() {
        // Configurar mocks
        when(mockItem1.getPrecioFinal()).thenReturn(500.0);
        when(mockItem2.getPrecioFinal()).thenReturn(300.0);
        when(mockSubPaqueteItem.getPrecioFinal()).thenReturn(800.0);
        
        // Agregar al paquete principal
        paquete.agregarVendible(mockItem1);
        paquete.agregarVendible(mockItem2);
        paquete.agregarVendible(mockSubPaqueteItem);
        
        // Precio base: 1600, descuento del paquete: 15%
        // Precio final: 1600 - (1600 * 0.15) = 1360
        assertEquals(1360.0, paquete.getPrecioFinal(), 0.001);
    }
    
    @Test
    @DisplayName("Deber�a agregar y eliminar vendibles correctamente")
    void testAgregarYEliminarVendibles() {
        // Verificar que comienza vac�o
        assertEquals(0.0, paquete.getPrecioBase(), 0.001);
        
        // Agregar un item
        when(mockItem1.getPrecioFinal()).thenReturn(500.0);
        paquete.agregarVendible(mockItem1);
        assertEquals(500.0, paquete.getPrecioBase(), 0.001);
        
        // Agregar otro item
        when(mockItem2.getPrecioFinal()).thenReturn(300.0);
        paquete.agregarVendible(mockItem2);
        assertEquals(800.0, paquete.getPrecioBase(), 0.001);
        
        // Eliminar un item
        paquete.eliminarVendible(mockItem1);
        assertEquals(300.0, paquete.getPrecioBase(), 0.001);
    }
    
    @Test
    @DisplayName("Deber�a permitir agregar cualquier tipo de ItemVendible (Producto o Paquete)")
    void testAgregarVendiblesDeDiferentesTipos() {
        // Crear un producto real
        Producto productoReal = new Producto(
            "PROD-003", "Teclado", "Logitech", 
            Categoria.ELECTRONICA, "Teclado mec�nico", 
            0.0, 100.0,
                0.2);
        ItemVendible itemProducto = new ItemVendible(1, productoReal);
        
        // Crear un sub-paquete
        Paquete subPaquete = new Paquete(
            "SUB-001", "SubPaquete", "Marca", 
            Categoria.ELECTRONICA, "Subpaquete de prueba", 
            0.0
        );
        Producto productoInterno = new Producto(
            "PROD-004", "Mouse", "Logitech", 
            Categoria.ELECTRONICA, "Mouse inal�mbrico", 
            0.0, 50.0,
                0.2);
        subPaquete.agregarVendible(new ItemVendible(1, productoInterno));
        
        ItemVendible itemSubPaquete = new ItemVendible(1, subPaquete);
        
        // Agregar al paquete principal
        paquete.agregarVendible(itemProducto);
        paquete.agregarVendible(itemSubPaquete);
        
        // Precio total: 100 + 50 = 150
        assertEquals(150.0, paquete.getPrecioBase(), 0.001);
    }
    
    @Test
    @DisplayName("Deber�a validar atributos obligatorios al crear un paquete")
    void testValidacionAtributosPaquete() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Paquete(null, NOMBRE, MARCA, CATEGORIA, DESCRIPCION, DESCUENTO);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Paquete(SKU, "", MARCA, CATEGORIA, DESCRIPCION, DESCUENTO);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Paquete(SKU, NOMBRE, null, CATEGORIA, DESCRIPCION, DESCUENTO);
        });
    }

    @Test
    @DisplayName("Debería validar nombre de Vendible en ItemVendible")
        void testNombreVendible(){
            Producto productoReal = new Producto(
                    "PROD-003", "Teclado", "Logitech",
                    Categoria.ELECTRONICA, "Teclado mec�nico",
                    0.0, 100.0,
                    0.2);
            ItemVendible itemProducto = new ItemVendible(1, productoReal);

            assertEquals("Teclado", itemProducto.getNombre());
        }

    @Test
    @DisplayName("Debería funcionar con cantidad 1")
    void testConstructorCantidadUno() {
        Producto productoReal = new Producto(
                "PROD-003", "Teclado", "Logitech",
                Categoria.ELECTRONICA, "Teclado mec�nico",
                0.0, 100.0,
                0.2);
        ItemVendible item = new ItemVendible(1, productoReal);
        assertEquals(100.0, item.getPrecioFinal(), 0.001);
        assertEquals(0.2, item.getPeso(), 0.001);
    }

    @Test
    @DisplayName("Debería lanzar excepción si la cantidad es 0")
    void testConstructorCantidadCero() {
        Producto productoReal = new Producto(
                "PROD-003", "Teclado", "Logitech",
                Categoria.ELECTRONICA, "Teclado mecánico",
                0.0, 100.0, 0.2);

        assertThrows(IllegalArgumentException.class, () -> {
            new ItemVendible(0, productoReal);
        });
    }

    @Nested
    class PesoYEliminacion {

        @Test
        @DisplayName("Debería calcular el peso total sumando pesos de items")
        void testGetPeso() {
            when(mockItem1.getPeso()).thenReturn(0.5);
            when(mockItem2.getPeso()).thenReturn(0.3);
            when(mockSubPaqueteItem.getPeso()).thenReturn(0.7);

            paquete.agregarVendible(mockItem1);
            paquete.agregarVendible(mockItem2);
            paquete.agregarVendible(mockSubPaqueteItem);

            assertEquals(1.5, paquete.getPeso(), 0.001);
            verify(mockItem1).getPeso();
            verify(mockItem2).getPeso();
            verify(mockSubPaqueteItem).getPeso();
        }

        @Test
        @DisplayName("Debería tener peso 0 cuando está vacío")
        void testGetPesoVacio() {
            assertEquals(0.0, paquete.getPeso());
        }

        @Test
        @DisplayName("Eliminar item inexistente no debe lanzar excepción")
        void testEliminarInexistente() {
            assertDoesNotThrow(() -> paquete.eliminarVendible(mockItem1));
            // Verificar que sigue vacío
            assertEquals(0.0, paquete.getPrecioBase());
        }

        @Test
        @DisplayName("Debería calcular precio base con items con descuentos individuales")
        void testGetPrecioBaseConItemsConDescuentos() {
            // Item1 tiene precio final 80 (base 100 con 20% desc)
            // Item2 tiene precio final 45 (base 50 con 10% desc)
            when(mockItem1.getPrecioFinal()).thenReturn(80.0);
            when(mockItem2.getPrecioFinal()).thenReturn(45.0);

            paquete.agregarVendible(mockItem1);
            paquete.agregarVendible(mockItem2);

            assertEquals(125.0, paquete.getPrecioBase(), 0.001);
        }
    }

    @Nested
    class AnidacionProfunda {

        @Test
        @DisplayName("Debería calcular precio de paquete anidado de 3 niveles")
        void testGetPrecioBaseAnidadoProfundo() {
            // Nivel 1: paquete principal
            // Nivel 2: subpaquete
            // Nivel 3: productos

            Producto p1 = new Producto("P1", "Producto 1", "Marca", Categoria.ELECTRONICA,
                    "Desc1", 0.0, 100.0, 0.5);
            Producto p2 = new Producto("P2", "Producto 2", "Marca", Categoria.ELECTRONICA,
                    "Desc2", 0.0, 50.0, 0.3);

            Paquete subSubPaquete = new Paquete("SUB-SUB", "Sub-sub paquete", "Marca",
                    Categoria.ELECTRONICA, "Sub-sub", 0.0);
            subSubPaquete.agregarVendible(new ItemVendible(1, p1));

            Paquete subPaquete = new Paquete("SUB", "Sub paquete", "Marca",
                    Categoria.ELECTRONICA, "Sub", 10.0); // 10% desc
            subPaquete.agregarVendible(new ItemVendible(1, p2));
            subPaquete.agregarVendible(new ItemVendible(1, subSubPaquete));

            Paquete principal = new Paquete("PRINC", "Principal", "Marca",
                    Categoria.ELECTRONICA, "Principal", 5.0); // 5% desc
            principal.agregarVendible(new ItemVendible(1, subPaquete));

            // Cálculo esperado:
            // subSubPaquete: base=100, desc=0% → final=100
            // subPaquete: items = p2(50) + subSub(100) = 150, desc=10% → final=135
            // principal: item = subPaquete(135), desc=5% → final=128.25
            assertEquals(128.25, principal.getPrecioFinal(), 0.001);
        }
    }
}