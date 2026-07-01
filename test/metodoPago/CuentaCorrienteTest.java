package metodoPago;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CuentaCorrienteTest {

    @Test
    void constructorYGetter_debenFuncionar() {
        CuentaCorriente cuenta = new CuentaCorriente(123456789, "mi.alias");
        assertEquals(123456789, cuenta.getCbu());
        assertEquals("mi.alias", cuenta.getAlias());
        assertTrue(cuenta instanceof MedioDePago);
    }

    @Test
    void constructorConCbuCero() {
        CuentaCorriente cuenta = new CuentaCorriente(0, "alias");
        assertEquals(0, cuenta.getCbu());
        assertEquals("alias", cuenta.getAlias());
    }

    @Test
    void constructorConAliasVacio() {
        CuentaCorriente cuenta = new CuentaCorriente(123, "");
        assertEquals(123, cuenta.getCbu());
        assertEquals("", cuenta.getAlias());
    }
}