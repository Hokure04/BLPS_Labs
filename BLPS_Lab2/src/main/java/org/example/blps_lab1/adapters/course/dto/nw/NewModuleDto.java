package org.example.blps_lab1.adapters.course.dto.nw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.blps_lab1.core.domain.course.nw.NewExercise;

import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class NewModuleDto {
    private UUID uuid;
    private String name;
    private String description;
    private List<NewExercise> exercises;
}
