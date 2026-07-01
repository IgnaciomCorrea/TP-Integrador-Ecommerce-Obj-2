package metodoPago;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MetodoPagoTest {

    @Test
    void procesarPago_debeEjecutarPasosEnOrden() {
        PagoRegistrable pago = new PagoRegistrable();
        MedioDePago medio = new MedioDePago();

        pago.procesarPago(150.0, medio);

        assertEquals(List.of("validar", "reservar", "ejecutar", "notificar"), pago.getPasos());
    }

    private static class PagoRegistrable extends MetodoPago<MedioDePago> {
        private final List<String> pasos = new ArrayList<>();

        @Override
        protected void validarDatos(Double monto, MedioDePago medio) {
            pasos.add("validar");
        }

        @Override
        protected void reservarFondos(Double monto, MedioDePago medio) {
            pasos.add("reservar");
        }

        @Override
        protected void ejecutarTransaccion(Double monto, MedioDePago medio) {
            pasos.add("ejecutar");
        }

        @Override
        protected void notificarResultado(Double monto, MedioDePago medio) {
            pasos.add("notificar");
        }

        List<String> getPasos() {
            return pasos;
        }
    }
}
