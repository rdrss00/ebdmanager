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
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private EbdStudentRepository studentRepository;

    @Mock
    private EbdClassRepository classRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @Test
    @DisplayName("Deve matricular aluno com sucesso")
    void enrollStudent_Success() {
        EbdClass ebdClass = new EbdClass(1L, "Jovens");
        EbdStudent student = new EbdStudent(2L, "Felipe", 21, ebdClass);
        Enrollment savedEnrollment = new Enrollment(10L, student, ebdClass);
        savedEnrollment.setCreatedAt(LocalDateTime.now());
        savedEnrollment.setUpdatedAt(LocalDateTime.now());

        EnrollmentRequestDTO request = new EnrollmentRequestDTO(2L, 1L);

        when(studentRepository.findById(2L)).thenReturn(Optional.of(student));
        when(classRepository.findById(1L)).thenReturn(Optional.of(ebdClass));
        when(enrollmentRepository.existsByStudentIdAndEbdClassId(2L, 1L)).thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(savedEnrollment);

        EnrollmentResponseDTO response = enrollmentService.enrollStudent(request);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Felipe", response.getStudentName());
        assertEquals("Jovens", response.getEbdClassName());
    }

    @Test
    @DisplayName("Deve lançar BusinessRuleException quando aluno já estiver matriculado na classe")
    void enrollStudent_AlreadyEnrolled() {
        EbdClass ebdClass = new EbdClass(1L, "Jovens");
        EbdStudent student = new EbdStudent(2L, "Felipe", 21, ebdClass);
        EnrollmentRequestDTO request = new EnrollmentRequestDTO(2L, 1L);

        when(studentRepository.findById(2L)).thenReturn(Optional.of(student));
        when(classRepository.findById(1L)).thenReturn(Optional.of(ebdClass));
        when(enrollmentRepository.existsByStudentIdAndEbdClassId(2L, 1L)).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> enrollmentService.enrollStudent(request));
    }

    @Test
    @DisplayName("Deve listar matrículas por classe")
    void getEnrollmentsByClass_Success() {
        EbdClass ebdClass = new EbdClass(1L, "Jovens");
        EbdStudent student = new EbdStudent(2L, "Felipe", 21, ebdClass);
        Enrollment enrollment = new Enrollment(10L, student, ebdClass);

        when(enrollmentRepository.findByEbdClassId(1L)).thenReturn(List.of(enrollment));

        List<EnrollmentResponseDTO> result = enrollmentService.getEnrollmentsByClass(1L);

        assertEquals(1, result.size());
        assertEquals("Felipe", result.get(0).getStudentName());
    }

    @Test
    @DisplayName("Deve cancelar matrícula com sucesso")
    void cancelEnrollment_Success() {
        EbdClass ebdClass = new EbdClass(1L, "Jovens");
        EbdStudent student = new EbdStudent(2L, "Felipe", 21, ebdClass);
        Enrollment enrollment = new Enrollment(10L, student, ebdClass);

        when(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment));

        enrollmentService.cancelEnrollment(10L);

        verify(enrollmentRepository).delete(enrollment);
    }
}
