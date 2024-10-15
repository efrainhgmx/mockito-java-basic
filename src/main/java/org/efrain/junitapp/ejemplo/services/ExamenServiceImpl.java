package org.efrain.junitapp.ejemplo.services;

import org.efrain.junitapp.ejemplo.models.Examen;
import org.efrain.junitapp.ejemplo.repositores.ExamenRepository;
import org.efrain.junitapp.ejemplo.repositores.PreguntasRepository;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements ExamenService{
    private ExamenRepository examenRepository;
    private PreguntasRepository preguntasRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository, PreguntasRepository preguntasRepository) {
        this.examenRepository = examenRepository;
        this.preguntasRepository = preguntasRepository;
    }

    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {
       return examenRepository.findAll()
                .stream()
                .filter(e -> nombre.equals(e.getNombre())).findFirst();
    }

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre) {
        Examen examen = null;
        Optional<Examen> examenOptional = findExamenPorNombre(nombre);
        if(examenOptional.isPresent()){
            examen = examenOptional.orElseThrow();
            List<String> preguntas = preguntasRepository.findPreguntasByExamenId(examen.getId());
            examen.setPreguntas(preguntas);
        }
        return examen;
    }

    @Override
    public Examen guardar(Examen examen) {
        if(!examen.getPreguntas().isEmpty()){
            preguntasRepository.guardarVarias(examen.getPreguntas());
        }
        return examenRepository.guardar(examen);
    }
}
