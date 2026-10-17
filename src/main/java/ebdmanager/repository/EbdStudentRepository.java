package ebdmanager.repository;

import ebdmanager.model.EbdStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para operações de acesso a dados da entidade EbdStudent.
 */
@Repository
public interface EbdStudentRepository extends JpaRepository<EbdStudent, Long> {

    List<EbdStudent> findByEbdClassId(Long ebdClassId);

    long countByEbdClassId(Long ebdClassId);
}
