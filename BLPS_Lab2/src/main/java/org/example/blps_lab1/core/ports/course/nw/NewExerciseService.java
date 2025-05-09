package org.example.blps_lab1.core.ports.course.nw;

import org.example.blps_lab1.adapters.course.dto.nw.NewExerciseDto;
import org.example.blps_lab1.core.domain.course.nw.NewExercise;

import java.util.List;
import java.util.UUID;

public interface NewExerciseService {

    /**
     * Создает упражнение. Важно, упражнение НИКАК не привязано модулям, чтобы любой модуль
     * мог переиспользовать любые упражнения
     *
     * @param exerciseDto объект типа {@link NewExerciseDto}, все поля обязательны
     * @return {@link NewExercise}
     */
    NewExercise createNewExercise(final NewExerciseDto exerciseDto);

    /**
     * Возвращает именно упражнение без привязки к какому-либо модулю. Данный метод во многом утилитарный для админов
     *
     * @param uuid упражнения
     * @return {@link NewExercise}
     */
    NewExercise getNewExerciseById(final UUID uuid);

    void deleteNewExercise(final Long id);

    List<NewExercise> getAllExercises();

    NewExercise updateNewExercise(Long id, NewExerciseDto exerciseDto);

    Boolean submitAnswer(Long exerciseId, String userAnswer);
}
