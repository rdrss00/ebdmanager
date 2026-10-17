package ebdmanager.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para solicitação de matrícula de um aluno em uma classe.
 */
public class EnrollmentRequestDTO {

    @NotNull(message = "O identificador do aluno (studentId) é obrigatório")
    private Long studentId;

    @NotNull(message = "O identificador da classe (ebdClassId) é obrigatório")
    private Long ebdClassId;

    public EnrollmentRequestDTO() {
    }

    public EnrollmentRequestDTO(Long studentId, Long ebdClassId) {
        this.studentId = studentId;
        this.ebdClassId = ebdClassId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getEbdClassId() {
        return ebdClassId;
    }

    public void setEbdClassId(Long ebdClassId) {
        this.ebdClassId = ebdClassId;
    }
}
