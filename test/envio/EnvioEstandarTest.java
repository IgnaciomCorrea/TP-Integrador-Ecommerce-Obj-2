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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvioEstandarTest {

    @Mock
    private CorreoArgentina correoMock;

    @Mock
    private ItemVendible itemMock1;

    @Mock
    private ItemVendible itemMock2;

    private EnvioEstandar envioEstandar;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        envioEstandar = new EnvioEstandar(correoMock);
        pedido = PedidoFactory.pedido();

        // Configurar mocks para calcular peso total
        when(itemMock1.getPeso()).thenReturn(0.5);
        when(itemMock2.getPeso()).thenReturn(0.2);

        pedido.agregarVendible(itemMock1);
        pedido.agregarVendible(itemMock2);
    }

    @Test
    void calcularCosto_debeUsarCorreoArgentinaConPesoTotalYDireccion() {
        Direccion direccion = new Direccion("Av. Siempre Viva", 123, "Springfield", "1234");
        Sucursal sucursal = mock(Sucursal.class);

        // Peso total = 0.5 + 0.2 = 0.7
        when(correoMock.estimarEnvio(0.7f, direccion)).thenReturn(150.0f);

        double costo = envioEstandar.calcularCosto(pedido, direccion, sucursal);
        assertEquals(150.0, costo, 0.001);
        verify(correoMock, times(1)).estimarEnvio(0.7f, direccion);
    }

    @Test
    void calcularTiempo_debeRetornar5Dias() {
        Direccion direccion = new Direccion("Av. Siempre Viva", 123, "Springfield", "1234");
        Sucursal sucursal = mock(Sucursal.class);

        int tiempo = envioEstandar.calcularTiempo(pedido, direccion, sucursal);
        assertEquals(5, tiempo);
    }
}