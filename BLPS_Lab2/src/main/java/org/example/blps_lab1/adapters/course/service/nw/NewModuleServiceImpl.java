package org.example.blps_lab1.adapters.course.service.nw;

import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.adapters.course.dto.nw.NewModuleDto;
import org.example.blps_lab1.adapters.course.mapper.NewModuleMapper;
import org.example.blps_lab1.adapters.db.course.NewCourseRepository;
import org.example.blps_lab1.adapters.db.course.NewExerciseRepository;
import org.example.blps_lab1.adapters.db.course.NewModuleRepository;
import org.example.blps_lab1.adapters.db.course.StudentRepository;
import org.example.blps_lab1.core.domain.course.nw.NewExercise;
import org.example.blps_lab1.core.domain.course.nw.NewModule;
import org.example.blps_lab1.core.exception.course.InvalidFieldException;
import org.example.blps_lab1.core.exception.course.NotExistException;
import org.example.blps_lab1.core.ports.auth.AuthService;
import org.example.blps_lab1.core.ports.course.nw.NewModuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NewModuleServiceImpl implements NewModuleService {

    private final NewModuleRepository newModuleRepository;
    private final NewExerciseRepository newExerciseRepository;

    private final PlatformTransactionManager transactionManager;
    private final NewCourseRepository newCourseRepository;
    private final AuthService authService;
    private final StudentRepository studentRepository;

    public NewModuleServiceImpl(NewModuleRepository newModuleRepository, NewExerciseRepository newExerciseRepository, PlatformTransactionManager transactionManager, NewCourseRepository newCourseRepository, AuthService authService, StudentRepository studentRepository) {
        this.newModuleRepository = newModuleRepository;
        this.newExerciseRepository = newExerciseRepository;
        this.transactionManager = transactionManager;
        this.newCourseRepository = newCourseRepository;
        this.authService = authService;
        this.studentRepository = studentRepository;
    }

    @Override
    public NewModule createModule(NewModuleDto module) {
        if (module == null) {
            log.error("user try to create null module");
            throw new InvalidFieldException("Не хватает информации для создания курса");
        }
        if (module.getName().isEmpty() ||
                module.getDescription().isEmpty()) {
            throw new InvalidFieldException("Поля name, description и total points являются обязательными");
        }

        return newModuleRepository.save(NewModuleMapper.toEntity(module));
    }

    @Override
    public NewModule linkExercise(UUID moduleUUID, UUID exerciseUUID) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setName("linkWithNewExercise");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(definition);
        try{
            var exerciseEntity = newExerciseRepository.findById(exerciseUUID).orElseThrow(() -> new NotExistException("Упражнения с uuid: " + exerciseUUID + " не существует"));
            var moduleEntity = newModuleRepository.findById(moduleUUID).orElseThrow(() -> new NotExistException("Модуль с uuid: " + exerciseUUID + " не существует"));

            moduleEntity.getExercises().add(exerciseEntity);
            transactionManager.commit(status);
            return newModuleRepository.save(moduleEntity);
        }catch (Exception e){
            transactionManager.rollback(status);
            throw e;
        }
    }

    /**
     * Возвращает именно модуль без привязки к какому-либо курсу.
     * Данный метод во многом утилитарный для админских ролей
     *
     * @param uuid модуля
     * @return {@link NewModule}
     */
    @Override
    public NewModule getModuleByUUID(UUID uuid) {
        return newModuleRepository.findById(uuid).orElseThrow(() -> new NotExistException("Модуль с uuid: " + uuid + " не существует"));
    }

    @Override
    public void deleteModule(UUID uuid) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setName("deleteNewModule");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(definition);
        try{
            var module = newModuleRepository.findById(uuid).orElseThrow(() -> new NotExistException("Модуля с таким uuid не существует"));

            newCourseRepository.removeByNewModuleList(List.of(module));
            newModuleRepository.deleteById(uuid);
            transactionManager.commit(status);
//            return 0;
        }catch (Exception e){
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public List<NewModule> getAllModules(Long courseID) {
        return List.of();
    }

    /**
     * Возвращает именно модули без привязки к какому-либо курсу.
     * Данный метод во многом утилитарный для админских ролей
     *
     * @return list of = {@link NewModule}
     */
    @Override
    public List<NewModule> getAllModules() {
        return newModuleRepository.findAll();
    }

    @Override
    public NewModule updateModule(UUID uuid, NewModuleDto moduleDto) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setName("updateNewModule");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(definition);
        try{
            if (moduleDto.getName().isEmpty() ||
                    moduleDto.getDescription().isEmpty()) {
                throw new InvalidFieldException("Поля name, description и total points являются обязательными");
            }
            var oldEntity = newModuleRepository.findById(uuid).orElseThrow(() -> new NotExistException("модуля с заданным " +
                    "uuid не существует"));

            var newEntity = NewModuleMapper.toEntity(moduleDto);
            newEntity.setCreatedAt(oldEntity.getCreatedAt());
            newEntity.setUuid(uuid);
            newEntity.setExercises(oldEntity.getExercises());

            newModuleRepository.save(newEntity);
            transactionManager.commit(status);
            return newEntity;
        }catch (Exception e){
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public Boolean isModuleComplete(UUID uuid) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setName("isNewModuleComplete");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(definition);
        try{
            var moduleEntity = newModuleRepository.findById(uuid).orElseThrow(() -> new NotExistException("модуля с заданным " +
                    "uuid не существует"));

            var moduleExercises = moduleEntity.getExercises();
            var sumPoints = moduleExercises.stream()
                    .mapToInt(NewExercise::getPoints)
                    .sum();
            var requiredPoints = sumPoints * 0.75;


            var student = studentRepository.findByUsid(authService.getCurrentUser().getId()).orElseThrow(() -> new NotExistException("Пользователь временно недоступен"));

            Set<UUID> finishedExerciseIds = student.getFinishedExercises()
                    .stream()
                    .map(NewExercise::getUuid)
                    .collect(Collectors.toSet());

            int earnedPoints = moduleExercises.stream()
                    .filter(e -> finishedExerciseIds.contains(e.getUuid()))
                    .mapToInt(NewExercise::getPoints)
                    .sum();
            transactionManager.commit(status);
            return earnedPoints >= requiredPoints;
        }catch (Exception e){
            transactionManager.rollback(status);
            throw e;
        }
    }
}
