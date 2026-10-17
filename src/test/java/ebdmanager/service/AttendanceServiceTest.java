package ebdmanager.service;

import ebdmanager.dto.AttendanceCreateDTO;
import ebdmanager.dto.AttendanceRecordItemDTO;
import ebdmanager.dto.AttendanceResponseDTO;
import ebdmanager.exception.BusinessRuleException;
import ebdmanager.exception.ResourceNotFoundException;
import ebdmanager.model.Attendance;
import ebdmanager.model.AttendanceOffering;
import ebdmanager.model.AttendanceStudent;
import ebdmanager.model.EbdClass;
import ebdmanager.model.EbdStudent;
import ebdmanager.repository.AttendanceOfferingRepository;
import ebdmanager.repository.AttendanceRepository;
import ebdmanager.repository.AttendanceStudentRepository;
import ebdmanager.repository.EbdClassRepository;
import ebdmanager.repository.EbdStudentRepository;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private AttendanceStudentRepository attendanceStudentRepository;

    @Mock
    private AttendanceOfferingRepository attendanceOfferingRepository;

    @Mock
    private EbdClassRepository classRepository;

    @Mock
    private EbdStudentRepository studentRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    @Test
    @DisplayName("Deve registrar a chamada de uma classe com sucesso")
    void recordAttendance_Success() {
        EbdClass ebdClass = new EbdClass(1L, "Jovens");
        EbdStudent s1 = new EbdStudent(10L, "Lucas", 20, ebdClass);
        EbdStudent s2 = new EbdStudent(11L, "Ana", 19, ebdClass);

        AttendanceCreateDTO request = new AttendanceCreateDTO(
                1L,
                LocalDateTime.now(),
                null,
                List.of(
                        new AttendanceRecordItemDTO(10L, true, true, true, 5.0),
                        new AttendanceRecordItemDTO(11L, false, false, false, 0.0)
                )
        );

        Attendance savedAttendance = new Attendance(100L, ebdClass, request.getCreatedAt());

        when(classRepository.findById(1L)).thenReturn(Optional.of(ebdClass));
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(savedAttendance);
        when(studentRepository.findById(10L)).thenReturn(Optional.of(s1));
        when(studentRepository.findById(11L)).thenReturn(Optional.of(s2));

        AttendanceStudent r1 = new AttendanceStudent(1L, savedAttendance, s1, true, true, true, 5.0);
        r1.setCreatedAt(request.getCreatedAt());
        AttendanceStudent r2 = new AttendanceStudent(2L, savedAttendance, s2, false, false, false, 0.0);
        r2.setCreatedAt(request.getCreatedAt());

        when(attendanceStudentRepository.saveAll(anyList())).thenReturn(List.of(r1, r2));

        AttendanceResponseDTO response = attendanceService.recordAttendance(request);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("Jovens", response.getEbdClassName());
        assertEquals(1, response.getTotalPresent());
        assertEquals(1, response.getTotalAbsent());
        assertEquals(5.0, response.getTotalOffering());
        assertEquals(2, response.getStudents().size());

        verify(attendanceOfferingRepository).save(any(AttendanceOffering.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessRuleException quando aluno for duplicado na chamada")
    void recordAttendance_DuplicateStudent() {
        EbdClass ebdClass = new EbdClass(1L, "Jovens");
        AttendanceCreateDTO request = new AttendanceCreateDTO(
                1L,
                LocalDateTime.now(),
                10.0,
                List.of(
                        new AttendanceRecordItemDTO(10L, true, true, true, 5.0),
                        new AttendanceRecordItemDTO(10L, true, true, true, 5.0)
                )
        );

        when(classRepository.findById(1L)).thenReturn(Optional.of(ebdClass));
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(new Attendance(100L, ebdClass, request.getCreatedAt()));
        when(studentRepository.findById(10L)).thenReturn(Optional.of(new EbdStudent(10L, "Lucas", 20, ebdClass)));

        assertThrows(BusinessRuleException.class, () -> attendanceService.recordAttendance(request));
    }

    @Test
    @DisplayName("Deve buscar chamada por ID")
    void getAttendanceById_Success() {
        EbdClass ebdClass = new EbdClass(1L, "Jovens");
        EbdStudent student = new EbdStudent(10L, "Lucas", 20, ebdClass);
        Attendance attendance = new Attendance(100L, ebdClass, LocalDateTime.now());

        AttendanceStudent record = new AttendanceStudent(1L, attendance, student, true, true, true, 10.0);
        record.setCreatedAt(attendance.getCreatedAt());

        AttendanceOffering offering = new AttendanceOffering(attendance, 10.0);

        when(attendanceRepository.findById(100L)).thenReturn(Optional.of(attendance));
        when(attendanceStudentRepository.findByAttendanceId(100L)).thenReturn(List.of(record));
        when(attendanceOfferingRepository.findByAttendanceId(100L)).thenReturn(Optional.of(offering));

        AttendanceResponseDTO response = attendanceService.getAttendanceById(100L);

        assertEquals(100L, response.getId());
        assertEquals(1, response.getTotalPresent());
        assertEquals(10.0, response.getTotalOffering());
    }
}
