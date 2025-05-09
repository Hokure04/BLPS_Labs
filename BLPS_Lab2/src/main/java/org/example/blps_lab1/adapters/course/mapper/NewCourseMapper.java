package org.example.blps_lab1.adapters.course.mapper;

import org.example.blps_lab1.adapters.course.dto.nw.NewCourseDto;
import org.example.blps_lab1.core.domain.course.nw.NewCourse;
import org.example.blps_lab1.core.domain.course.nw.NewModule;

import java.util.List;

public class NewCourseMapper {

    public static NewCourseDto toDto(NewCourse entity) {
        var modules = entity.getNewModuleList() == null
                ? null :
                entity.getNewModuleList()
                        .stream()
                        .map(NewModuleMapper::toDto)
                        .toList();
        return NewCourseDto.builder()
                .uuid(entity.getUuid())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .creationTime(entity.getCreationTime())
                .newModuleList(modules)
                .build();
    }

    public static NewCourse toEntity(NewCourseDto dto) {
        List<NewModule> modules = dto.getNewModuleList() == null
                ? null :
                dto.getNewModuleList()
                        .stream()
                        .map(NewModuleMapper::toEntity)
                        .toList();
        return NewCourse.builder()
                .uuid(dto.getUuid())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .creationTime(dto.getCreationTime())
                .newModuleList(modules)
                .build();
    }

}
