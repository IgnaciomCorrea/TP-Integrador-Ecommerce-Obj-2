package envio;

import Catalogo.Categoria;
import Catalogo.ItemVendible;
import Catalogo.Producto;
import direccion.Direccion;
import pedido.Pedido;
import sucursal.Sucursal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testutils.PedidoFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvioExpressTest {

    @Mock
    private EnvioExpressCalculadora calculadoraMock;

    private EnvioExpress envioExpress;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        envioExpress = new EnvioExpress(calculadoraMock);
        pedido = PedidoFactory.pedido();

        // Instanciamos productos
        Producto producto1 = new Producto(
                "SKU001", "Teclado", "Logitech", Categoria.ELECTRONICA,
                "Teclado mecánico", 0.0, 100.0, 0.5);
        Producto producto2 = new Producto(
                "SKU002", "Mouse", "Logitech", Categoria.ELECTRONICA,
                "Mouse inalámbrico", 0.0, 50.0, 0.2);

        // Agregamos al pedido productos
        pedido.agregarVendible(new ItemVendible(1, producto1));
        pedido.agregarVendible(new ItemVendible(1, producto2));
    }

    @Test
    void calcularCosto_debeUsarCalculadoraConPrecioTotal() {
        Direccion direccion = new Direccion("Av. Corrientes", 123, "Buenos Aires", "1234");
        Sucursal sucursal = mock(Sucursal.class);

        // El precio total del pedido es 100 + 50 = 150
        when(calculadoraMock.calcularCosto(150.0f)).thenReturn(22.5f);

        double costo = envioExpress.calcularCosto(pedido, direccion, sucursal);
        assertEquals(22.5, costo, 0.001);
        verify(calculadoraMock, times(1)).calcularCosto(150.0f);
    }

    @Test
    void calcularTiempo_debeRetornar1Dia() {
        Direccion direccion = new Direccion("Av. Cordoba", 321, "Buenos Aires", "4321");
        Sucursal sucursal = mock(Sucursal.class);

        int tiempo = envioExpress.calcularTiempo(pedido, direccion, sucursal);
        assertEquals(1, tiempo);
    }
}