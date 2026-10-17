package ebdmanager.repository;

import ebdmanager.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de acesso a dados da entidade Enrollment.
 */
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByEbdClassId(Long ebdClassId);

    Optional<Enrollment> findByStudentIdAndEbdClassId(Long studentId, Long ebdClassId);

    boolean existsByStudentIdAndEbdClassId(Long studentId, Long ebdClassId);

    long countByEbdClassId(Long ebdClassId);
}
