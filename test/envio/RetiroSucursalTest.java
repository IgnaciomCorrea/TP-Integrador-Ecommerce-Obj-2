package envio;

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
class RetiroSucursalTest {

    private RetiroSucursal retiroSucursal;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        retiroSucursal = new RetiroSucursal();
        pedido = PedidoFactory.pedido();
    }

    @Test
    void calcularCosto_siempreCero() {
        Direccion direccion = new Direccion("Av. Siempre Viva", 123, "Springfield", "1234");
        Sucursal sucursal = mock(Sucursal.class);

        double costo = retiroSucursal.calcularCosto(pedido, direccion, sucursal);
        assertEquals(0.0, costo);
    }

    @Test
    void calcularTiempo_conStockLocal_debeRetornar0() {
        Direccion direccion = new Direccion("Av. Siempre Viva", 123, "Springfield", "1234");
        Sucursal sucursal = mock(Sucursal.class);
        when(sucursal.hayStock(pedido)).thenReturn(true);

        int tiempo = retiroSucursal.calcularTiempo(pedido, direccion, sucursal);
        assertEquals(0, tiempo);
    }

    @Test
    void calcularTiempo_sinStockLocal_debeRetornar3() {
        Direccion direccion = new Direccion("Av. Siempre Viva", 123, "Springfield", "1234");
        Sucursal sucursal = mock(Sucursal.class);
        when(sucursal.hayStock(pedido)).thenReturn(false);

        int tiempo = retiroSucursal.calcularTiempo(pedido, direccion, sucursal);
        assertEquals(3, tiempo);
    }

    @Test
    void calcularTiempo_siSucursalEsNull_debeLanzarExcepcion() {
        Direccion direccion = new Direccion("Av. Siempre Viva", 123, "Springfield", "1234");

        assertThrows(NullPointerException.class,
                () -> retiroSucursal.calcularTiempo(pedido, direccion, null));
    }
}