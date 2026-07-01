package metodoPago;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BilleteraVirtualTest {

    @Test
    void constructorYGetter_debenFuncionar() {
        BilleteraVirtual billetera = new BilleteraVirtual(150.0);
        assertEquals(150.0, billetera.getSaldo());
        assertTrue(billetera instanceof MedioDePago); // hereda de MedioDePago
    }

    @Test
    void constructorConSaldoCero() {
        BilleteraVirtual billetera = new BilleteraVirtual(0.0);
        assertEquals(0.0, billetera.getSaldo());
    }

    @Test
    void constructorConSaldoNegativo() {
        BilleteraVirtual billetera = new BilleteraVirtual(-10.0);
        assertEquals(-10.0, billetera.getSaldo());
    }
}