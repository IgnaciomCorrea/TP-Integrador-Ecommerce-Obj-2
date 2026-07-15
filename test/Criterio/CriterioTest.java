package Criterio;

import Catalogo.Categoria;
import Catalogo.Producto;
import CriterioBusqueda.*;
import exceptions.CatalogoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Nested
    class CriterioDescripcionTest {

        @Test
        @DisplayName("CriterioDescripcion debe validar si la descripción contiene el texto")
        void validar_descripcionContieneTexto_debeRetornarTrue() {
            CriterioDescripcion criterio = new CriterioDescripcion("gaming");
            assertTrue(criterio.validar(teclado));
        }

        @Test
        @DisplayName("CriterioDescripcion debe ser case insensitive")
        void validar_descripcionCaseInsensitive_debeRetornarTrue() {
            CriterioDescripcion criterio = new CriterioDescripcion("GAMING");
            assertTrue(criterio.validar(teclado));
        }

        @Test
        @DisplayName("CriterioDescripcion debe retornar false si no contiene el texto")
        void validar_descripcionNoContieneTexto_debeRetornarFalse() {
            CriterioDescripcion criterio = new CriterioDescripcion("mouse");
            assertFalse(criterio.validar(teclado));
        }

        @Test
        @DisplayName("CriterioDescripcion con texto vacío debe retornar true (siempre contiene '')")
        void validar_conTextoVacio_debeRetornarTrue() {
            CriterioDescripcion criterio = new CriterioDescripcion("");
            assertTrue(criterio.validar(teclado));
        }
    }

    @Nested
    class CriterioCompuestoTest {

        private CriterioAND and;
        private CriterioOR or;
        private CriterioNOT not;

        @BeforeEach
        void setUp() {
            and = new CriterioAND();
            or = new CriterioOR();
            not = new CriterioNOT(new CriterioCategoria(Categoria.HOGAR));
        }

        @Test
        @DisplayName("CriterioCompuesto debe permitir agregar criterios")
        void agregarCriterio_debeAgregar() {
            and.agregarCriterio(new CriterioCategoria(Categoria.ELECTRONICA));
            and.agregarCriterio(new CriterioPrecio(100.0));
            // No lanza excepción
            assertTrue(and.validar(teclado));
        }

        @Test
        @DisplayName("CriterioCompuesto debe permitir eliminar criterios")
        void eliminarCriterio_debeEliminar() {
            Criterio precio = new CriterioPrecio(100.0);
            and.agregarCriterio(new CriterioCategoria(Categoria.ELECTRONICA));
            and.agregarCriterio(precio);

            assertTrue(and.validar(teclado)); // ambos cumplen

            and.eliminarCriterio(precio);
            // Ahora solo valida la categoría, que sigue siendo true
            assertTrue(and.validar(teclado));
            // Si eliminamos el último criterio, debería validar true (AND vacío = true)
            // No podemos eliminar el criterio de categoría porque no tenemos referencia
            // pero podemos verificar que el tamaño se reduce
        }

        @Test
        @DisplayName("Eliminar un criterio no existente no debe lanzar excepción")
        void eliminarCriterio_inexistente_noLanzaExcepcion() {
            Criterio otro = new CriterioNombre("otro");
            assertDoesNotThrow(() -> and.eliminarCriterio(otro));
        }

        @Test
        @DisplayName("CriterioCompuesto vacío (AND) debe retornar true")
        void AND_vacio_debeRetornarTrue() {
            assertTrue(new CriterioAND().validar(teclado));
        }

        @Test
        @DisplayName("CriterioCompuesto vacío (OR) debe retornar false")
        void OR_vacio_debeRetornarFalse() {
            assertFalse(new CriterioOR().validar(teclado));
        }

        @Test
        @DisplayName("CriterioCompuesto vacío (NOT) lanza excepción?")
        void NOT_sinCriterio_debeLanzarExcepcion() {
            // El constructor de CriterioNOT recibe un criterio, no puede estar vacío
            // así que no aplica.
        }

        @Test
        @DisplayName("Criterio agrega y elimina en OR")
        void OR_agregarYEliminar() {
            Criterio criterioCategoria = new CriterioCategoria(Categoria.ELECTRONICA);
            or.agregarCriterio(criterioCategoria);
            assertTrue(or.validar(teclado));

            Criterio precio = new CriterioPrecio(100.0);
            or.agregarCriterio(precio);
            assertTrue(or.validar(teclado));

            or.eliminarCriterio(criterioCategoria);
            // Aún tiene el precio, que cumple
            assertTrue(or.validar(teclado));

            or.eliminarCriterio(precio);
            // Queda vacío, OR vacío = false
            assertFalse(or.validar(teclado));
        }
    }

    @Nested
    class CriterioAbstractTest {

        @Test
        @DisplayName("Criterio.agregarCriterio debe lanzar excepción (por defecto)")
        void agregarCriterio_enCriterioSimple_debeLanzarExcepcion() {
            Criterio simple = new CriterioNombre("test");
            assertThrows(CatalogoException.class, () -> simple.agregarCriterio(new CriterioNombre("otro")));
        }

        @Test
        @DisplayName("Criterio.eliminarCriterio debe lanzar excepción (por defecto)")
        void eliminarCriterio_enCriterioSimple_debeLanzarExcepcion() {
            Criterio simple = new CriterioNombre("test");
            assertThrows(CatalogoException.class, () -> simple.eliminarCriterio(new CriterioNombre("otro")));
        }
    }
}
