package ebdmanager.service;

import ebdmanager.dto.StudentRequestDTO;
import ebdmanager.dto.StudentResponseDTO;
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
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelas regras de negócio e operações de CRUD dos alunos da EBD.
 */
@Service
public class StudentService {

    private final EbdStudentRepository studentRepository;
    private final EbdClassRepository classRepository;
    private final EnrollmentRepository enrollmentRepository;

    /**
     * Construtor para injeção de dependência.
     *
     * @param studentRepository    repositório de alunos
     * @param classRepository      repositório de classes
     * @param enrollmentRepository repositório de matrículas
     */
    public StudentService(EbdStudentRepository studentRepository,
                          EbdClassRepository classRepository,
                          EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.classRepository = classRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    /**
     * Cadastra um novo aluno na EBD e vincula sua matrícula à classe informada.
     *
     * @param dto dados para cadastro do aluno
     * @return informações do aluno cadastrado
     */
    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO dto) {
        EbdClass ebdClass = classRepository.findById(dto.getEbdClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Classe não encontrada com o ID: " + dto.getEbdClassId()));

        EbdStudent student = new EbdStudent(dto.getNome().trim(), dto.getIdade(), ebdClass);
        EbdStudent savedStudent = studentRepository.save(student);

        // Criação automática do registro de matrícula
        Enrollment enrollment = new Enrollment(savedStudent, ebdClass);
        enrollmentRepository.save(enrollment);

        return toResponseDTO(savedStudent);
    }

    /**
     * Retorna a lista de alunos, opcionalmente filtrada por classe.
     *
     * @param classId filtro opcional pelo ID da classe
     * @return lista de alunos
     */
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAllStudents(Optional<Long> classId) {
        List<EbdStudent> students = classId
                .map(studentRepository::findByEbdClassId)
                .orElseGet(studentRepository::findAll);

        return students.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca um aluno pelo seu identificador.
     *
     * @param id identificador do aluno
     * @return aluno encontrado
     */
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(Long id) {
        EbdStudent student = findEntityById(id);
        return toResponseDTO(student);
    }

    /**
     * Atualiza as informações cadastrais de um aluno.
     *
     * @param id  identificador do aluno
     * @param dto novos dados do aluno
     * @return aluno atualizado
     */
    @Transactional
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto) {
        EbdStudent student = findEntityById(id);

        student.setNome(dto.getNome().trim());
        student.setIdade(dto.getIdade());

        if (!student.getEbdClass().getId().equals(dto.getEbdClassId())) {
            EbdClass newClass = classRepository.findById(dto.getEbdClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Classe não encontrada com o ID: " + dto.getEbdClassId()));
            student.setEbdClass(newClass);

            // Se for transferido para outra classe, registra uma nova matrícula caso ainda não exista
            if (!enrollmentRepository.existsByStudentIdAndEbdClassId(student.getId(), newClass.getId())) {
                Enrollment enrollment = new Enrollment(student, newClass);
                enrollmentRepository.save(enrollment);
            }
        }

        EbdStudent updated = studentRepository.save(student);
        return toResponseDTO(updated);
    }

    /**
     * Remove um aluno do sistema pelo seu identificador.
     *
     * @param id identificador do aluno
     */
    @Transactional
    public void deleteStudent(Long id) {
        EbdStudent student = findEntityById(id);
        studentRepository.delete(student);
    }

    /**
     * Busca a entidade do aluno ou lança exceção caso não encontrada.
     *
     * @param id identificador do aluno
     * @return entidade do aluno
     */
    @Transactional(readOnly = true)
    public EbdStudent findEntityById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com o ID: " + id));
    }

    /**
     * Converte a entidade EbdStudent para DTO.
     *
     * @param student entidade a ser convertida
     * @return DTO correspondente
     */
    private StudentResponseDTO toResponseDTO(EbdStudent student) {
        return new StudentResponseDTO(
                student.getId(),
                student.getNome(),
                student.getIdade(),
                student.getEbdClass().getId(),
                student.getEbdClass().getNome(),
                student.getCreatedAt(),
                student.getUpdatedAt()
        );
    }
}
