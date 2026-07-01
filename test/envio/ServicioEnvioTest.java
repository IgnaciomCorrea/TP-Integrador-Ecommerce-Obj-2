package envio;

import direccion.Direccion;
import pedido.Pedido;
import sucursal.Sucursal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioEnvioTest {

    @Mock
    private MetodoEnvio estrategiaMock;

    @Mock
    private Pedido pedidoMock;

    private ServicioEnvio servicio;

    @BeforeEach
    void setUp() {
        servicio = new ServicioEnvio(estrategiaMock);
    }

    @Test
    void calcularCosto_debeDelegarEnEstrategia() {
        Direccion direccion = new Direccion("Av. Siempre Viva", 123, "Springfield", "1234");
        Sucursal sucursal = mock(Sucursal.class);

        when(estrategiaMock.calcularCosto(pedidoMock, direccion, sucursal)).thenReturn(200.0);

        double costo = servicio.calcularCosto(pedidoMock, direccion, sucursal);
        assertEquals(200.0, costo, 0.001);
        verify(estrategiaMock, times(1)).calcularCosto(pedidoMock, direccion, sucursal);
    }

    @Test
    void calcularTiempo_debeDelegarEnEstrategia() {
        Direccion direccion = new Direccion("Av. Siempre Viva", 123, "Springfield", "1234");
        Sucursal sucursal = mock(Sucursal.class);

        when(estrategiaMock.calcularTiempo(pedidoMock, direccion, sucursal)).thenReturn(3);

        int tiempo = servicio.calcularTiempo(pedidoMock, direccion, sucursal);
        assertEquals(3, tiempo);
        verify(estrategiaMock, times(1)).calcularTiempo(pedidoMock, direccion, sucursal);
    }

    @Test
    void setEstrategia_debeCambiarEstrategia() {
        MetodoEnvio nuevaEstrategia = mock(MetodoEnvio.class);
        servicio.setEstrategia(nuevaEstrategia);

        Direccion direccion = new Direccion("Av. Siempre Viva", 123, "Springfield", "1234");
        Sucursal sucursal = mock(Sucursal.class);

        servicio.calcularCosto(pedidoMock, direccion, sucursal);

        verify(nuevaEstrategia, times(1)).calcularCosto(pedidoMock, direccion, sucursal);
        verify(estrategiaMock, never()).calcularCosto(any(), any(), any());
    }

    @Test
    void constructor_conEstrategiaNull_debeLanzarExcepcion() {
        assertThrows(NullPointerException.class, () -> new ServicioEnvio(null));
    }
}