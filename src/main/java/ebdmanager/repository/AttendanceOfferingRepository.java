package ebdmanager.repository;

import ebdmanager.model.AttendanceOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de acesso a dados da entidade AttendanceOffering.
 */
@Repository
public interface AttendanceOfferingRepository extends JpaRepository<AttendanceOffering, Long> {

    Optional<AttendanceOffering> findByAttendanceId(Long attendanceId);

    List<AttendanceOffering> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<AttendanceOffering> findByAttendanceEbdClassIdAndCreatedAtBetween(Long ebdClassId, LocalDateTime start, LocalDateTime end);
}
