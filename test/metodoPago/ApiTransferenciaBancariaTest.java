package metodoPago;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApiTransferenciaBancariaTest {

    @Test
    void constructorVacio_debeCrearInstancia() {
        assertDoesNotThrow(() -> new ApiTransferenciaBancaria());
        ApiTransferenciaBancaria api = new ApiTransferenciaBancaria();
        assertNotNull(api);
    }
}