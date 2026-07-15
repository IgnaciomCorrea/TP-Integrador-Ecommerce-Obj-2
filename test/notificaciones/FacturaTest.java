package notificaciones;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class FacturaTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void constructor_debeAlmacenarValores() {
        Factura factura = new Factura(150.0, 30.0);
        factura.imprimirFactura();

        String output = outContent.toString();
        assertTrue(output.contains("Precio Producto: 150.0"));
        assertTrue(output.contains("Precio Envio: 30.0"));
        assertTrue(output.contains("Total = 180.0"));
    }

    @Test
    void imprimirFactura_debeMostrarPrecioProductoYPrecioEnvio() {
        Factura factura = new Factura(100.0, 25.0);
        factura.imprimirFactura();

        String output = outContent.toString();
        assertTrue(output.contains("Precio Producto: 100.0"));
        assertTrue(output.contains("Precio Envio: 25.0"));
        assertTrue(output.contains("Total = 125.0"));
    }

    @Test
    void imprimirFactura_conCeroEnvio() {
        Factura factura = new Factura(200.0, 0.0);
        factura.imprimirFactura();

        String output = outContent.toString();
        assertTrue(output.contains("Precio Producto: 200.0"));
        assertTrue(output.contains("Precio Envio: 0.0"));
        assertTrue(output.contains("Total = 200.0"));
    }

    @Test
    void imprimirFactura_conValoresDecimales() {
        Factura factura = new Factura(99.99, 9.99);
        factura.imprimirFactura();

        String output = outContent.toString();
        assertTrue(output.contains("Precio Producto: 99.99"));
        assertTrue(output.contains("Precio Envio: 9.99"));
    }
}