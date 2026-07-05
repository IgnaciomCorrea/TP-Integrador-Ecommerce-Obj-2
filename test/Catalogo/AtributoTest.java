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
    @DisplayName("Debería modificar el nombre únicamente")
    void testSetNombre() {
        Atributo<Integer> atributo = new Atributo<>("altura", 30);
        atributo.setNombre("ancho");
        assertEquals("ancho", atributo.getNombre());
        assertEquals(30, atributo.getValor());
    }

    @Test
    @DisplayName("Debería modificar el valor")
    void testSetValor() {
        Atributo<Double> atributo = new Atributo<>("precioDolar", 75.5);
        atributo.setValor(80.0);
        assertEquals("precioDolar", atributo.getNombre());
        assertEquals(80.0, atributo.getValor());
    }

    @Test
    @DisplayName("Debería funcionar con tipos diferentes")
    void testDiferentesTipos() {
        Atributo<String> str = new Atributo<>("color", "verde");
        Atributo<Integer> entero = new Atributo<>("ancho", 25);
        Atributo<Boolean> bool = new Atributo<>("importado", true);

        assertEquals("verde", str.getValor());
        assertEquals(25, entero.getValor());
        assertTrue(bool.getValor());
    }
}