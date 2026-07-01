package metodoPago;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoPorTarjetaDeCreditoTest {

    @Test
    void procesarPago_debeLlamarALasApisEnOrden() {
        Date fecha = new Date();
        TarjetaDeCredito tarjeta = new TarjetaDeCredito(123456789, 123, fecha);
        PagoPorTarjetaDeCredito pago = new PagoPorTarjetaDeCredito();

        try (MockedStatic<ApiTarjeta> mockedApi = mockStatic(ApiTarjeta.class)) {
            pago.procesarPago(100.0, tarjeta);

            mockedApi.verify(() -> ApiTarjeta.validarDatos(123456789, 123, fecha));
            mockedApi.verify(() -> ApiTarjeta.reservarFondos(123456789, 123, fecha));
            mockedApi.verify(() -> ApiTarjeta.ejecutarTransaccion(123456789, 123, fecha));
            mockedApi.verifyNoMoreInteractions();
        }
    }

    @Test
    void procesarPago_conMontoNegativo_debeLlamarValidarDatos() {
        Date fecha = new Date();
        TarjetaDeCredito tarjeta = new TarjetaDeCredito(123456789, 123, fecha);
        PagoPorTarjetaDeCredito pago = new PagoPorTarjetaDeCredito();

        try (MockedStatic<ApiTarjeta> mockedApi = mockStatic(ApiTarjeta.class)) {
            assertDoesNotThrow(() -> pago.procesarPago(-50.0, tarjeta));
            mockedApi.verify(() -> ApiTarjeta.validarDatos(123456789, 123, fecha));
        }
    }
}