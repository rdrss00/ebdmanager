package ebdmanager.controller;

import ebdmanager.dto.StudentRequestDTO;
import ebdmanager.dto.StudentResponseDTO;
import ebdmanager.service.StudentService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gerenciamento dos alunos da EBD.
 */
@RestController
@RequestMapping("/api/students")
@Tag(name = "Alunos", description = "Endpoints para gerenciamento cadastral dos alunos da EBD")
public class StudentController {

    private final StudentService studentService;

    /**
     * Construtor para injeção de dependência.
     *
     * @param studentService serviço de alunos
     */
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo aluno", description = "Cadastra um aluno e o vincula à classe indicada")
    public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody StudentRequestDTO dto) {
        StudentResponseDTO created = studentService.createStudent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Listar alunos", description = "Retorna todos os alunos, com filtro opcional por classe")
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents(@RequestParam(required = false) Long classId) {
        return ResponseEntity.ok(studentService.getAllStudents(Optional.ofNullable(classId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar aluno por ID", description = "Retorna os detalhes de um aluno específico")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar aluno", description = "Atualiza os dados de um aluno existente")
    public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequestDTO dto) {
        return ResponseEntity.ok(studentService.updateStudent(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir aluno", description = "Remove um aluno do sistema (exige perfil ADMIN)")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
