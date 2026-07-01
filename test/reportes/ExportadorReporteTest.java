package reportes;

import Catalogo.Categoria;
import Catalogo.Producto;
import Catalogo.Paquete;
import Catalogo.ItemVendible;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExportadorReporteTest {

    private ReporteProductosMasVendidos reporte;

    @BeforeEach
    void setUp() {
        // Crear productos reales
        Producto p1 = new Producto("SKU001", "Teclado", "Logitech", Categoria.ELECTRONICA,
                "Teclado mecánico", 0.0, 100.0, 0.5);
        Producto p2 = new Producto("SKU002", "Mouse", "Logitech", Categoria.ELECTRONICA,
                "Mouse inalámbrico", 0.0, 50.0, 0.2);
        Paquete paq = new Paquete("SKU003", "Kit Gamer", "Logitech", Categoria.ELECTRONICA,
                "Teclado + Mouse", 10.0);

        // Crear visitor y acumular datos manualmente
        reporte = new ReporteProductosMasVendidos(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31)
        );

        // Simular visitas (como haría un pedido entregado)
        reporte.visitProducto(p1, 3, 300.0); // 3 teclados, precio total 300
        reporte.visitProducto(p2, 2, 100.0); // 2 mouse, precio total 100
        reporte.visitPaquete(paq, 1, 135.0); // 1 kit, precio final 135
    }

    @Test
    void exportarTexto_debeContenerDatosEnFormatoTabular() {
        String texto = ExportadorReporte.exportarTexto(reporte);

        assertTrue(texto.contains("=== PRODUCTOS MÁS VENDIDOS ==="));
        assertTrue(texto.contains("Período: 2026-01-01 - 2026-12-31"));
        assertTrue(texto.contains("SKU\tNombre\tCantidad\tPrecio Promedio"));
        assertTrue(texto.contains("SKU001\tTeclado\t3\t100.00"));
        assertTrue(texto.contains("SKU002\tMouse\t2\t50.00"));
        assertTrue(texto.contains("SKU003\tKit Gamer\t1\t135.00"));
    }

    @Test
    void exportarCSV_debeContenerDatosEnFormatoCSV() {
        String csv = ExportadorReporte.exportarCSV(reporte);

        assertTrue(csv.startsWith("SKU,Nombre,Cantidad,PrecioPromedio\n"));
        assertTrue(csv.contains("SKU001,Teclado,3,100.00"));
        assertTrue(csv.contains("SKU002,Mouse,2,50.00"));
        assertTrue(csv.contains("SKU003,Kit Gamer,1,135.00"));
    }

    @Test
    void exportarHTML_debeContenerDatosEnFormatoHTML() {
        String html = ExportadorReporte.exportarHTML(reporte);

        assertTrue(html.contains("<h1>Productos más vendidos</h1>"));
        assertTrue(html.contains("<p>Período: 2026-01-01 - 2026-12-31</p>"));
        assertTrue(html.contains("<th>SKU</th><th>Nombre</th><th>Cantidad</th><th>Precio Promedio</th>"));
        assertTrue(html.contains("<td>SKU001</td><td>Teclado</td><td>3</td><td>100.00</td>"));
        assertTrue(html.contains("<td>SKU002</td><td>Mouse</td><td>2</td><td>50.00</td>"));
        assertTrue(html.contains("<td>SKU003</td><td>Kit Gamer</td><td>1</td><td>135.00</td>"));
    }

    @Test
    void exportarTexto_conReporteVacio_debeMostrarSoloEncabezados() {
        ReporteProductosMasVendidos vacio = new ReporteProductosMasVendidos(
                LocalDate.now(), LocalDate.now()
        );
        String texto = ExportadorReporte.exportarTexto(vacio);
        assertTrue(texto.contains("=== PRODUCTOS MÁS VENDIDOS ==="));
        assertTrue(texto.contains("SKU\tNombre\tCantidad\tPrecio Promedio"));
        // No debe haber líneas de datos (solo el encabezado)
        long lineas = texto.lines().count();
        assertEquals(5, lineas); // título, período, encabezados, separador, y fin
    }
}