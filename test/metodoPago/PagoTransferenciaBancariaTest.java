package metodoPago;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoTransferenciaBancariaTest {

    @Test
    void procesarPago_debeLlamarALasApisEnOrden() {
        CuentaCorriente cuenta = new CuentaCorriente(123456789, "mi.alias");
        PagoTransferenciaBancaria pago = new PagoTransferenciaBancaria();

        try (MockedStatic<ApiTransferenciaBancaria> mockedApi = mockStatic(ApiTransferenciaBancaria.class)) {
            pago.procesarPago(200.0, cuenta);

            mockedApi.verify(() -> ApiTransferenciaBancaria.validarDatos(123456789, "mi.alias"));
            mockedApi.verify(() -> ApiTransferenciaBancaria.reservarFondos(123456789, "mi.alias"));
            mockedApi.verify(() -> ApiTransferenciaBancaria.ejecutarTransaccion(123456789, "mi.alias"));
        }
    }

    @Test
    void procesarPago_conMontoNegativo_debeLlamarValidarDatos() {
        CuentaCorriente cuenta = new CuentaCorriente(123456789, "mi.alias");
        PagoTransferenciaBancaria pago = new PagoTransferenciaBancaria();

        try (MockedStatic<ApiTransferenciaBancaria> mockedApi = mockStatic(ApiTransferenciaBancaria.class)) {
            assertDoesNotThrow(() -> pago.procesarPago(-30.0, cuenta));
            mockedApi.verify(() -> ApiTransferenciaBancaria.validarDatos(123456789, "mi.alias"));
        }
    }
}