package Sistema;



import Catalogo.*;
import CriterioBusqueda.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.And;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class TestSistema {
    
	private Sistema sistema;
    private Catalogo catalogo;
    
    @BeforeEach
    void setUp() {
        // Crear sistema con catálogo inicializado
        catalogo = new Catalogo();
        
        // ===== PRODUCTOS DE DEPORTES =====
        Producto pelotaFutbol = new Producto(
            "DEP-001", "Pelota de Fútbol", "Adidas",
            Categoria.DEPORTES, "Pelota oficial tamaño 5",
            10.0, 45.0
        );
        pelotaFutbol.agregarAtributo("tamaño", "5");
        
        Producto botines = new Producto(
            "DEP-002", "Botines de Fútbol", "Nike",
            Categoria.DEPORTES, "Botines para césped natural",
            15.0, 120.0
        );
        botines.agregarAtributo("talle", "42");
        
        Producto raquetaTenis = new Producto(
            "DEP-003", "Raqueta de Tenis Tec", "Wilson",
            Categoria.DEPORTES, "Raqueta profesional",
            5.0, 200.0
        );
        
        // ===== PRODUCTOS DE ELECTRÓNICA =====
               
        Producto parlantes = new Producto(
            "ELE-002", "Parlantes Bluetooth", "JBL",
            Categoria.ELECTRONICA, "Parlante portátil",
            10.0, 150.0
        );
        
        Producto monitor = new Producto(
            "ELE-003", "Monitor 24 pulgadas", "LG",
            Categoria.ELECTRONICA, "Monitor Full HD",
            8.0, 300.0
        );
        
        Producto teclado = new Producto(
            "ELE-004", "Teclado Mecánico", "Logitech",
            Categoria.ELECTRONICA, "Teclado mecánico",
            5.0, 80.0
        );
        
        Producto mouse = new Producto(
            "ELE-005", "Mouse Inalámbrico", "Logitech",
            Categoria.ELECTRONICA, "Mouse ergonómico",
            5.0, 50.0
        );
        
        // ===== PRODUCTOS DE HOGAR =====
        Producto lampara = new Producto(
            "HOG-001", "Lámpara LED", "Philips",
            Categoria.HOGAR, "Lámpara de lectura",
            10.0, 60.0
        );
        
        // ===== PAQUETES =====
        Paquete kitFutbol = new Paquete(
            "PAQ-001", "Kit Fútbol Completo", "Adidas",
            Categoria.DEPORTES, "Pelota + Botines",
            20.0
        );
        kitFutbol.agregarVendible(new ItemVendible(1, pelotaFutbol));
        kitFutbol.agregarVendible(new ItemVendible(1, botines));
        
        Paquete kitGamer = new Paquete(
            "PAQ-002", "Kit Gamer Pro", "Logitech",
            Categoria.ELECTRONICA, "Monitor + Teclado + Mouse",
            15.0
        );
        kitGamer.agregarVendible(new ItemVendible(1, monitor));
        kitGamer.agregarVendible(new ItemVendible(1, teclado));
        kitGamer.agregarVendible(new ItemVendible(1, mouse));
        
        // ===== AGREGAR AL CATÁLOGO CON STOCK =====
        catalogo.agregarVendible(new StockVendible (10,pelotaFutbol));
        catalogo.agregarVendible(new StockVendible (5, botines));
        catalogo.agregarVendible(new StockVendible (8, raquetaTenis));

        catalogo.agregarVendible(new StockVendible (10,parlantes));
        catalogo.agregarVendible(new StockVendible (12,monitor));
        catalogo.agregarVendible(new StockVendible (20, teclado));
        catalogo.agregarVendible(new StockVendible (20, mouse));
        catalogo.agregarVendible(new StockVendible (8, lampara));
        
        catalogo.agregarVendible(new StockVendible (3, kitFutbol));
        catalogo.agregarVendible(new StockVendible (5, kitGamer));
        
        // Inyectar el catálogo en el sistema (asumiendo que Sistema tiene setter)
        // Si Sistema tiene constructor con Catalogo, usar ese

        sistema = new Sistema(catalogo);
    }
    
    // ============================================================
    // TESTS DE CRITERIOS SIMPLES
    // ============================================================
    
    @Test
    @DisplayName("Debería filtrar por nombre exacto")
    void testFiltrarPorNombreExacto() {
        Criterio criterio = new CriterioNombre("Pelota");
        List<StockVendible> resultados = sistema.filtrarCon(criterio);
        
        assertEquals(1, resultados.size());
        assertEquals("Pelota de Fútbol", resultados.get(0).getNombre());
    }
    
    @Test
    @DisplayName("Debería filtrar por nombre parcial (case insensitive)")
    void testFiltrarPorNombreParcial() {
        Criterio criterio = new CriterioNombre("tec");
        List<StockVendible> resultados = sistema.filtrarCon(criterio);
        
        assertEquals(2, resultados.size());
        assertTrue(resultados.stream().anyMatch(sv -> sv.getNombre().equals("Teclado Mecánico")));
        assertTrue(resultados.stream().anyMatch(sv -> sv.getNombre().equals("Raqueta de Tenis Tec")));
    }
    
        
    @Test
    @DisplayName("Debería filtrar por precio máximo")
    void testFiltrarPorPrecioMaximo() {
        Criterio criterio = new CriterioPrecio(100.0);
        List<StockVendible> resultados = sistema.filtrarCon(criterio);
        
        // Productos con precio <= 100: pelota(45), teclado(80), mouse(50), lampara(60)
        assertEquals(4, resultados.size());
        assertTrue(resultados.stream().allMatch(sv -> 
            sv.getVendible().getPrecioBase() <= 100.0
        ));
    }
    
    // ============================================================
    // TESTS DE CRITERIOS COMPUESTOS (AND, OR, NOT)
    // ============================================================
    
    @Test
    @DisplayName("Debería combinar criterios con AND")
    void testFiltrarConAnd() {
        Criterio and = new CriterioAND();
        
        and.agregarCriterio(new CriterioCategoria(Categoria.ELECTRONICA));
        and.agregarCriterio(new CriterioPrecio(100.0));
        
        List<StockVendible> resultados = sistema.filtrarCon(and);
        
        // Electrónicos con precio <= 100: teclado(80), mouse(50)
        assertEquals(2, resultados.size());
        assertTrue(resultados.stream().allMatch(sv -> 
            sv.getVendible().getCategoria() == Categoria.ELECTRONICA &&
            sv.getVendible().getPrecioBase() <= 100.0
        ));
    }
    
    @Test
    @DisplayName("Debería combinar criterios con OR")
    void testFiltrarConOr() {
        Criterio or = new CriterioOR();
        
        or.agregarCriterio(new CriterioNombre("mouse"));
        or.agregarCriterio(new CriterioNombre("teclado"));
        
        List<StockVendible> resultados = sistema.filtrarCon(or);
        
        // Debería encontrar Mouse Inalámbrico y Teclado Mecánico
        assertEquals(2, resultados.size());
    }
    
    @Test
    @DisplayName("Debería combinar criterios con NOT")
    void testFiltrarConNot() {
        Criterio not = new CriterioNOT(new CriterioCategoria(Categoria.JUGUETES));
                
        List<StockVendible> resultados = sistema.filtrarCon(not);
        
        // Todos los productos que NO son juguetes
        assertEquals(10, resultados.size());

    }
    
    @Test
    @DisplayName("Debería anidar criterios complejos")
    void testFiltrarConAnidamiento() {
        // (Electrónica AND precio <= 100) OR (Nombre contiene "Pelota")
        Criterio complejo = new CriterioOR();
        
        Criterio and = new CriterioAND();
        and.agregarCriterio(new CriterioCategoria(Categoria.ELECTRONICA));
        and.agregarCriterio(new CriterioPrecio(100.0));
                
        complejo.agregarCriterio(new CriterioNombre("Pelota"));
        complejo.agregarCriterio(and);
        
        
        List<StockVendible> resultados = sistema.filtrarCon(complejo);
        
        // Electrónicos baratos (2) + Pelota (1) = 3
        assertEquals(3, resultados.size());
    }

           
    @Test
    @DisplayName("Debería filtrar por nombre y precio combinado")
    void testFiltrarPorNombreYPrecio() {
        Criterio criterio = new CriterioAND();
        
        criterio.agregarCriterio(new CriterioNombre("o"));  // Productos con 'o' en el nombre)
        criterio.agregarCriterio(new CriterioPrecio(150.0));
        
        List<StockVendible> resultados = sistema.filtrarCon(criterio);
        
        // Productos con 'o' y precio <= 150:
        // Pelota(45), Botines(120), Parlantes(150), Mouse(50), Monopoly(35)
        assertEquals(5, resultados.size());
    }
    
    
    @Test
    @DisplayName("Debería filtrar por nombre con NOT")
    void testFiltrarPorNombreConNot() {
        // NOT (nombre contiene "Mecánico")
        Criterio criterio = new CriterioNOT(new CriterioNombre("Mecánico")
        );
        
        List<StockVendible> resultados = sistema.filtrarCon(criterio);
        
        // Todos los productos excepto "Teclado Mecánico"
        assertEquals(9, resultados.size());
        
    }
    
    // ============================================================
    // TEST DE BÚSQUEDA POR CATEGORÍA CON PRECIO
    // ============================================================
    
    @Test
    @DisplayName("Debería filtrar electrónicos entre $50 y $300")
    void testFiltrarElectronicosPorRangoPrecio() {
        Criterio criterio = new CriterioAND();
        
        criterio.agregarCriterio(new CriterioCategoria(Categoria.ELECTRONICA));
        criterio.agregarCriterio( new CriterioPrecio(300.0));
        criterio.agregarCriterio(new CriterioNOT(new CriterioPrecio(50.0)));
        
        List<StockVendible> resultados = sistema.filtrarCon(criterio);
        
        // Electrónicos entre 50 y 300: parlantes(150), monitor(300), teclado(80)
        assertEquals(3, resultados.size());
    }
    
    @Test
    @DisplayName("Debería filtrar productos de hogar y juguetes combinados con OR")
    void testFiltrarHogarOJuguetes() {
        Criterio criterio = new CriterioOR();
        
        criterio.agregarCriterio( new CriterioCategoria(Categoria.HOGAR));
        criterio.agregarCriterio(new CriterioCategoria(Categoria.DEPORTES));
        
        List<StockVendible> resultados = sistema.filtrarCon(criterio);
        
        // 1 de hogar + 2 de deporte + paquete kitFutbol + paquete kitHogar= 5
        assertEquals(5, resultados.size());
    }
    

    
    
   
}