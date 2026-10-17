package ebdmanager.repository;

import ebdmanager.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório para operações de acesso a dados da entidade Attendance.
 */
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByEbdClassId(Long ebdClassId);

    List<Attendance> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);

    List<Attendance> findByEbdClassIdAndCreatedAtBetweenOrderByCreatedAtDesc(Long ebdClassId, LocalDateTime start, LocalDateTime end);

    long countByEbdClassId(Long ebdClassId);

    long countByEbdClassIdAndCreatedAtBetween(Long ebdClassId, LocalDateTime start, LocalDateTime end);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
