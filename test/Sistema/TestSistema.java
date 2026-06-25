package Sistema;



import Catalogo.*;
import CriterioBusqueda.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class TestSistema {
    
	private Sistema sistema;
    private Catalogo catalogo;
    
    @BeforeEach
    void setUp() {
        // Crear sistema con cat�logo inicializado
        catalogo = new Catalogo();
        
        // ===== PRODUCTOS DE DEPORTES =====
        Producto pelotaFutbol = new Producto(
            "DEP-001", "Pelota de F�tbol", "Adidas",
            Categoria.DEPORTES, "Pelota oficial tama�o 5",
            10.0, 45.0,
                0.2);
        pelotaFutbol.agregarAtributo("tama�o", "5");
        
        Producto botines = new Producto(
            "DEP-002", "Botines de F�tbol", "Nike",
            Categoria.DEPORTES, "Botines para c�sped natural",
            15.0, 120.0,
                0.2);
        botines.agregarAtributo("talle", "42");
        
        Producto raquetaTenis = new Producto(
            "DEP-003", "Raqueta de Tenis Tec", "Wilson",
            Categoria.DEPORTES, "Raqueta profesional",
            5.0, 200.0,
                0.2);
        
        // ===== PRODUCTOS DE ELECTR�NICA =====
               
        Producto parlantes = new Producto(
            "ELE-002", "Parlantes Bluetooth", "JBL",
            Categoria.ELECTRONICA, "Parlante port�til",
            10.0, 150.0,
                0.2);
        
        Producto monitor = new Producto(
            "ELE-003", "Monitor 24 pulgadas", "LG",
            Categoria.ELECTRONICA, "Monitor Full HD",
            8.0, 300.0,
                0.2);
        
        Producto teclado = new Producto(
            "ELE-004", "Teclado Mec�nico", "Logitech",
            Categoria.ELECTRONICA, "Teclado mec�nico",
            5.0, 80.0,
                0.2);
        
        Producto mouse = new Producto(
            "ELE-005", "Mouse Inal�mbrico", "Logitech",
            Categoria.ELECTRONICA, "Mouse ergon�mico",
            5.0, 50.0,
                0.2);
        
        // ===== PRODUCTOS DE HOGAR =====
        Producto lampara = new Producto(
            "HOG-001", "L�mpara LED", "Philips",
            Categoria.HOGAR, "L�mpara de lectura",
            10.0, 60.0,
                0.2);
        
        // ===== PAQUETES =====
        Paquete kitFutbol = new Paquete(
            "PAQ-001", "Kit F�tbol Completo", "Adidas",
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
        
        // ===== AGREGAR AL CAT�LOGO CON STOCK =====
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
        
        // Inyectar el cat�logo en el sistema (asumiendo que Sistema tiene setter)
        // Si Sistema tiene constructor con Catalogo, usar ese

        sistema = new Sistema(catalogo);
    }
    
    // ============================================================
    // TESTS DE CRITERIOS SIMPLES
    // ============================================================
    
    @Test
    @DisplayName("Deber�a filtrar por nombre exacto")
    void testFiltrarPorNombreExacto() {
        Criterio criterio = new CriterioNombre("Pelota");
        List<StockVendible> resultados = sistema.filtrarCon(criterio);
        
        assertEquals(1, resultados.size());
        assertEquals("Pelota de F�tbol", resultados.get(0).getNombre());
    }
    
    @Test
    @DisplayName("Deber�a filtrar por nombre parcial (case insensitive)")
    void testFiltrarPorNombreParcial() {
        Criterio criterio = new CriterioNombre("tec");
        List<StockVendible> resultados = sistema.filtrarCon(criterio);
        
        assertEquals(2, resultados.size());
        assertTrue(resultados.stream().anyMatch(sv -> sv.getNombre().equals("Teclado Mec�nico")));
        assertTrue(resultados.stream().anyMatch(sv -> sv.getNombre().equals("Raqueta de Tenis Tec")));
    }
    
        
    @Test
    @DisplayName("Deber�a filtrar por precio m�ximo")
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
    @DisplayName("Deber�a combinar criterios con AND")
    void testFiltrarConAnd() {
        Criterio and = new CriterioAND();
        
        and.agregarCriterio(new CriterioCategoria(Categoria.ELECTRONICA));
        and.agregarCriterio(new CriterioPrecio(100.0));
        
        List<StockVendible> resultados = sistema.filtrarCon(and);
        
        // Electr�nicos con precio <= 100: teclado(80), mouse(50)
        assertEquals(2, resultados.size());
        assertTrue(resultados.stream().allMatch(sv -> 
            sv.getVendible().getCategoria() == Categoria.ELECTRONICA &&
            sv.getVendible().getPrecioBase() <= 100.0
        ));
    }
    
    @Test
    @DisplayName("Deber�a combinar criterios con OR")
    void testFiltrarConOr() {
        Criterio or = new CriterioOR();
        
        or.agregarCriterio(new CriterioNombre("mouse"));
        or.agregarCriterio(new CriterioNombre("teclado"));
        
        List<StockVendible> resultados = sistema.filtrarCon(or);
        
        // Deber�a encontrar Mouse Inal�mbrico y Teclado Mec�nico
        assertEquals(2, resultados.size());
    }
    
    @Test
    @DisplayName("Deber�a combinar criterios con NOT")
    void testFiltrarConNot() {
        Criterio not = new CriterioNOT(new CriterioCategoria(Categoria.JUGUETES));
                
        List<StockVendible> resultados = sistema.filtrarCon(not);
        
        // Todos los productos que NO son juguetes
        assertEquals(10, resultados.size());

    }
    
    @Test
    @DisplayName("Deber�a anidar criterios complejos")
    void testFiltrarConAnidamiento() {
        // (Electr�nica AND precio <= 100) OR (Nombre contiene "Pelota")
        Criterio complejo = new CriterioOR();
        
        Criterio and = new CriterioAND();
        and.agregarCriterio(new CriterioCategoria(Categoria.ELECTRONICA));
        and.agregarCriterio(new CriterioPrecio(100.0));
                
        complejo.agregarCriterio(new CriterioNombre("Pelota"));
        complejo.agregarCriterio(and);
        
        
        List<StockVendible> resultados = sistema.filtrarCon(complejo);
        
        // Electr�nicos baratos (2) + Pelota (1) = 3
        assertEquals(3, resultados.size());
    }

           
    @Test
    @DisplayName("Deber�a filtrar por nombre y precio combinado")
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
    @DisplayName("Deber�a filtrar por nombre con NOT")
    void testFiltrarPorNombreConNot() {
        // NOT (nombre contiene "Mec�nico")
        Criterio criterio = new CriterioNOT(new CriterioNombre("Mec�nico")
        );
        
        List<StockVendible> resultados = sistema.filtrarCon(criterio);
        
        // Todos los productos excepto "Teclado Mec�nico"
        assertEquals(9, resultados.size());
        
    }
    
    // ============================================================
    // TEST DE B�SQUEDA POR CATEGOR�A CON PRECIO
    // ============================================================
    
    @Test
    @DisplayName("Deber�a filtrar electr�nicos entre $50 y $300")
    void testFiltrarElectronicosPorRangoPrecio() {
        Criterio criterio = new CriterioAND();
        
        criterio.agregarCriterio(new CriterioCategoria(Categoria.ELECTRONICA));
        criterio.agregarCriterio( new CriterioPrecio(300.0));
        criterio.agregarCriterio(new CriterioNOT(new CriterioPrecio(50.0)));
        
        List<StockVendible> resultados = sistema.filtrarCon(criterio);
        
        // Electr�nicos entre 50 y 300: parlantes(150), monitor(300), teclado(80)
        assertEquals(3, resultados.size());
    }
    
    @Test
    @DisplayName("Deber�a filtrar productos de hogar y juguetes combinados con OR")
    void testFiltrarHogarOJuguetes() {
        Criterio criterio = new CriterioOR();
        
        criterio.agregarCriterio( new CriterioCategoria(Categoria.HOGAR));
        criterio.agregarCriterio(new CriterioCategoria(Categoria.DEPORTES));
        
        List<StockVendible> resultados = sistema.filtrarCon(criterio);
        
        // 1 de hogar + 2 de deporte + paquete kitFutbol + paquete kitHogar= 5
        assertEquals(5, resultados.size());
    }
    

    
    
   
}