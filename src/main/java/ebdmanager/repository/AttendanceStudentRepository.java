package ebdmanager.repository;

import ebdmanager.model.AttendanceStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório para operações de acesso a dados da entidade AttendanceStudent.
 */
@Repository
public interface AttendanceStudentRepository extends JpaRepository<AttendanceStudent, Long> {

    List<AttendanceStudent> findByAttendanceId(Long attendanceId);

    List<AttendanceStudent> findByStudentIdOrderByCreatedAtDesc(Long studentId);

    List<AttendanceStudent> findByStudentIdAndCreatedAtBetweenOrderByCreatedAtDesc(Long studentId, LocalDateTime start, LocalDateTime end);

    List<AttendanceStudent> findByAttendanceCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<AttendanceStudent> findByAttendanceEbdClassIdAndAttendanceCreatedAtBetween(Long ebdClassId, LocalDateTime start, LocalDateTime end);

    boolean existsByAttendanceIdAndStudentId(Long attendanceId, Long studentId);
}
