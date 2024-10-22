package org.efrain.junitapp.ejemplo.services;

import org.efrain.junitapp.ejemplo.models.Examen;
import org.efrain.junitapp.ejemplo.repositores.ExamenRepository;
import org.efrain.junitapp.ejemplo.repositores.ExamenRepositoryImpl;
import org.efrain.junitapp.ejemplo.repositores.PreguntasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {
    @Mock
    ExamenRepository repository;

    @Mock
    PreguntasRepository preguntasRepository;

    @InjectMocks
    ExamenServiceImpl service;

    @Captor
    ArgumentCaptor<Long> captor;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
        /*repository = mock(ExamenRepository.class);
        preguntasRepository = mock(PreguntasRepository.class);
        service = new ExamenServiceImpl(repository, preguntasRepository);*/

    }

    @Test
    void findExamenPorNombre() {

        when(repository.findAll())
                .thenReturn(Datos.EXAMENES);
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");
        assertTrue(examen.isPresent());
        assertEquals(5L, examen.get().getId());
        assertEquals("Matematicas", examen.get().getNombre());
    }

    @Test
    void findExamenPorNombreEmpty() {

        when(repository.findAll())
                .thenReturn(Datos.EXAMENES_EMPTY_LIST);
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");
        assertFalse(examen.isPresent());
    }

    @Test
    void testPreguntasExamen() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntasRepository.findPreguntasByExamenId(5L)).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("ecuaciones"));
    }

    @Test
    void testPreguntasExamenVerify() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntasRepository.findPreguntasByExamenId(5L)).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("ecuaciones"));
        /*
        * verify: Verifica si se ejecuta el método mencionado
        * en caso de no serlo, el test falla
        * */
        verify(repository).findAll();
        verify(preguntasRepository).findPreguntasByExamenId(anyLong());
    }

    @Test
    void testNotExistExamen() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES_EMPTY_LIST);
        when(preguntasRepository.findPreguntasByExamenId(5L)).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Computacion");
        assertNull(examen);
        verify(repository).findAll();
        verify(preguntasRepository).findPreguntasByExamenId(anyLong());
    }

    @Test
    void testGuardarExamen() {
        //?Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);
        //**Simula un autoincremento de id
        when(repository.guardar(any(Examen.class))).then(new Answer<Examen>(){
            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });

        //*WHEN
        Examen examen = service.guardar(newExamen);

        //!THEN
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());
        verify(repository).guardar(any(Examen.class));
    }

    @Test
    void testManejoException() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntasRepository.findPreguntasByExamenId(anyLong())).thenThrow(IllegalArgumentException.class);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findExamenPorNombreConPreguntas("Matematicas");
        });

        assertEquals(IllegalArgumentException.class, exception.getClass());
    }

    @Test
    void testArgumentMatchers() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntasRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(repository).findAll();
        verify(preguntasRepository).findPreguntasByExamenId(argThat(arg -> arg != null && arg.equals(5L)));
        //*eq es elargumentMatchers equals
        verify(preguntasRepository).findPreguntasByExamenId(eq(5L));
    }

    @Test
    void testArgumentMyMatchers() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES_NEGATIVOS);
        when(preguntasRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenPorNombreConPreguntas("Fisica");

        verify(repository).findAll();
        verify(preguntasRepository).findPreguntasByExamenId(argThat(new MiArgsMatchers()));
    }


    public static class MiArgsMatchers implements ArgumentMatcher<Long> {
        @Override
        public boolean matches(Long argument) {
            return argument !=null && argument > 0;
        }

        @Override
        public String toString() {
            return "es para un mensjae personalizado de error " +
                    "que imprime mockito en caso de que falle el test" +
                    " Debe ser un entero positivo";
        }
    }

    @Test
    void testArgumentCaptor() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntasRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenPorNombreConPreguntas("Fisica");

        //*ArgumentCapture, ayuda a capturar el argumento del método.
        //ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(preguntasRepository).findPreguntasByExamenId(captor.capture());

        assertEquals(7L, captor.getValue());
    }

    @Test
    void testDoThrow() {
        //*Se utiliza cuando el metodo es void y no retorna nada
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);
        doThrow(IllegalArgumentException.class).when(preguntasRepository).guardarVarias(anyList());

        assertThrows(IllegalArgumentException.class, () -> {
            service.guardar(examen);
        });
    }

    /*
    * doAnswer es otra manera de poder dar GIVEN
    * Datos de prueba o simular ciertos escenarios
    * */
    @Test
    void testDoAnswer() {
        //?GIVEN
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        //when(preguntasRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 5L ? Datos.PREGUNTAS : null;
        }).when(preguntasRepository).findPreguntasByExamenId(anyLong());

        //*WHEN
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        //!THEN
        verify(preguntasRepository).findPreguntasByExamenId(anyLong());
        assertEquals(5, examen.getPreguntas().size());
        assertEquals(5L, examen.getId());

    }

    @Test
    void testDoAnswerGuardarExamen() {
        //?Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        //**Simula un autoincremento de id co doAnswer
        doAnswer(new Answer<Examen>(){
            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        }).when(repository).guardar(any(Examen.class));

        //*WHEN
        Examen examen = service.guardar(newExamen);

        //!THEN
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());
        verify(repository).guardar(any(Examen.class));
    }

    /*
    * Los spies, usan los métodos reales pero puedes tambien
    * simular algunos métodos.
    * Este debe ejeuctar la implementacion y no la interfaz
    * */
    @Test
    void testSpy() {
        ExamenRepository examenRepository = spy(ExamenRepositoryImpl.class);
        PreguntasRepository preguntasRepository = spy(PreguntasRepository.class);

        ExamenServiceImpl examenService =  new ExamenServiceImpl(examenRepository, preguntasRepository);

        //when(preguntasRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        //*Evita que se ejecute el metodo real findPreguntasByExamenId
        doReturn(Datos.PREGUNTAS).when(preguntasRepository).findPreguntasByExamenId(anyLong());

        Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertTrue(examen.getPreguntas().contains("derivadas"));
    }


    /*
    * Verifica si se ejecuta en el orden
    * especificado
    * */
    @Test
    void testOrdenInvocaciones() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);

        service.findExamenPorNombreConPreguntas("Matematicas");
        service.findExamenPorNombreConPreguntas("Fisica");

        InOrder inOrder = inOrder(repository, preguntasRepository);
        //**Matematicas
        inOrder.verify(repository).findAll();
        inOrder.verify(preguntasRepository).findPreguntasByExamenId(5L);

        //**Fisica
        inOrder.verify(repository).findAll();
        inOrder.verify(preguntasRepository).findPreguntasByExamenId(7L);
    }

    @Test
    void testNumeroInvocaciones() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        service.findExamenPorNombreConPreguntas("Matematicas");


        verify(preguntasRepository).findPreguntasByExamenId(5L);
        //**POR DEFECTO ES TIMES(1)
        verify(preguntasRepository, times(1)).findPreguntasByExamenId(5L);
        verify(preguntasRepository, times(2)).findPreguntasByExamenId(5L);
        //**Al menos N veces
        verify(preguntasRepository, atLeast(1)).findPreguntasByExamenId(5L);
        verify(preguntasRepository, atLeastOnce()).findPreguntasByExamenId(5L);
        //**A lo mucho N veces
        verify(preguntasRepository, atMost(10)).findPreguntasByExamenId(5L);
        verify(preguntasRepository, atMostOnce()).findPreguntasByExamenId(5L);
    }

    @Test
    void testNumeroInvocacionesDos() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);

        service.findExamenPorNombreConPreguntas("Fisicaa");
        //**NUNCA SE INVOCA
        verify(preguntasRepository, never()).findPreguntasByExamenId(anyLong());
        verifyNoInteractions(preguntasRepository);
    }
}