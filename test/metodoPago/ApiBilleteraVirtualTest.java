package metodoPago;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApiBilleteraVirtualTest {

    @Test
    void constructorVacio_debeCrearInstancia() {
        assertDoesNotThrow(() -> new ApiBilleteraVirtual());
        ApiBilleteraVirtual api = new ApiBilleteraVirtual();
        assertNotNull(api);
    }
}