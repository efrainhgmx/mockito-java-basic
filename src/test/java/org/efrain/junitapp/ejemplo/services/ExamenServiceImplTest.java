package org.efrain.junitapp.ejemplo.services;

import org.efrain.junitapp.ejemplo.models.Examen;
import org.efrain.junitapp.ejemplo.repositores.ExamenRepository;
import org.efrain.junitapp.ejemplo.repositores.ExamenRepositoryImpl;
import org.efrain.junitapp.ejemplo.repositores.PreguntasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExamenServiceImplTest {
    ExamenRepository repository;
    ExamenService service;
    PreguntasRepository preguntasRepository;

    @BeforeEach
    void setUp() {
        repository = mock(ExamenRepository.class);
        preguntasRepository = mock(PreguntasRepository.class)
        service = new ExamenServiceImpl(repository, preguntasRepository);
    }

    @Test
    void findExamenPorNombre() {
        List<Examen> datos =  Arrays.asList(new Examen(5L, "Matematicas"), new Examen(6L, "Historia"),
                new Examen(7L, "Fisica"));

        when(repository.findAll())
                .thenReturn(datos);
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");
        assertTrue(examen.isPresent());
        assertEquals(5L, examen.get().getId());
        assertEquals("Matematicas", examen.get().getNombre());
    }

    @Test
    void findExamenPorNombreEmpty() {
        List<Examen> datos =  Collections.emptyList();

        when(repository.findAll())
                .thenReturn(datos);
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");
        assertFalse(examen.isPresent());
    }
}