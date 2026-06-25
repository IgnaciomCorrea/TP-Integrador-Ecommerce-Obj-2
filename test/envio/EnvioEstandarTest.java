package envio;

import Catalogo.Categoria;
import Catalogo.Producto;
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
class EnvioEstandarTest {

    @Mock
    private CorreoArgentina correoMock;

    private EnvioEstandar envioEstandar;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        envioEstandar = new EnvioEstandar(correoMock);
        pedido = new Pedido();
        // Agregar productos con peso
        Producto p1 = new Producto("SKU1", "Teclado", "Logitech", Categoria.ELECTRONICA,
                "Teclado mecánico", 0.0, 100.0, 0.5);
        Producto p2 = new Producto("SKU2", "Mouse", "Logitech", Categoria.ELECTRONICA,
                "Mouse inalámbrico", 0.0, 50.0, 0.2);
        pedido.agregarVendible(p1);
        pedido.agregarVendible(p2);
    }

    @Test
    void calcularCosto_debeUsarCorreoArgentinaConPesoYDireccion() {
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