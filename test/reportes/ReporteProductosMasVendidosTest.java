package reportes;

import Catalogo.Producto;
import Catalogo.Paquete;
import Catalogo.Categoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReporteProductosMasVendidosTest {

    private ReporteProductosMasVendidos reporte;
    private Producto producto1;
    private Producto producto2;
    private Paquete paquete1;

    @BeforeEach
    void setUp() {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fin = LocalDate.of(2026, 6, 1);
        reporte = new ReporteProductosMasVendidos(inicio, fin);

        producto1 = new Producto("SKU001", "Teclado", "Logitech", Categoria.ELECTRONICA,
                "Teclado mecánico", 0.0, 100.0, 0.5);
        producto2 = new Producto("SKU002", "Mouse", "Razer", Categoria.ELECTRONICA,
                "Mouse gamer", 0.0, 50.0, 0.2);
        paquete1 = new Paquete("PKG001", "Kit Gamer", "Logitech", Categoria.ELECTRONICA,
                "Kit con teclado y mouse", 10.0);
    }

    @Test
    void visitProducto_debeAcumularCantidadYPrecio() {
        reporte.visit(producto1, 3, 300.0);
        reporte.visit(producto1, 2, 200.0);

        List<ItemVenta> resultados = reporte.getResultados();
        assertEquals(1, resultados.size());
        ItemVenta item = resultados.get(0);
        assertEquals("SKU001", item.getSku());
        assertEquals("Teclado", item.getNombre());
        assertEquals(5, item.getCantidad());
        assertEquals(100.0, item.getPrecioPromedio(), 0.001);
    }

    @Test
    void visitProducto_y_visitPaquete_debenAcumularPorSeparado() {
        reporte.visit(producto1, 3, 300.0);
        reporte.visit(paquete1, 2, 200.0);
        reporte.visit(producto2, 5, 250.0);

        List<ItemVenta> resultados = reporte.getResultados();
        assertEquals(3, resultados.size());

        // Verificar que todos están presentes
        assertTrue(resultados.stream().anyMatch(i -> i.getSku().equals("SKU001")));
        assertTrue(resultados.stream().anyMatch(i -> i.getSku().equals("SKU002")));
        assertTrue(resultados.stream().anyMatch(i -> i.getSku().equals("PKG001")));
    }

    @Test
    void getResultados_debeOrdenarPorCantidadDescendente() {
        reporte.visit(producto1, 2, 200.0);
        reporte.visit(producto2, 5, 250.0);
        reporte.visit(paquete1, 3, 300.0);

        List<ItemVenta> resultados = reporte.getResultados();
        assertEquals(3, resultados.size());
        assertEquals("SKU002", resultados.get(0).getSku()); // 5 unidades
        assertEquals("PKG001", resultados.get(1).getSku()); // 3 unidades
        assertEquals("SKU001", resultados.get(2).getSku()); // 2 unidades
    }

    @Test
    void getResultados_conMismoSku_debeSumarCantidades() {
        reporte.visit(producto1, 2, 200.0);
        reporte.visit(producto1, 3, 300.0);

        List<ItemVenta> resultados = reporte.getResultados();
        assertEquals(1, resultados.size());
        assertEquals(5, resultados.get(0).getCantidad());
        assertEquals(100.0, resultados.get(0).getPrecioPromedio(), 0.001);
    }

    @Test
    void getResultados_conVariosItems_debeCalcularPrecioPromedioCorrectamente() {
        // Producto1: 2 unidades a 200 total => precio promedio 100
        // Producto1: 3 unidades a 330 total => precio promedio 110
        // Producto1: total 5 unidades, precio total 530 => promedio 106
        reporte.visit(producto1, 2, 200.0);
        reporte.visit(producto1, 3, 330.0);

        List<ItemVenta> resultados = reporte.getResultados();
        assertEquals(1, resultados.size());
        assertEquals(5, resultados.get(0).getCantidad());
        assertEquals(106.0, resultados.get(0).getPrecioPromedio(), 0.001);
    }

    @Test
    void getResultados_conPedidoVacio_debeRetornarListaVacia() {
        ReporteProductosMasVendidos reporteVacio = new ReporteProductosMasVendidos(
                LocalDate.now(), LocalDate.now().plusDays(1)
        );
        List<ItemVenta> resultados = reporteVacio.getResultados();
        assertTrue(resultados.isEmpty());
    }

    @Test
    void getFechaInicioYFin_debenRetornarCorrectamente() {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fin = LocalDate.of(2026, 6, 1);
        ReporteProductosMasVendidos r = new ReporteProductosMasVendidos(inicio, fin);
        assertEquals(inicio, r.getFechaInicio());
        assertEquals(fin, r.getFechaFin());
    }
}