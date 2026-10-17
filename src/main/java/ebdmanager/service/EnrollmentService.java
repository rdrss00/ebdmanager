package ebdmanager.service;

import ebdmanager.dto.EnrollmentRequestDTO;
import ebdmanager.dto.EnrollmentResponseDTO;
import ebdmanager.exception.BusinessRuleException;
import ebdmanager.exception.ResourceNotFoundException;
import ebdmanager.model.EbdClass;
import ebdmanager.model.EbdStudent;
import ebdmanager.model.Enrollment;
import ebdmanager.repository.EbdClassRepository;
import ebdmanager.repository.EbdStudentRepository;
import ebdmanager.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelo gerenciamento de matrículas de alunos em classes da EBD.
 */
@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final EbdStudentRepository studentRepository;
    private final EbdClassRepository classRepository;

    /**
     * Construtor para injeção de dependência.
     *
     * @param enrollmentRepository repositório de matrículas
     * @param studentRepository    repositório de alunos
     * @param classRepository      repositório de classes
     */
    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             EbdStudentRepository studentRepository,
                             EbdClassRepository classRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.classRepository = classRepository;
    }

    /**
     * Realiza a matrícula de um aluno em uma classe da EBD.
     *
     * @param dto dados da matrícula
     * @return informações da matrícula realizada
     */
    @Transactional
    public EnrollmentResponseDTO enrollStudent(EnrollmentRequestDTO dto) {
        EbdStudent student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com o ID: " + dto.getStudentId()));

        EbdClass ebdClass = classRepository.findById(dto.getEbdClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Classe não encontrada com o ID: " + dto.getEbdClassId()));

        if (enrollmentRepository.existsByStudentIdAndEbdClassId(student.getId(), ebdClass.getId())) {
            throw new BusinessRuleException("O aluno já está matriculado nesta classe.");
        }

        Enrollment enrollment = new Enrollment(student, ebdClass);
        Enrollment saved = enrollmentRepository.save(enrollment);

        // Atualiza a classe ativa do aluno
        student.setEbdClass(ebdClass);
        studentRepository.save(student);

        return toResponseDTO(saved);
    }

    /**
     * Retorna as matrículas de uma classe específica.
     *
     * @param classId identificador da classe
     * @return lista de matrículas da classe
     */
    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> getEnrollmentsByClass(Long classId) {
        return enrollmentRepository.findByEbdClassId(classId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retorna o histórico de matrículas de um aluno.
     *
     * @param studentId identificador do aluno
     * @return lista de matrículas do aluno
     */
    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Cancela uma matrícula existente pelo seu identificador.
     *
     * @param id identificador da matrícula
     */
    @Transactional
    public void cancelEnrollment(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula não encontrada com o ID: " + id));
        enrollmentRepository.delete(enrollment);
    }

    /**
     * Converte a entidade Enrollment para DTO.
     *
     * @param enrollment entidade a ser convertida
     * @return DTO correspondente
     */
    private EnrollmentResponseDTO toResponseDTO(Enrollment enrollment) {
        return new EnrollmentResponseDTO(
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getStudent().getNome(),
                enrollment.getEbdClass().getId(),
                enrollment.getEbdClass().getNome(),
                enrollment.getCreatedAt(),
                enrollment.getUpdatedAt()
        );
    }
}
