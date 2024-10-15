package org.efrain.junitapp.ejemplo.services;

import org.efrain.junitapp.ejemplo.models.Examen;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Datos {
    public final static List<Examen> EXAMENES =  Arrays.asList(new Examen(5L, "Matematicas"), new Examen(6L, "Historia"),
            new Examen(7L, "Fisica"));

    public final static List<Examen> EXAMENES_EMPTY_LIST =  Collections.emptyList();

    public final static List<String> PREGUNTAS = Arrays.asList("artimetica", "integrales", "geometria", "derivadas", "ecuaciones");

    public final static Examen EXAMEN = new Examen(null, "Fisica");

}
