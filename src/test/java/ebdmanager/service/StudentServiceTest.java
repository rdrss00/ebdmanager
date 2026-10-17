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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private EbdStudentRepository studentRepository;

    @Mock
    private EbdClassRepository classRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    @DisplayName("Deve cadastrar um aluno e criar sua matrícula")
    void createStudent_Success() {
        EbdClass ebdClass = new EbdClass(1L, "Jovens");
        StudentRequestDTO request = new StudentRequestDTO("João Silva", 22, 1L);

        EbdStudent savedStudent = new EbdStudent(1L, "João Silva", 22, ebdClass);
        savedStudent.setCreatedAt(LocalDateTime.now());
        savedStudent.setUpdatedAt(LocalDateTime.now());

        when(classRepository.findById(1L)).thenReturn(Optional.of(ebdClass));
        when(studentRepository.save(any(EbdStudent.class))).thenReturn(savedStudent);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(new Enrollment(savedStudent, ebdClass));

        StudentResponseDTO response = studentService.createStudent(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("João Silva", response.getNome());
        assertEquals(22, response.getIdade());
        assertEquals(1L, response.getEbdClassId());
        assertEquals("Jovens", response.getEbdClassName());

        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando a classe não existir")
    void createStudent_ClassNotFound() {
        StudentRequestDTO request = new StudentRequestDTO("João Silva", 22, 99L);
        when(classRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.createStudent(request));
    }

    @Test
    @DisplayName("Deve buscar aluno por ID com sucesso")
    void getStudentById_Success() {
        EbdClass ebdClass = new EbdClass(1L, "Jovens");
        EbdStudent student = new EbdStudent(1L, "Maria", 20, ebdClass);
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentResponseDTO response = studentService.getStudentById(1L);

        assertEquals("Maria", response.getNome());
        assertEquals(20, response.getIdade());
    }

    @Test
    @DisplayName("Deve listar alunos com filtro por classe")
    void getAllStudents_WithClassFilter() {
        EbdClass ebdClass = new EbdClass(1L, "Jovens");
        EbdStudent student = new EbdStudent(1L, "Maria", 20, ebdClass);

        when(studentRepository.findByEbdClassId(1L)).thenReturn(List.of(student));

        List<StudentResponseDTO> result = studentService.getAllStudents(Optional.of(1L));

        assertEquals(1, result.size());
        assertEquals("Maria", result.get(0).getNome());
    }

    @Test
    @DisplayName("Deve atualizar aluno com sucesso")
    void updateStudent_Success() {
        EbdClass class1 = new EbdClass(1L, "Jovens");
        EbdClass class2 = new EbdClass(2L, "Adultos");
        EbdStudent student = new EbdStudent(1L, "Pedro", 25, class1);

        StudentRequestDTO request = new StudentRequestDTO("Pedro Santos", 26, 2L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(classRepository.findById(2L)).thenReturn(Optional.of(class2));
        when(enrollmentRepository.existsByStudentIdAndEbdClassId(1L, 2L)).thenReturn(false);
        when(studentRepository.save(any(EbdStudent.class))).thenReturn(student);

        StudentResponseDTO response = studentService.updateStudent(1L, request);

        assertEquals("Pedro Santos", response.getNome());
        assertEquals(26, response.getIdade());
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    @DisplayName("Deve remover aluno com sucesso")
    void deleteStudent_Success() {
        EbdClass ebdClass = new EbdClass(1L, "Jovens");
        EbdStudent student = new EbdStudent(1L, "Lucas", 18, ebdClass);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.deleteStudent(1L);

        verify(studentRepository).delete(student);
    }
}
