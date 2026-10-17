package ebdmanager.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para retorno dos dados completos de uma chamada da EBD.
 */
public class AttendanceResponseDTO {

    private Long id;
    private Long ebdClassId;
    private String ebdClassName;
    private LocalDateTime createdAt;
    private double totalOffering;
    private int totalPresent;
    private int totalAbsent;
    private List<AttendanceStudentResponseDTO> students = new ArrayList<>();

    public AttendanceResponseDTO() {
    }

    public AttendanceResponseDTO(Long id, Long ebdClassId, String ebdClassName, LocalDateTime createdAt, double totalOffering, int totalPresent, int totalAbsent, List<AttendanceStudentResponseDTO> students) {
        this.id = id;
        this.ebdClassId = ebdClassId;
        this.ebdClassName = ebdClassName;
        this.createdAt = createdAt;
        this.totalOffering = totalOffering;
        this.totalPresent = totalPresent;
        this.totalAbsent = totalAbsent;
        this.students = students != null ? students : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getTotalOffering() {
        return totalOffering;
    }

    public void setTotalOffering(double totalOffering) {
        this.totalOffering = totalOffering;
    }

    public int getTotalPresent() {
        return totalPresent;
    }

    public void setTotalPresent(int totalPresent) {
        this.totalPresent = totalPresent;
    }

    public int getTotalAbsent() {
        return totalAbsent;
    }

    public void setTotalAbsent(int totalAbsent) {
        this.totalAbsent = totalAbsent;
    }

    public List<AttendanceStudentResponseDTO> getStudents() {
        return students;
    }

    public void setStudents(List<AttendanceStudentResponseDTO> students) {
        this.students = students != null ? students : new ArrayList<>();
    }
}
