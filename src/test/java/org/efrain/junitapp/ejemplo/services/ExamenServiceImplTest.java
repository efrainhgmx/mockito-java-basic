package org.efrain.junitapp.ejemplo.services;

import org.efrain.junitapp.ejemplo.models.Examen;
import org.efrain.junitapp.ejemplo.repositores.ExamenRepository;
import org.efrain.junitapp.ejemplo.repositores.ExamenRepositoryImpl;
import org.efrain.junitapp.ejemplo.repositores.PreguntasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
                .thenReturn(Datos.DATOS);
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");
        assertTrue(examen.isPresent());
        assertEquals(5L, examen.get().getId());
        assertEquals("Matematicas", examen.get().getNombre());
    }

    @Test
    void findExamenPorNombreEmpty() {

        when(repository.findAll())
                .thenReturn(Datos.DATOS_EMPTY);
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");
        assertFalse(examen.isPresent());
    }

    @Test
    void testPreguntasExamen() {
        when(repository.findAll()).thenReturn(Datos.DATOS);
        when(preguntasRepository.findPreguntasByExamenId(5L)).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("ecuaciones"));
    }

    @Test
    void testPreguntasExamenVerify() {
        when(repository.findAll()).thenReturn(Datos.DATOS);
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
        when(repository.findAll()).thenReturn(Datos.DATOS_EMPTY);
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
        when(repository.findAll()).thenReturn(Datos.DATOS);
        when(preguntasRepository.findPreguntasByExamenId(anyLong())).thenThrow(IllegalArgumentException.class);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findExamenPorNombreConPreguntas("Matematicas");
        });

        assertEquals(IllegalArgumentException.class, exception.getClass());
    }
}