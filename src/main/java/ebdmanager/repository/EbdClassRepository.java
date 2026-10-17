package ebdmanager.repository;

import ebdmanager.model.EbdClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para operações de acesso a dados da entidade EbdClass.
 */
@Repository
public interface EbdClassRepository extends JpaRepository<EbdClass, Long> {

    Optional<EbdClass> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);
}
