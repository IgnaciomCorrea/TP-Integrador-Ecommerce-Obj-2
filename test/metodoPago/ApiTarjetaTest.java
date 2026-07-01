package metodoPago;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApiTarjetaTest {

    @Test
    void constructorVacio_debeCrearInstancia() {
        assertDoesNotThrow(() -> new ApiTarjeta());
        ApiTarjeta api = new ApiTarjeta();
        assertNotNull(api);
    }
}