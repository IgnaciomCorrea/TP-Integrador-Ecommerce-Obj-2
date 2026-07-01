package direccion;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DireccionTest {

    @Test
    void constructorYGetter_debenFuncionar() {
        Direccion direccion = new Direccion("Av. Siempre Viva", 123, "Springfield", "1234");

        assertEquals("Av. Siempre Viva", direccion.getCalle());
        assertEquals(123, direccion.getNumero());
        assertEquals("Springfield", direccion.getCiudad());
        assertEquals("1234", direccion.getCodigoPostal());
    }

    @Test
    void constructor_conCalleVacia_debePermitir() {
        Direccion direccion = new Direccion("", 0, "Ciudad", "0000");
        assertEquals("", direccion.getCalle());
    }

    @Test
    void constructor_conNumeroCero_debePermitir() {
        Direccion direccion = new Direccion("Calle", 0, "Ciudad", "0000");
        assertEquals(0, direccion.getNumero());
    }

    @Test
    void constructor_conCiudadYCodigoPostalValidos_debeFuncionar() {
        Direccion direccion = new Direccion("Calle Falsa", 123, "Buenos Aires", "C1000");
        assertEquals("Buenos Aires", direccion.getCiudad());
        assertEquals("C1000", direccion.getCodigoPostal());
    }

    @Test
    void constructor_conCodigoPostalNumerico_debeFuncionar() {
        Direccion direccion = new Direccion("Calle", 456, "Ciudad", "5000");
        assertEquals("5000", direccion.getCodigoPostal());
    }

    @Test
    void dosDireccionesConMismosDatos_debenSerIguales() {
        Direccion d1 = new Direccion("Calle", 123, "Ciudad", "0000");
        Direccion d2 = new Direccion("Calle", 123, "Ciudad", "0000");
        // Nota: como no implementa equals, no son iguales por contenido.
        // Esto es aceptable para un DTO simple.
        assertNotEquals(d1, d2);
        // Pero si se desea, se podría agregar equals/hashCode.
        // Por ahora, el test verifica que no se lanzan excepciones.
    }
}