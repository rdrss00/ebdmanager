package ebdmanager.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidade de domínio que representa a sessão de chamada/aula de uma classe da EBD.
 * Tabela correspondente: attendance.
 */
@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ebd_class_id", nullable = false)
    private EbdClass ebdClass;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "attendance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttendanceStudent> studentAttendances = new ArrayList<>();

    @OneToOne(mappedBy = "attendance", cascade = CascadeType.ALL, orphanRemoval = true)
    private AttendanceOffering offering;

    public Attendance() {
    }

    public Attendance(EbdClass ebdClass, LocalDateTime createdAt) {
        this.ebdClass = ebdClass;
        this.createdAt = createdAt;
    }

    public Attendance(Long id, EbdClass ebdClass, LocalDateTime createdAt) {
        this.id = id;
        this.ebdClass = ebdClass;
        this.createdAt = createdAt;
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

    public EbdClass getEbdClass() {
        return ebdClass;
    }

    public void setEbdClass(EbdClass ebdClass) {
        this.ebdClass = ebdClass;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<AttendanceStudent> getStudentAttendances() {
        return studentAttendances;
    }

    public void setStudentAttendances(List<AttendanceStudent> studentAttendances) {
        this.studentAttendances = studentAttendances;
    }

    public AttendanceOffering getOffering() {
        return offering;
    }

    public void setOffering(AttendanceOffering offering) {
        this.offering = offering;
    }

    public void addStudentAttendance(AttendanceStudent studentAttendance) {
        studentAttendances.add(studentAttendance);
        studentAttendance.setAttendance(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                '}';
    }
}
