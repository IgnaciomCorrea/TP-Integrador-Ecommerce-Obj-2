package metodoPago;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoPorBilleteraVirtualTest {

    @Test
    void procesarPago_debeLlamarALasApisEnOrden() {
        BilleteraVirtual billetera = new BilleteraVirtual(100.0);
        PagoPorBilleteraVirtual pago = new PagoPorBilleteraVirtual();

        try (MockedStatic<ApiBilleteraVirtual> mockedApi = mockStatic(ApiBilleteraVirtual.class)) {
            pago.procesarPago(50.0, billetera);

            mockedApi.verify(() -> ApiBilleteraVirtual.validarDatos(50.0, 100.0));
            mockedApi.verify(() -> ApiBilleteraVirtual.reservarFondos(50.0, 100.0));
            mockedApi.verify(() -> ApiBilleteraVirtual.ejecutarTransaccion(50.0, 100.0));
            mockedApi.verifyNoMoreInteractions();
        }
    }

    @Test
    void procesarPago_conMontoNegativo_debeLlamarValidarDatos() {
        BilleteraVirtual billetera = new BilleteraVirtual(100.0);
        PagoPorBilleteraVirtual pago = new PagoPorBilleteraVirtual();

        try (MockedStatic<ApiBilleteraVirtual> mockedApi = mockStatic(ApiBilleteraVirtual.class)) {
            assertDoesNotThrow(() -> pago.procesarPago(-10.0, billetera));
            mockedApi.verify(() -> ApiBilleteraVirtual.validarDatos(-10.0, 100.0));
        }
    }

    @Test
    void procesarPago_conSaldoInsuficiente_debeLlamarTodasLasApis() {
        // No hay validación en esta capa, solo delega a la API
        BilleteraVirtual billetera = new BilleteraVirtual(10.0);
        PagoPorBilleteraVirtual pago = new PagoPorBilleteraVirtual();

        try (MockedStatic<ApiBilleteraVirtual> mockedApi = mockStatic(ApiBilleteraVirtual.class)) {
            pago.procesarPago(50.0, billetera);
            mockedApi.verify(() -> ApiBilleteraVirtual.validarDatos(50.0, 10.0));
            mockedApi.verify(() -> ApiBilleteraVirtual.reservarFondos(50.0, 10.0));
            mockedApi.verify(() -> ApiBilleteraVirtual.ejecutarTransaccion(50.0, 10.0));
        }
    }
}