package metodoPago;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BilleteraVirtualTest {

    @Test
    void constructorYGetter_debenFuncionar() {
        BilleteraVirtual billetera = new BilleteraVirtual(150.0);
        assertEquals(150.0, billetera.getSaldo());
    }
}