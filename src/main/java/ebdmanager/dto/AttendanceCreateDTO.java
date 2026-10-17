package ebdmanager.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para lançamento da chamada de uma classe.
 */
public class AttendanceCreateDTO {

    @NotNull(message = "O identificador da classe (ebdClassId) é obrigatório")
    private Long ebdClassId;

    private LocalDateTime createdAt;

    @PositiveOrZero(message = "O total da oferta não pode ser negativo")
    private Double totalOffering;

    @NotEmpty(message = "A lista de alunos para registro de presença não pode estar vazia")
    @Valid
    private List<AttendanceRecordItemDTO> students;

    public AttendanceCreateDTO() {
    }

    public AttendanceCreateDTO(Long ebdClassId, LocalDateTime createdAt, Double totalOffering, List<AttendanceRecordItemDTO> students) {
        this.ebdClassId = ebdClassId;
        this.createdAt = createdAt;
        this.totalOffering = totalOffering;
        this.students = students;
    }

    public Long getEbdClassId() {
        return ebdClassId;
    }

    public void setEbdClassId(Long ebdClassId) {
        this.ebdClassId = ebdClassId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getTotalOffering() {
        return totalOffering;
    }

    public void setTotalOffering(Double totalOffering) {
        this.totalOffering = totalOffering;
    }

    public List<AttendanceRecordItemDTO> getStudents() {
        return students;
    }

    public void setStudents(List<AttendanceRecordItemDTO> students) {
        this.students = students;
    }
}
