package Catalogo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class AtributoTest {

    @Test
    @DisplayName("Debería crear atributo con nombre y valor correctos")
    void testConstructorYGetter() {
        Atributo<String> atributo = new Atributo<>("color", "rojo");
        assertEquals("color", atributo.getNombre());
        assertEquals("rojo", atributo.getValor());
    }

    @Test
    @DisplayName("Debería modificar el nombre")
    void testSetNombre() {
        Atributo<Integer> atributo = new Atributo<>("edad", 30);
        atributo.setNombre("años");
        assertEquals("años", atributo.getNombre());
    }

    @Test
    @DisplayName("Debería modificar el valor")
    void testSetValor() {
        Atributo<Double> atributo = new Atributo<>("peso", 75.5);
        atributo.setValor(80.0);
        assertEquals(80.0, atributo.getValor());
    }

    @Test
    @DisplayName("Debería funcionar con tipos diferentes")
    void testDiferentesTipos() {
        Atributo<String> str = new Atributo<>("nombre", "Juan");
        Atributo<Integer> entero = new Atributo<>("edad", 25);
        Atributo<Boolean> bool = new Atributo<>("activo", true);

        assertEquals("Juan", str.getValor());
        assertEquals(25, entero.getValor());
        assertTrue(bool.getValor());
    }
}