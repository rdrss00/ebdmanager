package ebdmanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade de domínio que representa o total de ofertas arrecadadas em uma chamada.
 * Tabela correspondente: attendance_offering.
 */
@Entity
@Table(name = "attendance_offering")
public class AttendanceOffering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id", nullable = false, unique = true)
    private Attendance attendance;

    @Column(name = "total_offering", nullable = false)
    private double totalOffering;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public AttendanceOffering() {
    }

    public AttendanceOffering(Attendance attendance, double totalOffering) {
        this.attendance = attendance;
        this.totalOffering = totalOffering;
    }

    public AttendanceOffering(Long id, Attendance attendance, double totalOffering) {
        this.id = id;
        this.attendance = attendance;
        this.totalOffering = totalOffering;
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

    public double getTotalOffering() {
        return totalOffering;
    }

    public void setTotalOffering(double totalOffering) {
        this.totalOffering = totalOffering;
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
        AttendanceOffering that = (AttendanceOffering) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AttendanceOffering{" +
                "id=" + id +
                ", totalOffering=" + totalOffering +
                ", createdAt=" + createdAt +
                '}';
    }
}
