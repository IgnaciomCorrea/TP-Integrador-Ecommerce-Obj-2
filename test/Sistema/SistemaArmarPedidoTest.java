package Sistema;

import Catalogo.Catalogo;
import Catalogo.ItemVendible;
import Catalogo.Producto;
import Catalogo.Categoria;
import Catalogo.StockVendible;
import exceptions.PedidoExcepcion;
import pedido.Cancelado;
import pedido.Confirmado;
import pedido.ObservadorStock;
import pedido.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sistema.Sistema;
import testutils.PedidoFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SistemaArmarPedidoTest {

    @Mock
    private Catalogo catalogoMock;

    @Mock
    private ItemVendible itemMock;

    private Sistema sistema;

    @BeforeEach
    void setUp() {
        sistema = new Sistema(catalogoMock);
        // Configurar itemMock para que tenga SKU y cantidad
        when(itemMock.getSku()).thenReturn("SKU001");
        when(itemMock.getCantidad()).thenReturn(2);
    }

    @Test
    void armarPedido_conStockSuficiente_debeConfirmarYGuardar() {
        Pedido pedido = PedidoFactory.pedido();
        pedido.agregarVendible(itemMock);

        when(catalogoMock.verificarStockPedido(pedido)).thenReturn(true);

        sistema.armarPedido(pedido);

        // Verificar que el pedido se guardó
        assertEquals(1, sistema.getPedidos().size());
        assertTrue(sistema.getPedidos().contains(pedido));

        // Verificar que se confirmó (debe estar en Confirmado o más adelante)
        assertTrue(pedido.getEstado() instanceof Confirmado);

        // Verificar que se agregó el observador de stock
        assertTrue(pedido.getObservadores().stream()
                .anyMatch(obs -> obs instanceof ObservadorStock));
    }

    @Test
    void armarPedido_sinStock_debeCancelarYGuardar() {
        Pedido pedido = PedidoFactory.pedido();
        pedido.agregarVendible(itemMock);

        when(catalogoMock.verificarStockPedido(pedido)).thenReturn(false);

        sistema.armarPedido(pedido);

        assertEquals(1, sistema.getPedidos().size());
        assertTrue(pedido.getEstado() instanceof Cancelado);
    }

    @Test
    void armarPedido_conPedidoNull_debeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> sistema.armarPedido(null));
    }

    @Test
    void armarPedido_conVariosItemsYStockSuficiente_debeConfirmar() {
        Pedido pedido = PedidoFactory.pedido();
        pedido.agregarVendible(itemMock);
        ItemVendible otroItem = mock(ItemVendible.class);
        when(otroItem.getSku()).thenReturn("SKU002");
        when(otroItem.getCantidad()).thenReturn(1);
        pedido.agregarVendible(otroItem);

        when(catalogoMock.verificarStockPedido(pedido)).thenReturn(true);

        sistema.armarPedido(pedido);
        assertTrue(pedido.getEstado() instanceof Confirmado);
        assertEquals(1, sistema.getPedidos().size());
    }
}