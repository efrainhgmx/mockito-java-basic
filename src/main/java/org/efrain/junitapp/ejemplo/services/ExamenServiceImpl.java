package org.efrain.junitapp.ejemplo.services;

import org.efrain.junitapp.ejemplo.models.Examen;
import org.efrain.junitapp.ejemplo.repositores.ExamenRepository;

import java.util.Optional;

public class ExamenServiceImpl implements ExamenService{
    private ExamenRepository examenRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    @Override
    public Examen findExamenPorNombre(String nombre) {
        Optional<Examen> examenOptinal= examenRepository.findAll()
                .stream()
                .filter(e -> nombre.equals(e.getNombre())).findFirst();

        Examen examen = null;
        if (examenOptinal.isPresent()){
            examen = examenOptinal.orElseThrow();
        }
        return examen;
    }
}
