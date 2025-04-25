package org.example.blps_lab1.adapters.rest.lms;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.blps_lab1.courseSignUp.dto.ExerciseDto;
import org.example.blps_lab1.courseSignUp.models.Exercise;
import org.example.blps_lab1.courseSignUp.service.ExerciseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("lmsEnrollmentController")
@RequestMapping("/api/v1/exercises")
@AllArgsConstructor
public class ExerciseController {
    private final ExerciseService exerciseService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllExercises(){
        Map<String, Object> response = new HashMap<>();
        List<Exercise> exerciseList = exerciseService.getAllExercises();
        List<ExerciseDto> exerciseDtoList = exerciseService.convertToExerciseDto(exerciseList);
        response.put("exercise_list", exerciseDtoList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getExerciseById(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        Exercise exercise = exerciseService.getExerciseById(id);
        ExerciseDto exerciseDto = exerciseService.convertToExerciseDto(exercise);
        response.put("exercise", exerciseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<Map<String, Object>> submitAnswer(@PathVariable Long id, @RequestBody Map<String, String> userAnswer){
        String answer = userAnswer.get("answer");
        boolean isCorrect = exerciseService.submitAnswer(id, answer);
        Map<String, Object> response = new HashMap<>();
        response.put("exercise_id", id);
        response.put("is_correct", isCorrect);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
