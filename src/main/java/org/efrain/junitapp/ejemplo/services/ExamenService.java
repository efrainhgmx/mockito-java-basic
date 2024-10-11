package org.efrain.junitapp.ejemplo.services;

import org.efrain.junitapp.ejemplo.models.Examen;

public interface ExamenService {
    Examen findExamenPorNombre(String nombre);
}
