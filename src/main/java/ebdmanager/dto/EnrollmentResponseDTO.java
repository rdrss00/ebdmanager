package ebdmanager.dto;

import java.time.LocalDateTime;

/**
 * DTO para retorno de informações de matrícula de um aluno.
 */
public class EnrollmentResponseDTO {

    private Long id;
    private Long studentId;
    private String studentName;
    private Long ebdClassId;
    private String ebdClassName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public EnrollmentResponseDTO() {
    }

    public EnrollmentResponseDTO(Long id, Long studentId, String studentName, Long ebdClassId, String ebdClassName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.ebdClassId = ebdClassId;
        this.ebdClassName = ebdClassName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getEbdClassId() {
        return ebdClassId;
    }

    public void setEbdClassId(Long ebdClassId) {
        this.ebdClassId = ebdClassId;
    }

    public String getEbdClassName() {
        return ebdClassName;
    }

    public void setEbdClassName(String ebdClassName) {
        this.ebdClassName = ebdClassName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
