package metodoPago;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MedioDePagoTest {

    @Test
    void constructorVacio_debeCrearInstancia() {
        assertDoesNotThrow(() -> new MedioDePago());
        MedioDePago medio = new MedioDePago();
        assertNotNull(medio);
    }
}