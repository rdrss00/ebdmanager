package ebdmanager.controller;

import ebdmanager.dto.ClassRequestDTO;
import ebdmanager.dto.ClassResponseDTO;
import ebdmanager.service.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para gerenciamento de classes da EBD.
 */
@RestController
@RequestMapping("/api/classes")
@Tag(name = "Classes", description = "Endpoints para gerenciamento das turmas/classes da EBD")
public class ClassController {

    private final ClassService classService;

    /**
     * Construtor para injeção de dependência.
     *
     * @param classService serviço de classes
     */
    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @PostMapping
    @Operation(summary = "Criar nova classe", description = "Cadastra uma nova classe na EBD")
    public ResponseEntity<ClassResponseDTO> createClass(@Valid @RequestBody ClassRequestDTO dto) {
        ClassResponseDTO created = classService.createClass(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Listar classes", description = "Retorna todas as classes cadastradas")
    public ResponseEntity<List<ClassResponseDTO>> getAllClasses() {
        return ResponseEntity.ok(classService.getAllClasses());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar classe por ID", description = "Retorna os detalhes de uma classe específica")
    public ResponseEntity<ClassResponseDTO> getClassById(@PathVariable Long id) {
        return ResponseEntity.ok(classService.getClassById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar classe", description = "Atualiza o nome de uma classe existente")
    public ResponseEntity<ClassResponseDTO> updateClass(@PathVariable Long id, @Valid @RequestBody ClassRequestDTO dto) {
        return ResponseEntity.ok(classService.updateClass(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir classe", description = "Remove uma classe do sistema (exige perfil ADMIN)")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        classService.deleteClass(id);
        return ResponseEntity.noContent().build();
    }
}
