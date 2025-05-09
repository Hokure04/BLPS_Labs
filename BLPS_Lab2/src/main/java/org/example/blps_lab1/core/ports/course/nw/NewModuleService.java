package org.example.blps_lab1.core.ports.course.nw;

import org.example.blps_lab1.adapters.course.dto.ModuleDto;
import org.example.blps_lab1.adapters.course.dto.nw.NewModuleDto;
import org.example.blps_lab1.core.domain.course.nw.NewModule;

import java.util.List;
import java.util.UUID;

public interface NewModuleService {
    NewModule createModule(final NewModuleDto module);
    NewModule linkExercise(final UUID moduleUUID, final UUID exerciseUUID);
    NewModule getModuleById(final Long id);
    void deleteModule(final Long id);
    List<NewModule> getAllModules(Long courseID);
    NewModule updateModule(Long id, NewModuleDto moduleDto);
}
