package pedido;

import Catalogo.Vendible;
import exceptions.ExcepcionGeneral;
import notificaciones.CambioEstadoEvento;
import notificaciones.ObservadorPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoTest {

    private Pedido pedido;
    private Vendible vendibleMock;

    @Mock
    private ObservadorPedido observadorMock;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        vendibleMock = mock(Vendible.class);
    }

    // ---------- AGREGAR OBSERVADOR ----------
    @Test
    void agregarObservador_debeAgregarYNotificarEnCambioDeEstado() {
        pedido.agregarObservador(observadorMock);
        pedido.confirmarPedido(); // esto llama a setEstado internamente

        verify(observadorMock, times(1)).onCambioEstado(any(CambioEstadoEvento.class), eq(pedido));
    }

    // ---------- SET ESTADO (notificación) ----------
    @Test
    void setEstado_debeNotificarAObservadoresConEstadoAnteriorYNuevo() {
        pedido.agregarObservador(observadorMock);

        EstadoPedido estadoAnterior = pedido.getEstado();
        EstadoPedido nuevoEstado = new Confirmado();

        pedido.setEstado(nuevoEstado);

        ArgumentCaptor<CambioEstadoEvento> captor = ArgumentCaptor.forClass(CambioEstadoEvento.class);
        verify(observadorMock, times(1)).onCambioEstado(captor.capture(), eq(pedido));

        CambioEstadoEvento evento = captor.getValue();
        assertSame(estadoAnterior, evento.getEstadoAnterior());
        assertSame(nuevoEstado, evento.getEstadoNuevo());
    }

    // ---------- AGREGAR VENDIBLE ----------
    @Test
    void agregarVendible_enBorrador_debeAgregar() {
        pedido.agregarVendible(vendibleMock);
        assertTrue(pedido.getVendibles().contains(vendibleMock));
    }

    @Test
    void agregarVendible_enEstadoNoBorrador_debeLanzarExcepcion() {
        pedido.confirmarPedido(); // pasa a Confirmado
        assertThrows(ExcepcionGeneral.class, () -> pedido.agregarVendible(vendibleMock));
    }

    // ---------- QUITAR VENDIBLE ----------
    @Test
    void quitarVendible_enBorrador_debeQuitar() {
        pedido.agregarVendible(vendibleMock);
        pedido.quitarVendible(vendibleMock);
        assertFalse(pedido.getVendibles().contains(vendibleMock));
    }

    @Test
    void quitarVendible_enEstadoNoBorrador_debeLanzarExcepcion() {
        pedido.confirmarPedido(); // Confirmado
        assertThrows(ExcepcionGeneral.class, () -> pedido.quitarVendible(vendibleMock));
    }

    // ---------- CONFIRMAR PEDIDO ----------
    @Test
    void confirmarPedido_conVendibles_debePasarAConfirmado() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        assertTrue(pedido.getEstado() instanceof Confirmado);
    }

    @Test
    void confirmarPedido_sinVendibles_debeLanzarExcepcion() {
        assertThrows(ExcepcionGeneral.class, () -> pedido.confirmarPedido());
    }

    @Test
    void confirmarPedido_desdeEstadoNoBorrador_debeLanzarExcepcion() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido(); // Confirmado
        assertThrows(ExcepcionGeneral.class, () -> pedido.confirmarPedido());
    }

    // ---------- CANCELAR PEDIDO ----------
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
    }

    @Test
    void cancelarPedido_desdeCancelado_debeLanzarExcepcion() {
        pedido.cancelarPedido();
        assertThrows(ExcepcionGeneral.class, () -> pedido.cancelarPedido());
    }

    // ---------- PASAR A EN PREPARACION ----------
    @Test
    void pasarAEnPreparacion_desdeConfirmado_debePasarAEnPreparacion() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        pedido.pasarAEnPreparacion();
        assertTrue(pedido.getEstado() instanceof EnPreparacion);
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

    // ---------- PASAR A ENVIADO ----------
    @Test
    void pasarAEnviado_desdeEnPreparacion_debePasarAEnviado() {
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        pedido.pasarAEnPreparacion();
        pedido.pasarAEnviado();
        assertTrue(pedido.getEstado() instanceof Enviado);
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

    // ---------- PASAR A ENTREGADO ----------
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

    // ---------- GETTERS ----------
    @Test
    void getEstado_debeRetornarEstadoActual() {
        assertTrue(pedido.getEstado() instanceof Borrador);
        pedido.agregarVendible(vendibleMock);
        pedido.confirmarPedido();
        assertTrue(pedido.getEstado() instanceof Confirmado);
    }

    @Test
    void getVendibles_debeRetornarCopiaDeLaLista() {
        pedido.agregarVendible(vendibleMock);
        List<Vendible> lista = pedido.getVendibles();
        assertTrue(lista.contains(vendibleMock));
        // Verificar que es una copia (no la misma referencia)
        assertNotSame(pedido.getVendibles(), lista);
        // Añadir a la lista devuelta no debe afectar al pedido
        Vendible otro = mock(Vendible.class);
        lista.add(otro);
        assertFalse(pedido.getVendibles().contains(otro));
    }
}