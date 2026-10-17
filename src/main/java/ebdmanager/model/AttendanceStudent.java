package ebdmanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade de domínio que representa a presença e participação individual de um aluno em uma chamada.
 * Tabela correspondente: attendance_student.
 */
@Entity
@Table(name = "attendance_student")
public class AttendanceStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id", nullable = false)
    private Attendance attendance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ebd_student_id", nullable = false)
    private EbdStudent student;

    @Column(name = "present", nullable = false)
    private boolean present;

    @Column(name = "book", nullable = false)
    private boolean book;

    @Column(name = "bible", nullable = false)
    private boolean bible;

    @Column(name = "offering", nullable = false)
    private double offering;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public AttendanceStudent() {
    }

    public AttendanceStudent(Attendance attendance, EbdStudent student, boolean present, boolean book, boolean bible, double offering) {
        this.attendance = attendance;
        this.student = student;
        this.present = present;
        this.book = book;
        this.bible = bible;
        this.offering = offering;
    }

    public AttendanceStudent(Long id, Attendance attendance, EbdStudent student, boolean present, boolean book, boolean bible, double offering) {
        this.id = id;
        this.attendance = attendance;
        this.student = student;
        this.present = present;
        this.book = book;
        this.bible = bible;
        this.offering = offering;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public EbdStudent getStudent() {
        return student;
    }

    public void setStudent(EbdStudent student) {
        this.student = student;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendanceStudent that = (AttendanceStudent) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AttendanceStudent{" +
                "id=" + id +
                ", present=" + present +
                ", book=" + book +
                ", bible=" + bible +
                ", offering=" + offering +
                ", createdAt=" + createdAt +
                '}';
    }
}
