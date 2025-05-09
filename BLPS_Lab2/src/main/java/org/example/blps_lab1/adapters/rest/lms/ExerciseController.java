package org.example.blps_lab1.adapters.rest.lms;

import lombok.AllArgsConstructor;
import org.example.blps_lab1.adapters.course.mapper.NewExerciseMapper;
import org.example.blps_lab1.core.ports.course.nw.NewExerciseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController("lmsEnrollmentController")
@RequestMapping("/api/v1/lms/exercises")
@AllArgsConstructor
public class ExerciseController {
    private final NewExerciseService exerciseService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllExercises(){
        //TODO странный метод, ранее почему-то возвращал упражнения ВООБЩЕ ВСЕ
        return null;
//        Map<String, Object> response = new HashMap<>();
//        List<Exercise> exerciseList = exerciseService.getAllExercises();
//        List<ExerciseDto> exerciseDtoList = ExerciseMapper.convertToExerciseDto(exerciseList);
//        response.put("exercise_list", exerciseDtoList);
//        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<Map<String, Object>> submitAnswer(@PathVariable Long id, @RequestBody Map<String, String> userAnswer){
        String answer = userAnswer.get("answer");
        Boolean isCorrect = exerciseService.submitAnswer(id, answer);
        Map<String, Object> response = new HashMap<>();
        response.put("exercise_id", id);
        response.put("is_correct", isCorrect);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
