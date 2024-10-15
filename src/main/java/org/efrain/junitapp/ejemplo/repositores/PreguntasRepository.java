package org.efrain.junitapp.ejemplo.repositores;

import java.util.List;

public interface PreguntasRepository {
    List<String> findPreguntasByExamenId(Long id);
    void guardarVarias(List<String> preguntas);
}
