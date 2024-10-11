package org.efrain.junitapp.ejemplo.repositores;

import org.efrain.junitapp.ejemplo.models.Examen;

import java.util.Arrays;
import java.util.List;

public class ExamenRepositoryImpl implements ExamenRepository{
    @Override
    public List<Examen> findAll() {
        return Arrays.asList(new Examen(5L, "Matematicas"), new Examen(6L, "Historia"),
                new Examen(7L, "Fisica"));
    }
}
