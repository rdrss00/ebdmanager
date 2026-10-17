package ebdmanager.controller;

import ebdmanager.dto.AttendanceCreateDTO;
import ebdmanager.dto.AttendanceResponseDTO;
import ebdmanager.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para registro de chamadas e controle de presenças dos alunos da EBD.
 */
@RestController
@RequestMapping("/api/attendances")
@Tag(name = "Chamadas e Presenças", description = "Endpoints para registro e consulta de chamadas e presenças de turmas")
public class AttendanceController {

    private final AttendanceService attendanceService;

    /**
     * Construtor para injeção de dependência.
     *
     * @param attendanceService serviço de chamadas e presenças
     */
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    @Operation(
            summary = "Registrar chamada",
            description = "Registra a chamada de uma turma na data informada, incluindo presenças, bíblias, revistas e ofertas"
    )
    public ResponseEntity<AttendanceResponseDTO> recordAttendance(@Valid @RequestBody AttendanceCreateDTO dto) {
        AttendanceResponseDTO recorded = attendanceService.recordAttendance(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(recorded);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar chamada por ID", description = "Retorna os detalhes e a lista de presenças de uma chamada")
    public ResponseEntity<AttendanceResponseDTO> getAttendanceById(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.getAttendanceById(id));
    }

    @GetMapping("/class/{classId}")
    @Operation(summary = "Listar chamadas da classe", description = "Retorna todo o histórico de chamadas realizadas para a classe informada")
    public ResponseEntity<List<AttendanceResponseDTO>> getAttendancesByClass(@PathVariable Long classId) {
        return ResponseEntity.ok(attendanceService.getAttendancesByClass(classId));
    }
}
