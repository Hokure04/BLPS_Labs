package org.example.blps_lab1.adapters.course.service.nw;

import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.adapters.course.dto.nw.NewExerciseDto;
import org.example.blps_lab1.adapters.course.mapper.NewExerciseMapper;
import org.example.blps_lab1.adapters.db.course.NewExerciseRepository;
import org.example.blps_lab1.adapters.db.course.NewModuleRepository;
import org.example.blps_lab1.adapters.db.course.StudentRepository;
import org.example.blps_lab1.core.domain.course.nw.NewExercise;
import org.example.blps_lab1.core.exception.course.InvalidFieldException;
import org.example.blps_lab1.core.exception.course.NotExistException;
import org.example.blps_lab1.core.ports.auth.AuthService;
import org.example.blps_lab1.core.ports.course.nw.NewExerciseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class NewExerciseServiceImpl implements NewExerciseService {
    private final NewExerciseRepository newExerciseRepository;
    private final PlatformTransactionManager transactionManager;
    private final NewModuleRepository newModuleRepository;
    private final StudentRepository studentRepository;
    private final AuthService authService;

    public NewExerciseServiceImpl(NewExerciseRepository newExerciseRepository, PlatformTransactionManager transactionManager, NewModuleRepository newModuleRepository, StudentRepository studentRepository, AuthService authService) {
        this.newExerciseRepository = newExerciseRepository;
        this.transactionManager = transactionManager;
        this.newModuleRepository = newModuleRepository;
        this.studentRepository = studentRepository;
        this.authService = authService;
    }

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
            log.warn("exercise dto somehow is nil");
            throw new InvalidFieldException("Не хватает информации для создания упражнения");
        }
        if (exerciseDto.getName().isEmpty()
                || exerciseDto.getDescription().isEmpty()
                || exerciseDto.getAnswer().isEmpty()
                || exerciseDto.getPoints() == null
        ) {
            log.warn("user not specified some of the fields");
            throw new InvalidFieldException("Поля name, description, answer и points обязательны");
        }
        if (exerciseDto.getPoints() < 0) {
            log.warn("user try to specify negative value for points: {}", exerciseDto.getPoints());
            throw new InvalidFieldException("Поля points должно быть целым положительным числом");
        }

        return newExerciseRepository.save(
                NewExerciseMapper.toEntity(exerciseDto)
        );
    }

    /**
     * Возвращает именно упражнение без привязки к какому-либо модулю.
     * Данный метод во многом утилитарный для админских ролей
     *
     * @param uuid упражнения
     * @return {@link NewExercise}
     */
    @Override
    public NewExercise getNewExerciseByUUID(UUID uuid) {
        return newExerciseRepository.findById(uuid).orElseThrow(() -> new NotExistException("упражнения с uuid: " + uuid + " не существует"));
    }


    @Override
    public void deleteNewExercise(UUID uuid) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setName("deleteNewExercise");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(definition);
        try{
            var exercise = newExerciseRepository.findById(uuid).orElseThrow(() -> new NotExistException("Упражнения с uuid: " + uuid + " не существует"));

            studentRepository.removeByFinishedExercises(List.of(exercise));


            newModuleRepository.removeByExercises(List.of(exercise));
            newExerciseRepository.delete(exercise);
            transactionManager.commit(status);

        }catch (Exception e){
            transactionManager.rollback(status);
            throw e;
        }
    }

    /**
     * Возвращает именно упражнения без привязки к какому-либо модулю. Данный метод во многом утилитарный для админов
     *
     * @return упражнения {@link NewExercise}
     */
    @Override
    public List<NewExercise> getAllExercises() {
        return newExerciseRepository.findAll();
    }

    /**
     * Обновляет указанное упраженение. Важно, данный метод обновит упражнение и все модули, которые на него ссылаются
     * также обновятся
     *
     * @param uuid        курса, который обновляем
     * @param exerciseDto новые данные курса. Важно, данные должны быть полными
     * @return новое обновленное упражнение
     */
    @Override
    public NewExercise updateNewExercise(UUID uuid, NewExerciseDto exerciseDto) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setName("updateNewTransaction");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(definition);
        try{
            var exerciseToUpdate = newExerciseRepository.findById(uuid).orElseThrow(() -> new NotExistException("Упражнения с таким uuid не существует"));
            var toSave = NewExerciseMapper.toEntity(exerciseDto);
            toSave.setUuid(exerciseToUpdate.getUuid());
            transactionManager.commit(status);
            return newExerciseRepository.save(toSave);
        }catch (Exception e){
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public Boolean submitAnswer(UUID exerciseUUID, String userAnswer) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setName("submitAnswerForExercise");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(definition);
        try{
            var exercise = newExerciseRepository.findById(exerciseUUID).orElseThrow(() -> new NotExistException("упражнения с uuid: " + exerciseUUID + " не существует"));
            var userID = authService.getCurrentUser().getId();

            var student = studentRepository.findByUsid(userID).orElseThrow(() -> new NotExistException("Пользователь временно недоступен для операций"));
            if (exercise.getAnswer().equals(userAnswer)) {
                student.getFinishedExercises().add(exercise);
                transactionManager.commit(status);
                return true;
            }
            transactionManager.commit(status);
            return false;
        }catch (Exception e){
            transactionManager.rollback(status);
            throw e;
        }
    }
}
