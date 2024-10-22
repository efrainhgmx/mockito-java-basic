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
class ExamenServiceImpSpylTest {
    @Spy
    ExamenRepositoryImpl repository;

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
}