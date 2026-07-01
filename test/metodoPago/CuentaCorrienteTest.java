package metodoPago;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CuentaCorrienteTest {

    @Test
    void constructorYGetter_debenFuncionar() {
        CuentaCorriente cuenta = new CuentaCorriente(123456789, "mi.alias");
        assertEquals(123456789, cuenta.getCbu());
        assertEquals("mi.alias", cuenta.getAlias());
    }
}