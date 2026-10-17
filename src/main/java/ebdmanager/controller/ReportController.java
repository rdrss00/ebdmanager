package ebdmanager.controller;

import ebdmanager.dto.AttendanceDateReportDTO;
import ebdmanager.dto.ClassAttendanceReportDTO;
import ebdmanager.dto.GeneralReportDTO;
import ebdmanager.dto.PeriodReportDTO;
import ebdmanager.dto.QuarterlyReportDTO;
import ebdmanager.dto.StudentAttendanceReportDTO;
import ebdmanager.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Controlador REST para geração de relatórios estatísticos e analíticos da EBD.
 */
@RestController
@RequestMapping("/api/reports")
@Tag(name = "Relatórios", description = "Endpoints para emissão de relatórios de frequência, participação, ofertas e estatísticas gerais")
public class ReportController {

    private final ReportService reportService;

    /**
     * Construtor para injeção de dependência.
     *
     * @param reportService serviço de relatórios
     */
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/by-date")
    @Operation(summary = "Relatório por data", description = "Gera relatório de frequência e participação consolidado por data")
    public ResponseEntity<AttendanceDateReportDTO> getReportByDate(
            @Parameter(description = "Data no formato YYYY-MM-DD", example = "2026-10-18")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reportService.getReportByDate(date));
    }

    @GetMapping("/by-student/{studentId}")
    @Operation(summary = "Relatório por aluno", description = "Gera relatório de frequência e participação individual de um aluno (presenças, faltas, bíblia, revista, ofertas)")
    public ResponseEntity<StudentAttendanceReportDTO> getReportByStudent(
            @PathVariable Long studentId,
            @Parameter(description = "Data inicial no formato YYYY-MM-DD", example = "2026-01-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Data final no formato YYYY-MM-DD", example = "2026-12-31")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.getReportByStudent(studentId, Optional.ofNullable(startDate), Optional.ofNullable(endDate)));
    }

    @GetMapping("/by-class/{classId}")
    @Operation(summary = "Relatório por classe", description = "Gera relatório de frequência e participação por classe (presentes e ausentes dado um dia ou período de dias)")
    public ResponseEntity<ClassAttendanceReportDTO> getReportByClass(
            @PathVariable Long classId,
            @Parameter(description = "Data inicial no formato YYYY-MM-DD", example = "2026-01-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Data final no formato YYYY-MM-DD", example = "2026-12-31")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.getReportByClass(classId, Optional.ofNullable(startDate), Optional.ofNullable(endDate)));
    }

    @GetMapping("/period")
    @Operation(summary = "Relatório por período geral", description = "Gera relatório de frequência e participação por período geral (presentes e ausentes por classe)")
    public ResponseEntity<PeriodReportDTO> getReportByPeriod(
            @Parameter(description = "Data inicial no formato YYYY-MM-DD", example = "2026-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Data final no formato YYYY-MM-DD", example = "2026-03-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.getReportByPeriod(startDate, endDate));
    }

    @GetMapping("/general")
    @Operation(summary = "Relatório geral", description = "Gera relatório quantitativo geral (quantos alunos, quantas classes, distribuição de alunos por classe)")
    public ResponseEntity<GeneralReportDTO> getGeneralReport() {
        return ResponseEntity.ok(reportService.getGeneralReport());
    }

    @GetMapping("/quarterly")
    @Operation(summary = "Relatório por trimestre", description = "Gera relatório consolidado por trimestre (alunos, classes, presenças, faltas, ofertas, etc.)")
    public ResponseEntity<QuarterlyReportDTO> getQuarterlyReport(
            @Parameter(description = "Ano", example = "2026")
            @RequestParam int year,
            @Parameter(description = "Trimestre (1 a 4)", example = "1")
            @RequestParam int quarter) {
        return ResponseEntity.ok(reportService.getQuarterlyReport(year, quarter));
    }
}
