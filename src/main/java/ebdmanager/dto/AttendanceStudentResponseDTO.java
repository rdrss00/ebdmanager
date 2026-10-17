package ebdmanager.dto;

import java.time.LocalDateTime;

/**
 * DTO para retorno dos dados de presença e itens de participação de um aluno.
 */
public class AttendanceStudentResponseDTO {

    private Long id;
    private Long studentId;
    private String studentName;
    private boolean present;
    private boolean book;
    private boolean bible;
    private double offering;
    private LocalDateTime createdAt;

    public AttendanceStudentResponseDTO() {
    }

    public AttendanceStudentResponseDTO(Long id, Long studentId, String studentName, boolean present, boolean book, boolean bible, double offering, LocalDateTime createdAt) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.present = present;
        this.book = book;
        this.bible = bible;
        this.offering = offering;
        this.createdAt = createdAt;
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

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public boolean isBook() {
        return book;
    }

    public void setBook(boolean book) {
        this.book = book;
    }

    public boolean isBible() {
        return bible;
    }

    public void setBible(boolean bible) {
        this.bible = bible;
    }

    public double getOffering() {
        return offering;
    }

    public void setOffering(double offering) {
        this.offering = offering;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
