package Criterio;

import Catalogo.Categoria;
import Catalogo.Producto;
import CriterioBusqueda.Criterio;
import CriterioBusqueda.CriterioAND;
import CriterioBusqueda.CriterioCategoria;
import CriterioBusqueda.CriterioNOT;
import CriterioBusqueda.CriterioNombre;
import CriterioBusqueda.CriterioOR;
import CriterioBusqueda.CriterioPrecio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CriterioTest {
    private Producto teclado;

    @BeforeEach
    void setUp() {
        teclado = new Producto(
                "TEC-001",
                "Teclado Mecanico",
                "Logitech",
                Categoria.ELECTRONICA,
                "Teclado para gaming",
                0.0,
                100.0,
                0.5
        );
    }

    @Test
    void criterioNombre_debeValidarSinDistinguirMayusculas() {
        assertTrue(new CriterioNombre("mecanico").validar(teclado));
        assertTrue(new CriterioNombre("TECLADO").validar(teclado));
        assertFalse(new CriterioNombre("mouse").validar(teclado));
    }

    @Test
    void criterioPrecio_debeValidarPrecioMaximo() {
        assertTrue(new CriterioPrecio(100.0).validar(teclado));
        assertFalse(new CriterioPrecio(99.0).validar(teclado));
    }

    @Test
    void criterioCategoria_debeValidarCategoriaExacta() {
        assertTrue(new CriterioCategoria(Categoria.ELECTRONICA).validar(teclado));
        assertFalse(new CriterioCategoria(Categoria.HOGAR).validar(teclado));
    }

    @Test
    void criterioAND_debeValidarTodosLosCriterios() {
        Criterio and = new CriterioAND();
        and.agregarCriterio(new CriterioCategoria(Categoria.ELECTRONICA));
        and.agregarCriterio(new CriterioPrecio(150.0));

        assertTrue(and.validar(teclado));

        and.agregarCriterio(new CriterioNombre("mouse"));
        assertFalse(and.validar(teclado));
    }

    @Test
    void criterioOR_debeValidarAlMenosUnCriterio() {
        Criterio or = new CriterioOR();
        or.agregarCriterio(new CriterioNombre("mouse"));
        or.agregarCriterio(new CriterioCategoria(Categoria.ELECTRONICA));

        assertTrue(or.validar(teclado));
    }

    @Test
    void criterioNOT_debeNegarElCriterioInterno() {
        Criterio not = new CriterioNOT(new CriterioCategoria(Categoria.HOGAR));

        assertTrue(not.validar(teclado));
    }
}
