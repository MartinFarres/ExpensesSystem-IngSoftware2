import com.interisys.business.domain.entity.Departamento;
import com.interisys.business.domain.entity.Direccion;
import com.interisys.business.domain.entity.Localidad;
import com.interisys.business.domain.entity.Pais;
import com.interisys.business.domain.entity.Propietario;
import com.interisys.business.domain.entity.Provincia;
import com.interisys.business.persistence.DAOPropietarioBean;
import com.interisys.business.persistence.NoResultDAOException;
import com.interisys.business.logic.PropietarioServiceBean;
import com.interisys.business.logic.ErrorServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.mockito.plugins.MockMaker;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PropietarioServiceBeanTest {

    @Mock
    private DAOPropietarioBean daoMock;

    @InjectMocks
    private PropietarioServiceBean propietarioService;

    private Propietario propietario;
    private Direccion direccion;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        propietario = new Propietario();
        propietario.setId("test-id");
        propietario.setNombre("Juan");
        propietario.setApellido("Pérez");
        propietario.setCorreoElectronico("juan.perez@test.com");
        propietario.setTelefono("123456789");
        propietario.setHabitaConsorcio(true);
        propietario.setEliminado(false);

        // Crear un País
        Pais pais = new Pais();
        pais.setId("PA01");
        pais.setNombre("Argentina");
        pais.setEliminado(false);

        // Crear una Provincia
        Provincia provincia = new Provincia();
        provincia.setId("PROV01");
        provincia.setNombre("Buenos Aires");
        provincia.setEliminado(false);
        provincia.setPais(pais);

        // Crear un Departamento
        Departamento departamento = new Departamento();
        departamento.setId("DEP01");
        departamento.setNombre("La Plata");
        departamento.setEliminado(false);
        departamento.setProvincia(provincia);

        // Crear una Localidad
        Localidad localidad = new Localidad();
        localidad.setId("LOC01");
        localidad.setNombre("Tolosa");
        localidad.setEliminado(false);
        localidad.setCodigoPostal("1900");
        localidad.setDepartamento(departamento);

        // Crear una Dirección
        direccion = new Direccion(); // Inicializar el objeto Direccion
        direccion.setId("DIR01");
        direccion.setCalle("Calle 1");
        direccion.setNumeracion("1234");
        direccion.setBarrio("Centro");
        direccion.setPisoCasa("Piso 3");
        direccion.setPuertaManzana("Dpto 5");
        direccion.setUbicacionCoordenadaX("-34.9214");
        direccion.setUbicacionCoordenadaY("-57.9544");
        direccion.setObservacion("Cerca de la estación de tren.");
        direccion.setLocalidad(localidad);
        direccion.setEliminado(false);
    }

    @Test
    public void testBuscarPropietarioSuccess() throws Exception {
        when(daoMock.buscarPropietario("test-id")).thenReturn(propietario);

        Propietario result = propietarioService.buscarPropietario("test-id");
        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        verify(daoMock, times(1)).buscarPropietario("test-id");
    }

    @Test(expected = ErrorServiceException.class)
    public void testBuscarPropietarioNoResult() throws Exception {
        when(daoMock.buscarPropietario("test-id")).thenThrow(new NoResultDAOException());

        propietarioService.buscarPropietario("test-id");
    }

    @Test
    public void testCrearPropietarioSuccess() throws Exception {
        when(daoMock.buscarPropietarioPorNombreApellido("Juan", "Pérez")).thenThrow(new NoResultDAOException());

        Propietario result = propietarioService.crearPropietario(
                "Juan", "Pérez", "juan.perez@test.com", "123456789", false, direccion);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        verify(daoMock, times(1)).guardarPropietario(any(Propietario.class));
    }

    @Test(expected = ErrorServiceException.class)
    public void testCrearPropietarioDuplicate() throws Exception {
        when(daoMock.buscarPropietarioPorNombreApellido("Juan", "Pérez")).thenReturn(propietario);

        propietarioService.crearPropietario("Juan", "Pérez", "juan.perez@test.com", "123456789", true, null);
    }

    @Test
    public void testModificarPropietarioSuccess() throws Exception {
        when(daoMock.buscarPropietario("test-id")).thenReturn(propietario);
        when(daoMock.buscarPropietarioPorNombreApellido("Juan", "Pérez")).thenReturn(propietario);

        propietarioService.modificarPropietario("test-id", "Juan", "Pérez", "juan.perez@test.com", "987654321", true, direccion);

        verify(daoMock, times(1)).actualizarPropietario(propietario);
        assertEquals("987654321", propietario.getTelefono());
    }

    @Test
    public void testEliminarPropietarioSuccess() throws Exception {
        when(daoMock.buscarPropietario("test-id")).thenReturn(propietario);

        propietarioService.eliminarPropietario("test-id");

        assertTrue(propietario.isEliminado());
        verify(daoMock, times(1)).actualizarPropietario(propietario);
    }
}
