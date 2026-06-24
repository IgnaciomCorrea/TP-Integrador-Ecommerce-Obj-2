package envio;

import Catalogo.Categoria;
import Catalogo.Producto;
import direccion.Direccion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pedido.Pedido;
import sucursal.Sucursal;

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
        pedido = new Pedido();
        Producto p1 = new Producto("SKU1", "Teclado", "Logitech", Categoria.ELECTRONICA,
                "teclado mecanico", 0.0, 100.0, 0.5);
        Producto p2 = new Producto("SKU2", "Mouse", "Logitech", Categoria.ELECTRONICA,
                "mouse nuevo", 0.0, 50.0, 0.2);
        pedido.agregarVendible(p1);
        pedido.agregarVendible(p2);
    }

    @Test
    void calcularCosto_debeUsarCalculadoraConPrecioTotal() {
        Direccion direccion = new Direccion("Av. Siempre Viva", 123, "Springfield", "1234");
        Sucursal sucursal = mock(Sucursal.class);

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