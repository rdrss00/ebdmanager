package ebdmanager.service;

import ebdmanager.dto.ClassRequestDTO;
import ebdmanager.dto.ClassResponseDTO;
import ebdmanager.exception.BusinessRuleException;
import ebdmanager.exception.ResourceNotFoundException;
import ebdmanager.model.EbdClass;
import ebdmanager.repository.EbdClassRepository;
import ebdmanager.repository.EbdStudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelas regras de negócio e operações de CRUD das classes da EBD.
 */
@Service
public class ClassService {

    private final EbdClassRepository classRepository;
    private final EbdStudentRepository studentRepository;

    /**
     * Construtor para injeção de dependência.
     *
     * @param classRepository   repositório de classes
     * @param studentRepository repositório de alunos
     */
    public ClassService(EbdClassRepository classRepository, EbdStudentRepository studentRepository) {
        this.classRepository = classRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Cria uma nova classe na EBD.
     *
     * @param dto dados para criação da classe
     * @return informações da classe criada
     */
    @Transactional
    public ClassResponseDTO createClass(ClassRequestDTO dto) {
        if (classRepository.existsByNomeIgnoreCase(dto.getNome().trim())) {
            throw new BusinessRuleException("Já existe uma classe cadastrada com o nome: " + dto.getNome());
        }

        EbdClass ebdClass = new EbdClass(dto.getNome().trim());
        EbdClass saved = classRepository.save(ebdClass);
        return toResponseDTO(saved);
    }

    /**
     * Retorna a lista de todas as classes cadastradas.
     *
     * @return lista de classes
     */
    @Transactional(readOnly = true)
    public List<ClassResponseDTO> getAllClasses() {
        return classRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca uma classe pelo seu identificador.
     *
     * @param id identificador da classe
     * @return classe encontrada
     */
    @Transactional(readOnly = true)
    public ClassResponseDTO getClassById(Long id) {
        EbdClass ebdClass = findEntityById(id);
        return toResponseDTO(ebdClass);
    }

    /**
     * Atualiza as informações de uma classe existente.
     *
     * @param id  identificador da classe a ser atualizada
     * @param dto novos dados da classe
     * @return classe atualizada
     */
    @Transactional
    public ClassResponseDTO updateClass(Long id, ClassRequestDTO dto) {
        EbdClass ebdClass = findEntityById(id);
        String novoNome = dto.getNome().trim();

        if (!ebdClass.getNome().equalsIgnoreCase(novoNome) && classRepository.existsByNomeIgnoreCase(novoNome)) {
            throw new BusinessRuleException("Já existe outra classe cadastrada com o nome: " + novoNome);
        }

        ebdClass.setNome(novoNome);
        EbdClass updated = classRepository.save(ebdClass);
        return toResponseDTO(updated);
    }

    /**
     * Remove uma classe do sistema pelo seu identificador.
     *
     * @param id identificador da classe
     */
    @Transactional
    public void deleteClass(Long id) {
        EbdClass ebdClass = findEntityById(id);
        classRepository.delete(ebdClass);
    }

    /**
     * Busca a entidade da classe ou lança exceção caso não encontrada.
     *
     * @param id identificador da classe
     * @return entidade da classe
     */
    @Transactional(readOnly = true)
    public EbdClass findEntityById(Long id) {
        return classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe não encontrada com o ID: " + id));
    }

    /**
     * Converte uma entidade EbdClass em DTO com contagem de alunos.
     *
     * @param ebdClass entidade a ser convertida
     * @return DTO de resposta da classe
     */
    private ClassResponseDTO toResponseDTO(EbdClass ebdClass) {
        long totalStudents = studentRepository.countByEbdClassId(ebdClass.getId());
        return new ClassResponseDTO(
                ebdClass.getId(),
                ebdClass.getNome(),
                ebdClass.getCreatedAt(),
                ebdClass.getUpdatedAt(),
                totalStudents
        );
    }
}
