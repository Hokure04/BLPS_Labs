package org.example.blps_lab1.adapters.course.service.nw;

import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.adapters.course.dto.ModuleDto;
import org.example.blps_lab1.adapters.course.dto.nw.NewModuleDto;
import org.example.blps_lab1.adapters.course.mapper.NewModuleMapper;
import org.example.blps_lab1.adapters.db.course.ExerciseRepository;
import org.example.blps_lab1.adapters.db.course.NewExerciseRepository;
import org.example.blps_lab1.adapters.db.course.NewModuleRepository;
import org.example.blps_lab1.core.domain.course.nw.NewModule;
import org.example.blps_lab1.core.exception.course.InvalidFieldException;
import org.example.blps_lab1.core.exception.course.NotExistException;
import org.example.blps_lab1.core.ports.course.nw.NewModuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class NewModuleServiceImpl implements NewModuleService {

    private final NewModuleRepository newModuleRepository;
    private final NewExerciseRepository newExerciseRepository;

    private final TransactionTemplate transactionTemplate;

    public NewModuleServiceImpl(NewModuleRepository newModuleRepository, NewExerciseRepository newExerciseRepository, PlatformTransactionManager trManager) {
        this.newModuleRepository = newModuleRepository;
        this.newExerciseRepository = newExerciseRepository;
        this.transactionTemplate = new TransactionTemplate(trManager);
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
        return transactionTemplate.execute(status -> {
            var exerciseEntity = newExerciseRepository.findById(exerciseUUID).orElseThrow(() -> new NotExistException("Упражнения с uuid: " + exerciseUUID + " не существует"));
            var moduleEntity = newModuleRepository.findById(moduleUUID).orElseThrow(() -> new NotExistException("Модуль с uuid: " + exerciseUUID + " не существует"));

            moduleEntity.getExercises().add(exerciseEntity);
            return newModuleRepository.save(moduleEntity);
        });
    }

    @Override
    public NewModule getModuleById(Long id) {
        return null;
    }

    @Override
    public void deleteModule(Long id) {

    }

    @Override
    public List<NewModule> getAllModules(Long courseID) {
        return List.of();
    }

    @Override
    public NewModule updateModule(Long id, NewModuleDto moduleDto) {
        return null;
    }
}
