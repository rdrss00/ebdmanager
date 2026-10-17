package ebdmanager.service;

import ebdmanager.dto.AttendanceDateReportDTO;
import ebdmanager.dto.ClassAttendanceReportDTO;
import ebdmanager.dto.GeneralReportDTO;
import ebdmanager.dto.PeriodReportDTO;
import ebdmanager.dto.QuarterlyReportDTO;
import ebdmanager.dto.StudentAttendanceReportDTO;
import ebdmanager.exception.BusinessRuleException;
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
import ebdmanager.repository.EnrollmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private EbdClassRepository classRepository;

    @Mock
    private EbdStudentRepository studentRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private AttendanceStudentRepository attendanceStudentRepository;

    @Mock
    private AttendanceOfferingRepository attendanceOfferingRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    @DisplayName("Deve gerar relatório por data com sucesso")
    void getReportByDate_Success() {
        LocalDate date = LocalDate.of(2026, 10, 18);
        EbdClass ebdClass = new EbdClass(1L, "Jovens");
        Attendance attendance = new Attendance(100L, ebdClass, date.atTime(9, 0));
        EbdStudent student = new EbdStudent(1L, "Lucas", 20, ebdClass);

        AttendanceStudent r1 = new AttendanceStudent(1L, attendance, student, true, true, true, 5.0);
        AttendanceOffering offering = new AttendanceOffering(attendance, 5.0);

        when(attendanceRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(any(), any()))
                .thenReturn(List.of(attendance));
        when(attendanceStudentRepository.findByAttendanceId(100L)).thenReturn(List.of(r1));
        when(attendanceOfferingRepository.findByAttendanceId(100L)).thenReturn(Optional.of(offering));

        AttendanceDateReportDTO report = reportService.getReportByDate(date);

        assertNotNull(report);
        assertEquals(date, report.getDate());
        assertEquals(1, report.getTotalClassesHeld());
        assertEquals(1, report.getTotalPresent());
        assertEquals(0, report.getTotalAbsent());
        assertEquals(100.0, report.getOverallAttendanceRate());
        assertEquals(5.0, report.getTotalOfferings());
        assertEquals(1, report.getTotalBibles());
        assertEquals(1, report.getTotalBooks());
    }

    @Test
    @DisplayName("Deve gerar relatório por aluno com sucesso")
    void getReportByStudent_Success() {
        EbdClass ebdClass = new EbdClass(1L, "Jovens");
        EbdStudent student = new EbdStudent(1L, "Lucas", 20, ebdClass);
        Attendance attendance1 = new Attendance(100L, ebdClass, LocalDateTime.now().minusDays(7));
        Attendance attendance2 = new Attendance(101L, ebdClass, LocalDateTime.now());

        AttendanceStudent r1 = new AttendanceStudent(1L, attendance1, student, true, true, true, 10.0);
        r1.setCreatedAt(attendance1.getCreatedAt());
        AttendanceStudent r2 = new AttendanceStudent(2L, attendance2, student, false, false, false, 0.0);
        r2.setCreatedAt(attendance2.getCreatedAt());

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(attendanceStudentRepository.findByStudentIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(r1, r2));

        StudentAttendanceReportDTO report = reportService.getReportByStudent(1L, Optional.empty(), Optional.empty());

        assertNotNull(report);
        assertEquals(1L, report.getStudentId());
        assertEquals("Lucas", report.getStudentName());
        assertEquals(2, report.getTotalSessions());
        assertEquals(1, report.getPresentCount());
        assertEquals(1, report.getAbsentCount());
        assertEquals(50.0, report.getAttendanceRate());
        assertEquals(10.0, report.getTotalOffering());
        assertEquals(1, report.getBibleCount());
        assertEquals(1, report.getBookCount());
        assertEquals(2, report.getSessions().size());
    }

    @Test
    @DisplayName("Deve gerar relatório por classe com sucesso")
    void getReportByClass_Success() {
        EbdClass ebdClass = new EbdClass(1L, "Jovens");
        Attendance attendance = new Attendance(100L, ebdClass, LocalDateTime.now());
        EbdStudent student = new EbdStudent(1L, "Lucas", 20, ebdClass);

        AttendanceStudent r1 = new AttendanceStudent(1L, attendance, student, true, true, true, 15.0);
        AttendanceOffering offering = new AttendanceOffering(attendance, 15.0);

        when(classRepository.findById(1L)).thenReturn(Optional.of(ebdClass));
        when(studentRepository.countByEbdClassId(1L)).thenReturn(10L);
        when(attendanceRepository.findByEbdClassId(1L)).thenReturn(List.of(attendance));
        when(attendanceStudentRepository.findByAttendanceId(100L)).thenReturn(List.of(r1));
        when(attendanceOfferingRepository.findByAttendanceId(100L)).thenReturn(Optional.of(offering));

        ClassAttendanceReportDTO report = reportService.getReportByClass(1L, Optional.empty(), Optional.empty());

        assertNotNull(report);
        assertEquals("Jovens", report.getClassName());
        assertEquals(10L, report.getTotalEnrolledStudents());
        assertEquals(1, report.getTotalSessions());
        assertEquals(1, report.getTotalPresent());
        assertEquals(0, report.getTotalAbsent());
        assertEquals(100.0, report.getAttendanceRate());
        assertEquals(15.0, report.getTotalOfferings());
    }

    @Test
    @DisplayName("Deve gerar relatório por período geral com sucesso")
    void getReportByPeriod_Success() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 3, 31);

        EbdClass ebdClass = new EbdClass(1L, "Jovens");
        Attendance attendance = new Attendance(100L, ebdClass, start.atTime(10, 0));
        EbdStudent student = new EbdStudent(1L, "Lucas", 20, ebdClass);

        AttendanceStudent r1 = new AttendanceStudent(1L, attendance, student, true, true, true, 20.0);
        AttendanceOffering offering = new AttendanceOffering(attendance, 20.0);

        when(attendanceRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(any(), any()))
                .thenReturn(List.of(attendance));
        when(attendanceStudentRepository.findByAttendanceId(100L)).thenReturn(List.of(r1));
        when(attendanceOfferingRepository.findByAttendanceId(100L)).thenReturn(Optional.of(offering));

        PeriodReportDTO report = reportService.getReportByPeriod(start, end);

        assertNotNull(report);
        assertEquals(1, report.getTotalSessions());
        assertEquals(1, report.getTotalPresent());
        assertEquals(100.0, report.getOverallAttendanceRate());
        assertEquals(20.0, report.getTotalOfferings());
        assertEquals(1, report.getClassBreakdowns().size());
    }

    @Test
    @DisplayName("Deve lançar BusinessRuleException quando data inicial for após a data final")
    void getReportByPeriod_InvalidDates() {
        LocalDate start = LocalDate.of(2026, 5, 1);
        LocalDate end = LocalDate.of(2026, 4, 1);

        assertThrows(BusinessRuleException.class, () -> reportService.getReportByPeriod(start, end));
    }

    @Test
    @DisplayName("Deve gerar relatório geral quantitativo")
    void getGeneralReport_Success() {
        EbdClass c1 = new EbdClass(1L, "Classe A");
        EbdClass c2 = new EbdClass(2L, "Classe B");

        when(classRepository.findAll()).thenReturn(List.of(c1, c2));
        when(studentRepository.count()).thenReturn(20L);
        when(studentRepository.countByEbdClassId(1L)).thenReturn(8L);
        when(studentRepository.countByEbdClassId(2L)).thenReturn(12L);
        when(enrollmentRepository.countByEbdClassId(1L)).thenReturn(8L);
        when(enrollmentRepository.countByEbdClassId(2L)).thenReturn(12L);

        GeneralReportDTO report = reportService.getGeneralReport();

        assertNotNull(report);
        assertEquals(2, report.getTotalClasses());
        assertEquals(20, report.getTotalStudents());
        assertEquals(10.0, report.getAverageStudentsPerClass());
        assertEquals(2, report.getClasses().size());
    }

    @Test
    @DisplayName("Deve gerar relatório por trimestre com sucesso")
    void getQuarterlyReport_Success() {
        when(classRepository.count()).thenReturn(3L);
        when(studentRepository.count()).thenReturn(30L);
        when(attendanceRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(any(), any()))
                .thenReturn(List.of());

        QuarterlyReportDTO report = reportService.getQuarterlyReport(2026, 1);

        assertNotNull(report);
        assertEquals(2026, report.getYear());
        assertEquals(1, report.getQuarter());
        assertEquals(LocalDate.of(2026, 1, 1), report.getStartDate());
        assertEquals(LocalDate.of(2026, 3, 31), report.getEndDate());
        assertEquals(3L, report.getTotalClasses());
        assertEquals(30L, report.getTotalStudents());
        assertEquals(10.0, report.getAverageStudentsPerClass());
    }

    @Test
    @DisplayName("Deve lançar BusinessRuleException quando trimestre for inválido")
    void getQuarterlyReport_InvalidQuarter() {
        assertThrows(BusinessRuleException.class, () -> reportService.getQuarterlyReport(2026, 5));
    }
}
