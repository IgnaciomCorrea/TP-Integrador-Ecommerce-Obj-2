package pedido;

import Catalogo.Vendible;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import exceptions.ExcepcionGeneral;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PedidoTest {

    private Pedido pedido;

    @Mock
    private Vendible vendibleMock;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
    }

    @Test
    void nuevoPedido_debeEstarEnBorrador() {
        assertTrue(pedido.getEstado() instanceof Borrador);
    }

    @Test
    void agregarVendible_enBorrador_debeAgregar() {
        pedido.agregarVendible(vendibleMock);
        assertTrue(pedido.getVendibles().contains(vendibleMock));
    }

    @Test
    void quitarVendible_enBorrador_debeQuitar() {
        pedido.agregarVendible(vendibleMock);
        pedido.quitarVendible(vendibleMock);
        assertFalse(pedido.getVendibles().contains(vendibleMock));
    }

    @Test
    void quitarVendible_inexistente_enBorrador_noLanzaExcepcion() {
        assertDoesNotThrow(() -> pedido.quitarVendible(vendibleMock));
    }

    @Test
    void confirmarPedido_conVendibles_debePasarAConfirmado() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        assertTrue(pedido.getEstado() instanceof Confirmado);
    }

    @Test
    void confirmarPedido_sinVendibles_debeLanzarExcepcion() {
        assertThrows(ExcepcionGeneral.class, () -> pedido.confirmarPedido());
        assertTrue(pedido.getEstado() instanceof Borrador);
    }

    @Test
    void confirmarPedido_desdeConfirmado_debeLanzarExcepcion() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        assertThrows(ExcepcionGeneral.class, () -> pedido.confirmarPedido());
    }

    @Test
    void cancelarPedido_desdeBorrador_debePasarACancelado() {
        pedido.cancelarPedido();
        assertTrue(pedido.getEstado() instanceof Cancelado);
    }

    @Test
    void cancelarPedido_desdeConfirmado_debePasarACancelado() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        pedido.cancelarPedido();
        assertTrue(pedido.getEstado() instanceof Cancelado);
    }

    @Test
    void cancelarPedido_desdeEnPreparacion_debePasarACancelado() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        pedido.pasarAEnPreparacion();
        pedido.cancelarPedido();
        assertTrue(pedido.getEstado() instanceof Cancelado);
    }

    @Test
    void cancelarPedido_desdeEnviado_debePasarACancelado() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        pedido.pasarAEnPreparacion();
        pedido.pasarAEnviado();
        pedido.cancelarPedido();
        assertTrue(pedido.getEstado() instanceof Cancelado);
    }

    @Test
    void cancelarPedido_desdeEntregado_debeLanzarExcepcion() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        pedido.pasarAEnPreparacion();
        pedido.pasarAEnviado();
        pedido.pasarAEntregado();
        assertThrows(ExcepcionGeneral.class, () -> pedido.cancelarPedido());
        assertTrue(pedido.getEstado() instanceof Entregado);
    }

    @Test
    void cancelarPedido_desdeCancelado_debeLanzarExcepcion() {
        pedido.cancelarPedido();
        assertThrows(ExcepcionGeneral.class, () -> pedido.cancelarPedido());
    }

    @Test
    void pasarAEnPreparacion_desdeConfirmado_debePasarAEnPreparacion() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        pedido.pasarAEnPreparacion();
        assertTrue(pedido.getEstado() instanceof EnPreparacion);
    }

    @Test
    void pasarAEnviado_desdeEnPreparacion_debePasarAEnviado() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        pedido.pasarAEnPreparacion();
        pedido.pasarAEnviado();
        assertTrue(pedido.getEstado() instanceof Enviado);
    }

    @Test
    void pasarAEntregado_desdeEnviado_debePasarAEntregado() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        pedido.pasarAEnPreparacion();
        pedido.pasarAEnviado();
        pedido.pasarAEntregado();
        assertTrue(pedido.getEstado() instanceof Entregado);
    }

    @Test
    void pasarAEnPreparacion_desdeBorrador_debeLanzarExcepcion() {
        assertThrows(ExcepcionGeneral.class, () -> pedido.pasarAEnPreparacion());
    }

    @Test
    void pasarAEnPreparacion_desdeEnPreparacion_debeLanzarExcepcion() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        pedido.pasarAEnPreparacion();
        assertThrows(ExcepcionGeneral.class, () -> pedido.pasarAEnPreparacion());
    }

    @Test
    void pasarAEnPreparacion_desdeEnviado_debeLanzarExcepcion() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        pedido.pasarAEnPreparacion();
        pedido.pasarAEnviado();
        assertThrows(ExcepcionGeneral.class, () -> pedido.pasarAEnPreparacion());
    }

    @Test
    void pasarAEnviado_desdeBorrador_debeLanzarExcepcion() {
        assertThrows(ExcepcionGeneral.class, () -> pedido.pasarAEnviado());
    }

    @Test
    void pasarAEnviado_desdeConfirmado_debeLanzarExcepcion() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        assertThrows(ExcepcionGeneral.class, () -> pedido.pasarAEnviado());
    }

    @Test
    void pasarAEnviado_desdeEnviado_debeLanzarExcepcion() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        pedido.pasarAEnPreparacion();
        pedido.pasarAEnviado();
        assertThrows(ExcepcionGeneral.class, () -> pedido.pasarAEnviado());
    }

    @Test
    void pasarAEntregado_desdeBorrador_debeLanzarExcepcion() {
        assertThrows(ExcepcionGeneral.class, () -> pedido.pasarAEntregado());
    }

    @Test
    void pasarAEntregado_desdeConfirmado_debeLanzarExcepcion() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        assertThrows(ExcepcionGeneral.class, () -> pedido.pasarAEntregado());
    }

    @Test
    void pasarAEntregado_desdeEnPreparacion_debeLanzarExcepcion() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        pedido.pasarAEnPreparacion();
        assertThrows(ExcepcionGeneral.class, () -> pedido.pasarAEntregado());
    }

    @Test
    void pasarAEntregado_desdeEntregado_debeLanzarExcepcion() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        pedido.pasarAEnPreparacion();
        pedido.pasarAEnviado();
        pedido.pasarAEntregado();
        assertThrows(ExcepcionGeneral.class, () -> pedido.pasarAEntregado());
    }

    @Test
    void operacionesEnCancelado_debenLanzarExcepcion() {
        pedido.cancelarPedido();
        assertThrows(ExcepcionGeneral.class, () -> pedido.agregarVendible(vendibleMock));
        assertThrows(ExcepcionGeneral.class, () -> pedido.quitarVendible(vendibleMock));
        assertThrows(ExcepcionGeneral.class, () -> pedido.confirmarPedido());
        assertThrows(ExcepcionGeneral.class, () -> pedido.pasarAEnPreparacion());
        assertThrows(ExcepcionGeneral.class, () -> pedido.pasarAEnviado());
        assertThrows(ExcepcionGeneral.class, () -> pedido.pasarAEntregado());
    }

    @Test
    void operacionesEnEntregado_debenLanzarExcepcion() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        pedido.pasarAEnPreparacion();
        pedido.pasarAEnviado();
        pedido.pasarAEntregado();

        assertThrows(ExcepcionGeneral.class, () -> pedido.agregarVendible(vendibleMock));
        assertThrows(ExcepcionGeneral.class, () -> pedido.quitarVendible(vendibleMock));
        assertThrows(ExcepcionGeneral.class, () -> pedido.confirmarPedido());
        assertThrows(ExcepcionGeneral.class, () -> pedido.cancelarPedido());
        assertThrows(ExcepcionGeneral.class, () -> pedido.pasarAEnPreparacion());
        assertThrows(ExcepcionGeneral.class, () -> pedido.pasarAEnviado());
        assertThrows(ExcepcionGeneral.class, () -> pedido.pasarAEntregado());
    }

    @Test
    void getVendibles_debeRetornarListaConLosAgregados() {
        pedido.agregarVendible(vendibleMock);
        assertTrue(pedido.getVendibles().contains(vendibleMock));
        assertEquals(1, pedido.getVendibles().size());
    }

    @Test
    void getEstado_debeRetornarEstadoActual() {
        assertTrue(pedido.getEstado() instanceof Borrador);
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        assertTrue(pedido.getEstado() instanceof Confirmado);
    }
}