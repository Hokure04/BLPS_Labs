package org.example.blps_lab1.adapters.rest.cms;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.blps_lab1.courseSignUp.dto.ModuleDto;
import org.example.blps_lab1.courseSignUp.models.Module;
import org.example.blps_lab1.courseSignUp.service.ModuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/modules")
@AllArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ModuleController {
    private final ModuleService moduleService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createModule(@Valid @RequestBody Module module){
        Map<String, Object> response = new HashMap<>();
        Module createdModule = moduleService.createModule(module);
        ModuleDto moduleDto = moduleService.convertToModelDto(createdModule);
        response.put("created_module", moduleDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteModule(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        moduleService.deleteModule(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateModule(@PathVariable Long id, @Valid @RequestBody ModuleDto moduleDto){
        Map<String, Object> response = new HashMap<>();
        Module updatedModule = moduleService.updateModule(id, moduleDto);
        response.put("message", "module updated");
        response.put("module", updatedModule);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
