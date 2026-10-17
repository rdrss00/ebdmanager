package ebdmanager.controller;

import ebdmanager.dto.EnrollmentRequestDTO;
import ebdmanager.dto.EnrollmentResponseDTO;
import ebdmanager.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para gerenciamento de matrículas de alunos em turmas da EBD.
 */
@RestController
@RequestMapping("/api/enrollments")
@Tag(name = "Matrículas", description = "Endpoints para gerenciamento de matrículas de alunos em classes")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    /**
     * Construtor para injeção de dependência.
     *
     * @param enrollmentService serviço de matrículas
     */
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    @Operation(summary = "Matricular aluno", description = "Matricula um aluno em uma classe da EBD")
    public ResponseEntity<EnrollmentResponseDTO> enrollStudent(@Valid @RequestBody EnrollmentRequestDTO dto) {
        EnrollmentResponseDTO created = enrollmentService.enrollStudent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/class/{classId}")
    @Operation(summary = "Listar matrículas da classe", description = "Retorna todos os alunos matriculados na classe informada")
    public ResponseEntity<List<EnrollmentResponseDTO>> getEnrollmentsByClass(@PathVariable Long classId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByClass(classId));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Listar matrículas do aluno", description = "Retorna o histórico de matrículas de um aluno específico")
    public ResponseEntity<List<EnrollmentResponseDTO>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(studentId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar matrícula", description = "Remove um registro de matrícula (exige perfil ADMIN)")
    public ResponseEntity<Void> cancelEnrollment(@PathVariable Long id) {
        enrollmentService.cancelEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}
