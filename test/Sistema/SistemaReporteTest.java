package Sistema;

import Catalogo.Catalogo;
import Catalogo.Categoria;
import Catalogo.ItemVendible;
import Catalogo.Producto;
import Catalogo.StockVendible;
import org.junit.jupiter.api.Test;
import pedido.Pedido;
import reportes.ReporteProductosMasVendidos;
import sistema.Sistema;
import testutils.PedidoFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SistemaReporteTest {

    @Test
    void generarReporteProductosMasVendidos_debeContarSoloPedidosEntregados() {
        Catalogo catalogo = new Catalogo();
        Sistema sistema = new Sistema(catalogo);
        Producto teclado = new Producto(
                "TEC-001",
                "Teclado",
                "Logitech",
                Categoria.ELECTRONICA,
                "Teclado mecanico",
                0.0,
                100.0,
                0.5
        );
        catalogo.agregarVendible(new StockVendible(10, teclado));

        Pedido entregado = PedidoFactory.pedido();
        entregado.agregarVendible(new ItemVendible(2, teclado));
        sistema.armarPedido(entregado);
        entregado.pasarAEnPreparacion();
        entregado.pasarAEnviado();
        entregado.pasarAEntregado();

        Pedido confirmadoNoEntregado = PedidoFactory.pedido();
        confirmadoNoEntregado.agregarVendible(new ItemVendible(3, teclado));
        sistema.armarPedido(confirmadoNoEntregado);

        ReporteProductosMasVendidos reporte = sistema.generarReporteProductosMasVendidos(
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1)
        );

        assertEquals(1, reporte.getResultados().size());
        assertEquals(2, reporte.getResultados().get(0).getCantidad());
    }
}
