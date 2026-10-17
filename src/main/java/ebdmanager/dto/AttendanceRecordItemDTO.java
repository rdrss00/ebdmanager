package ebdmanager.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO para registro individual de presença e itens de participação de um aluno.
 */
public class AttendanceRecordItemDTO {

    @NotNull(message = "O identificador do aluno (studentId) é obrigatório")
    private Long studentId;

    private boolean present;
    private boolean book;
    private boolean bible;

    @PositiveOrZero(message = "O valor da oferta não pode ser negativo")
    private Double offering = 0.0;

    public AttendanceRecordItemDTO() {
    }

    public AttendanceRecordItemDTO(Long studentId, boolean present, boolean book, boolean bible, Double offering) {
        this.studentId = studentId;
        this.present = present;
        this.book = book;
        this.bible = bible;
        this.offering = offering != null ? offering : 0.0;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
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

    public Double getOffering() {
        return offering != null ? offering : 0.0;
    }

    public void setOffering(Double offering) {
        this.offering = offering != null ? offering : 0.0;
    }
}
