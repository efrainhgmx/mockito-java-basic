package org.efrain.junitapp.ejemplo.repositores;

import org.efrain.junitapp.ejemplo.models.Examen;

import java.util.List;

public interface ExamenRepository {
    List<Examen> findAll();
    Examen guardar(Examen examen);
}
