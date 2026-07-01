package metodoPago;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class TarjetaDeCreditoTest {

    @Test
    void constructorYGetter_debenFuncionar() {
        Date fecha = new Date();
        TarjetaDeCredito tarjeta = new TarjetaDeCredito(123456789, 123, fecha);
        assertEquals(123456789, tarjeta.getNroTarjeta());
        assertEquals(123, tarjeta.getCvv());
        assertEquals(fecha, tarjeta.getFechaVencimiento());
        assertTrue(tarjeta instanceof MedioDePago);
    }

    @Test
    void constructorConCvvCero() {
        Date fecha = new Date();
        TarjetaDeCredito tarjeta = new TarjetaDeCredito(123, 0, fecha);
        assertEquals(0, tarjeta.getCvv());
    }

    @Test
    void constructorConFechaNull() {
        TarjetaDeCredito tarjeta = new TarjetaDeCredito(123, 456, null);
        assertNull(tarjeta.getFechaVencimiento());
    }
}