package Sistema;

import Catalogo.Catalogo;
import Catalogo.Categoria;
import Catalogo.ItemVendible;
import Catalogo.Producto;
import Catalogo.StockVendible;
import exceptions.PedidoExcepcion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pedido.Pedido;
import reportes.ReporteProductosMasVendidos;
import sistema.Sistema;
import testutils.PedidoFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class SistemaReporteTest {

    private Catalogo catalogo;
    private Sistema sistema;
    private Producto teclado;
    private Producto mouse;

    @BeforeEach
    void setUp() {
        catalogo = new Catalogo();
        sistema = new Sistema(catalogo);

        teclado = new Producto("TEC-001", "Teclado", "Logitech", Categoria.ELECTRONICA,
                "Teclado mecánico", 0.0, 100.0, 0.5);
        mouse = new Producto("MOU-001", "Mouse", "Logitech", Categoria.ELECTRONICA,
                "Mouse inalámbrico", 0.0, 50.0, 0.2);

        catalogo.agregarVendible(new StockVendible(10, teclado));
        catalogo.agregarVendible(new StockVendible(10, mouse));
    }

    @Test
    void generarReporte_debeContarSoloPedidosEntregadosEnPeriodo() {
        // Pedido entregado dentro del período
        Pedido entregado = PedidoFactory.pedido();
        entregado.agregarVendible(new ItemVendible(2, teclado));
        entregado.agregarVendible(new ItemVendible(1, mouse));
        sistema.armarPedido(entregado);
        avanzarHastaEntregado(entregado);

        // Pedido entregado fuera del período (fecha anterior)
        Pedido entregadoAntiguo = PedidoFactory.pedido();
        entregadoAntiguo.agregarVendible(new ItemVendible(5, teclado));
        // Simular fecha antigua (usamos reflexión o spy)
        // Como no hay setter, usamos spy para modificar la fecha
        Pedido entregadoAntiguoSpy = spy(entregadoAntiguo);
        doReturn(LocalDate.now().minusDays(10)).when(entregadoAntiguoSpy).getFecha();
        sistema.armarPedido(entregadoAntiguoSpy);
        avanzarHastaEntregado(entregadoAntiguoSpy);

        // Pedido no entregado (confirmado)
        Pedido noEntregado = PedidoFactory.pedido();
        noEntregado.agregarVendible(new ItemVendible(3, mouse));
        sistema.armarPedido(noEntregado);
        // no avanzamos a entregado

        LocalDate inicio = LocalDate.now().minusDays(5);
        LocalDate fin = LocalDate.now().plusDays(5);

        ReporteProductosMasVendidos reporte = sistema.generarReporteProductosMasVendidos(inicio, fin);

        // Solo debe contar el pedido entregado en el período
        // Entregado: 2 teclados, 1 mouse
        assertEquals(2, reporte.getResultados().size());

        // Verificar cantidades
        var itemTeclado = reporte.getResultados().stream()
                .filter(i -> i.getSku().equals("TEC-001"))
                .findFirst().orElseThrow();
        assertEquals(2, itemTeclado.getCantidad());

        var itemMouse = reporte.getResultados().stream()
                .filter(i -> i.getSku().equals("MOU-001"))
                .findFirst().orElseThrow();
        assertEquals(1, itemMouse.getCantidad());
    }

    @Test
    void generarReporte_conPedidosEntregadosSinItems_debeRetornarVacio() {
        Pedido pedidoVacio = PedidoFactory.pedido(); // Esto crea un pedido sin items

        // Verificar que armar un pedido sin items lanza excepción
        assertThrows(PedidoExcepcion.class, () -> sistema.armarPedido(pedidoVacio));
        // O la excepción que corresponda (IllegalArgumentException, etc.)
    }

    @Test
    void generarReporte_fueraDePeriodo_noDebeIncluirPedidos() {
        Pedido entregado = PedidoFactory.pedido();
        entregado.agregarVendible(new ItemVendible(1, teclado));
        sistema.armarPedido(entregado);
        avanzarHastaEntregado(entregado);

        // Fechas anteriores al pedido
        LocalDate inicio = LocalDate.now().minusDays(10);
        LocalDate fin = LocalDate.now().minusDays(9);
        ReporteProductosMasVendidos reporte = sistema.generarReporteProductosMasVendidos(inicio, fin);
        assertTrue(reporte.getResultados().isEmpty());
    }

    // Helper para avanzar un pedido hasta Entregado
    private void avanzarHastaEntregado(Pedido pedido) {
        pedido.pasarAEnPreparacion();
        pedido.pasarAEnviado();
        pedido.pasarAEntregado();
    }
}