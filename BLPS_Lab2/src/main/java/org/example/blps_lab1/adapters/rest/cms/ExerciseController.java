package org.example.blps_lab1.adapters.rest.cms;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.blps_lab1.adapters.course.dto.nw.NewExerciseDto;
import org.example.blps_lab1.adapters.course.mapper.NewExerciseMapper;
import org.example.blps_lab1.core.ports.course.nw.NewExerciseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cms/exercises")
@AllArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Tag(name = "Exercise-controller", description = "контроллер для управления заданиями")
public class ExerciseController {
    private final NewExerciseService newExerciseService;

    @PostMapping
    @Operation(summary = "создание задания")
    public ResponseEntity<Map<String, Object>> createExercise(@RequestBody NewExerciseDto exerciseDto){
        Map<String, Object> response = new HashMap<>();
        var createdExercise = newExerciseService.createNewExercise(exerciseDto);
        var newExerciseDto = NewExerciseMapper.toDto(createdExercise);
        response.put("created_exercise", newExerciseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "удаление задания")
    public ResponseEntity<Map<String, Object>> deleteExercise(@PathVariable @Parameter(description = "Идентификатор задания") Long id){
        Map<String, Object> response = new HashMap<>();
        newExerciseService.deleteNewExercise(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> getExerciseById(@PathVariable UUID uuid){
        Map<String, Object> response = new HashMap<>();
        var exercise = newExerciseService.getNewExerciseById(uuid);
        var exerciseDto = NewExerciseMapper.toDto(exercise);
        response.put("exercise", exerciseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    @Operation(summary = "обновление задания")
    public ResponseEntity<Map<String, Object>> updateExercise(@PathVariable @Parameter(description = "Идентификатор задания") Long id, @Valid @RequestBody NewExerciseDto exerciseDto){
        Map<String, Object> response = new HashMap<>();
        var updatedExercise = newExerciseService.updateNewExercise(id, exerciseDto);
        response.put("message", "exercise updated");
        response.put("exercise", updatedExercise);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
