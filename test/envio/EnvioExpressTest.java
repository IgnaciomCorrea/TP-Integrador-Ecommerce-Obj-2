package envio;

import Catalogo.ItemVendible;
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

    @Mock
    private ItemVendible itemMock1;

    @Mock
    private ItemVendible itemMock2;

    private EnvioExpress envioExpress;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        envioExpress = new EnvioExpress(calculadoraMock);
        pedido = PedidoFactory.pedido();



        pedido.agregarVendible(itemMock1);
        pedido.agregarVendible(itemMock2);
    }

    @Test
    void calcularCosto_debeUsarCalculadoraConPrecioTotal() {
        Direccion direccion = new Direccion("Av. Siempre Viva", 123, "Springfield", "1234");
        Sucursal sucursal = mock(Sucursal.class);

        // Configurar mocks para calcular precio total
        when(itemMock1.getPrecioFinal()).thenReturn(100.0);
        when(itemMock2.getPrecioFinal()).thenReturn(50.0);

        when(calculadoraMock.calcularCosto(150.0f)).thenReturn(22.5f);

        double costo = envioExpress.calcularCosto(pedido, direccion, sucursal);
        assertEquals(22.5, costo, 0.001);
        verify(calculadoraMock, times(1)).calcularCosto(150.0f);
    }

    @Test
    void calcularTiempo_debeRetornar1Dia() {
        Direccion direccion = new Direccion("Av. Siempre Viva", 123, "Springfield", "1234");
        Sucursal sucursal = mock(Sucursal.class);

        int tiempo = envioExpress.calcularTiempo(pedido, direccion, sucursal);
        assertEquals(1, tiempo);
    }
}
