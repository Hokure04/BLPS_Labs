package org.example.blps_lab1.adapters.course.service.nw;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.adapters.course.dto.nw.NewExerciseDto;
import org.example.blps_lab1.adapters.course.mapper.NewExerciseMapper;
import org.example.blps_lab1.adapters.db.course.NewExerciseRepository;
import org.example.blps_lab1.core.domain.course.nw.NewExercise;
import org.example.blps_lab1.core.exception.course.InvalidFieldException;
import org.example.blps_lab1.core.exception.course.NotExistException;
import org.example.blps_lab1.core.ports.course.nw.NewExerciseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class NewExerciseServiceImpl implements NewExerciseService {
    private NewExerciseRepository newExerciseRepository;

    /**
     * Создает упражнение. Важно, упражнение НИКАК не привязано модулям, чтобы любой модуль
     * мог переиспользовать любые упражнения
     *
     * @param exerciseDto объект типа {@link NewExerciseDto}, все поля обязательны
     * @return {@link NewExercise}
     */
    @Override
    public NewExercise createNewExercise(NewExerciseDto exerciseDto) {
        if (exerciseDto == null) {
            log.error("exercise dto somehow is nil");
            throw new InvalidFieldException("Не хватает информации для создания курса");
        }
        if (exerciseDto.getName().isEmpty()
                || exerciseDto.getDescription().isEmpty()
                || exerciseDto.getAnswer().isEmpty()
                || exerciseDto.getDifficultyLevel() == null
        ) {
            log.error("user not specified some of the fields");
            throw new InvalidFieldException("Поля name, description, answer и difficultyLevel обязательны");
        }

        return newExerciseRepository.save(
                NewExerciseMapper.toEntity(exerciseDto)
        );
    }

    /**
     * Возвращает именно упражнение без привязки к какому-либо модулю.
     *  Данный метод во многом утилитарный для админских ролей
     *
     * @param uuid упражнения
     * @return {@link NewExercise}
     */
    @Override
    public NewExercise getNewExerciseById(UUID uuid) {
        return newExerciseRepository.findById(uuid).orElseThrow(() -> new NotExistException("упражнения с uuid: " + uuid + " не существует"));
    }



    @Override
    public void deleteNewExercise(Long id) {

    }

    @Override
    public List<NewExercise> getAllExercises() {
        return List.of();
    }

    @Override
    public NewExercise updateNewExercise(Long id, NewExerciseDto exerciseDto) {
        return null;
    }

    @Override
    public Boolean submitAnswer(Long exerciseId, String userAnswer) {
        return null;
    }
}
