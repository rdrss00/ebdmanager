package ebdmanager.service;

import ebdmanager.dto.AttendanceDateReportDTO;
import ebdmanager.dto.ClassAttendanceReportDTO;
import ebdmanager.dto.ClassPeriodSummaryDTO;
import ebdmanager.dto.ClassStudentCountDTO;
import ebdmanager.dto.GeneralReportDTO;
import ebdmanager.dto.QuarterlyReportDTO;
import ebdmanager.dto.StudentAttendanceReportDTO;
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
import ebdmanager.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Serviço responsável pela geração de relatórios estatísticos e analíticos da EBD.
 */
@Service
public class ReportService {

    private final EbdClassRepository classRepository;
    private final EbdStudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceStudentRepository attendanceStudentRepository;
    private final AttendanceOfferingRepository attendanceOfferingRepository;

    /**
     * Construtor para injeção de dependência.
     *
     * @param classRepository              repositório de classes
     * @param studentRepository            repositório de alunos
     * @param enrollmentRepository         repositório de matrículas
     * @param attendanceRepository         repositório de chamadas
     * @param attendanceStudentRepository  repositório de presenças individuais
     * @param attendanceOfferingRepository repositório de ofertas
     */
    public ReportService(EbdClassRepository classRepository,
                         EbdStudentRepository studentRepository,
                         EnrollmentRepository enrollmentRepository,
                         AttendanceRepository attendanceRepository,
                         AttendanceStudentRepository attendanceStudentRepository,
                         AttendanceOfferingRepository attendanceOfferingRepository) {
        this.classRepository = classRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.attendanceRepository = attendanceRepository;
        this.attendanceStudentRepository = attendanceStudentRepository;
        this.attendanceOfferingRepository = attendanceOfferingRepository;
    }

    /**
     * Gera relatório de frequência e participação consolidado por data.
     *
     * @param date data a ser analisada
     * @return relatório do dia informado
     */
    @Transactional(readOnly = true)
    public AttendanceDateReportDTO getReportByDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Attendance> attendances = attendanceRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end);
        List<ClassPeriodSummaryDTO> breakdowns = buildClassSummaries(attendances);

        int totalPresent = breakdowns.stream().mapToInt(ClassPeriodSummaryDTO::getTotalPresent).sum();
        int totalAbsent = breakdowns.stream().mapToInt(ClassPeriodSummaryDTO::getTotalAbsent).sum();
        int totalBibles = breakdowns.stream().mapToInt(ClassPeriodSummaryDTO::getTotalBibles).sum();
        int totalBooks = breakdowns.stream().mapToInt(ClassPeriodSummaryDTO::getTotalBooks).sum();
        double totalOfferings = breakdowns.stream().mapToDouble(ClassPeriodSummaryDTO::getTotalOfferings).sum();
        double rate = calculatePercentage(totalPresent, totalPresent + totalAbsent);

        return new AttendanceDateReportDTO(
                date,
                attendances.size(),
                totalPresent,
                totalAbsent,
                rate,
                totalBibles,
                totalBooks,
                roundDouble(totalOfferings),
                breakdowns
        );
    }

    /**
     * Gera relatório de frequência e participação individual de um aluno.
     *
     * @param studentId identificador do aluno
     * @param startDate data inicial opcional
     * @param endDate   data final opcional
     * @return relatório do aluno
     */
    @Transactional(readOnly = true)
    public StudentAttendanceReportDTO getReportByStudent(Long studentId, Optional<LocalDate> startDate, Optional<LocalDate> endDate) {
        EbdStudent student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com o ID: " + studentId));

        List<AttendanceStudent> records;
        if (startDate.isPresent() && endDate.isPresent()) {
            LocalDateTime start = startDate.get().atStartOfDay();
            LocalDateTime end = endDate.get().atTime(LocalTime.MAX);
            records = attendanceStudentRepository.findByStudentIdAndCreatedAtBetweenOrderByCreatedAtDesc(studentId, start, end);
        } else {
            records = attendanceStudentRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
        }

        int presentCount = 0;
        int absentCount = 0;
        int bibleCount = 0;
        int bookCount = 0;
        double totalOffering = 0.0;
        List<StudentAttendanceReportDTO.StudentSessionHistoryDTO> sessions = new ArrayList<>();

        for (AttendanceStudent r : records) {
            if (r.isPresent()) {
                presentCount++;
            } else {
                absentCount++;
            }
            if (r.isBible()) {
                bibleCount++;
            }
            if (r.isBook()) {
                bookCount++;
            }
            totalOffering += r.getOffering();

            sessions.add(new StudentAttendanceReportDTO.StudentSessionHistoryDTO(
                    r.getAttendance().getId(),
                    r.getCreatedAt(),
                    r.isPresent(),
                    r.isBook(),
                    r.isBible(),
                    r.getOffering()
            ));
        }

        int totalSessions = records.size();
        double attendanceRate = calculatePercentage(presentCount, totalSessions);

        return new StudentAttendanceReportDTO(
                student.getId(),
                student.getNome(),
                student.getIdade(),
                student.getEbdClass().getNome(),
                totalSessions,
                presentCount,
                absentCount,
                attendanceRate,
                bibleCount,
                bookCount,
                roundDouble(totalOffering),
                sessions
        );
    }

    /**
     * Gera relatório de frequência e participação de uma classe em um período ou geral.
     *
     * @param classId   identificador da classe
     * @param startDate data inicial opcional
     * @param endDate   data final opcional
     * @return relatório da classe
     */
    @Transactional(readOnly = true)
    public ClassAttendanceReportDTO getReportByClass(Long classId, Optional<LocalDate> startDate, Optional<LocalDate> endDate) {
        EbdClass ebdClass = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Classe não encontrada com o ID: " + classId));

        long enrolledCount = studentRepository.countByEbdClassId(classId);

        List<Attendance> attendances;
        if (startDate.isPresent() && endDate.isPresent()) {
            LocalDateTime start = startDate.get().atStartOfDay();
            LocalDateTime end = endDate.get().atTime(LocalTime.MAX);
            attendances = attendanceRepository.findByEbdClassIdAndCreatedAtBetweenOrderByCreatedAtDesc(classId, start, end);
        } else {
            attendances = attendanceRepository.findByEbdClassId(classId);
        }

        int totalPresent = 0;
        int totalAbsent = 0;
        int totalBibles = 0;
        int totalBooks = 0;
        double totalOfferings = 0.0;
        List<ClassAttendanceReportDTO.ClassSessionHistoryDTO> sessions = new ArrayList<>();

        for (Attendance att : attendances) {
            List<AttendanceStudent> records = attendanceStudentRepository.findByAttendanceId(att.getId());
            double sessionOffering = attendanceOfferingRepository.findByAttendanceId(att.getId())
                    .map(AttendanceOffering::getTotalOffering)
                    .orElse(0.0);

            int pCount = 0;
            int aCount = 0;
            for (AttendanceStudent r : records) {
                if (r.isPresent()) {
                    pCount++;
                } else {
                    aCount++;
                }
                if (r.isBible()) {
                    totalBibles++;
                }
                if (r.isBook()) {
                    totalBooks++;
                }
            }

            totalPresent += pCount;
            totalAbsent += aCount;
            totalOfferings += sessionOffering;

            sessions.add(new ClassAttendanceReportDTO.ClassSessionHistoryDTO(
                    att.getId(),
                    att.getCreatedAt(),
                    pCount,
                    aCount,
                    sessionOffering
            ));
        }

        double rate = calculatePercentage(totalPresent, totalPresent + totalAbsent);

        return new ClassAttendanceReportDTO(
                ebdClass.getId(),
                ebdClass.getNome(),
                enrolledCount,
                attendances.size(),
                totalPresent,
                totalAbsent,
                rate,
                totalBibles,
                totalBooks,
                roundDouble(totalOfferings),
                sessions
        );
    }

    /**
     * Gera relatório de frequência e participação em um período geral.
     *
     * @param startDate data inicial do período
     * @param endDate   data final do período
     * @return relatório geral do período com divisão por turmas
     */
    @Transactional(readOnly = true)
    public ebdmanager.dto.PeriodReportDTO getReportByPeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new BusinessRuleException("A data inicial não pode ser posterior à data final.");
        }

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<Attendance> attendances = attendanceRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end);
        List<ClassPeriodSummaryDTO> breakdowns = buildClassSummaries(attendances);

        int totalPresent = breakdowns.stream().mapToInt(ClassPeriodSummaryDTO::getTotalPresent).sum();
        int totalAbsent = breakdowns.stream().mapToInt(ClassPeriodSummaryDTO::getTotalAbsent).sum();
        int totalBibles = breakdowns.stream().mapToInt(ClassPeriodSummaryDTO::getTotalBibles).sum();
        int totalBooks = breakdowns.stream().mapToInt(ClassPeriodSummaryDTO::getTotalBooks).sum();
        double totalOfferings = breakdowns.stream().mapToDouble(ClassPeriodSummaryDTO::getTotalOfferings).sum();
        double rate = calculatePercentage(totalPresent, totalPresent + totalAbsent);

        return new ebdmanager.dto.PeriodReportDTO(
                start,
                end,
                attendances.size(),
                totalPresent,
                totalAbsent,
                rate,
                totalBibles,
                totalBooks,
                roundDouble(totalOfferings),
                breakdowns
        );
    }

    /**
     * Gera relatório quantitativo geral da EBD (alunos, classes e distribuição).
     *
     * @return resumo geral quantitativo
     */
    @Transactional(readOnly = true)
    public GeneralReportDTO getGeneralReport() {
        List<EbdClass> classes = classRepository.findAll();
        long totalClasses = classes.size();
        long totalStudents = studentRepository.count();

        double avgStudents = totalClasses > 0 ? (double) totalStudents / totalClasses : 0.0;
        avgStudents = roundDouble(avgStudents);

        List<ClassStudentCountDTO> classDTOs = new ArrayList<>();
        for (EbdClass c : classes) {
            long studentCount = studentRepository.countByEbdClassId(c.getId());
            long enrollmentCount = enrollmentRepository.countByEbdClassId(c.getId());
            classDTOs.add(new ClassStudentCountDTO(c.getId(), c.getNome(), studentCount, enrollmentCount));
        }

        return new GeneralReportDTO(totalClasses, totalStudents, avgStudents, classDTOs);
    }

    /**
     * Gera relatório geral e analítico consolidado por trimestre de um ano.
     *
     * @param year    ano civil desejado
     * @param quarter trimestre desejado (1 a 4)
     * @return relatório consolidado do trimestre
     */
    @Transactional(readOnly = true)
    public QuarterlyReportDTO getQuarterlyReport(int year, int quarter) {
        if (quarter < 1 || quarter > 4) {
            throw new BusinessRuleException("Trimestre inválido. Escolha um valor entre 1 e 4.");
        }

        LocalDate startDate;
        LocalDate endDate;

        switch (quarter) {
            case 1 -> {
                startDate = LocalDate.of(year, 1, 1);
                endDate = LocalDate.of(year, 3, 31);
            }
            case 2 -> {
                startDate = LocalDate.of(year, 4, 1);
                endDate = LocalDate.of(year, 6, 30);
            }
            case 3 -> {
                startDate = LocalDate.of(year, 7, 1);
                endDate = LocalDate.of(year, 9, 30);
            }
            default -> {
                startDate = LocalDate.of(year, 10, 1);
                endDate = LocalDate.of(year, 12, 31);
            }
        }

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        long totalClasses = classRepository.count();
        long totalStudents = studentRepository.count();
        double avgStudents = totalClasses > 0 ? roundDouble((double) totalStudents / totalClasses) : 0.0;

        List<Attendance> attendances = attendanceRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end);
        List<ClassPeriodSummaryDTO> breakdowns = buildClassSummaries(attendances);

        int totalPresent = breakdowns.stream().mapToInt(ClassPeriodSummaryDTO::getTotalPresent).sum();
        int totalAbsent = breakdowns.stream().mapToInt(ClassPeriodSummaryDTO::getTotalAbsent).sum();
        int totalBibles = breakdowns.stream().mapToInt(ClassPeriodSummaryDTO::getTotalBibles).sum();
        int totalBooks = breakdowns.stream().mapToInt(ClassPeriodSummaryDTO::getTotalBooks).sum();
        double totalOfferings = breakdowns.stream().mapToDouble(ClassPeriodSummaryDTO::getTotalOfferings).sum();
        double rate = calculatePercentage(totalPresent, totalPresent + totalAbsent);

        return new QuarterlyReportDTO(
                year,
                quarter,
                startDate,
                endDate,
                totalClasses,
                totalStudents,
                avgStudents,
                attendances.size(),
                totalPresent,
                totalAbsent,
                rate,
                totalBibles,
                totalBooks,
                roundDouble(totalOfferings),
                breakdowns
        );
    }

    /**
     * Agrupa e consolida métricas de presença por classe a partir de uma lista de chamadas.
     *
     * @param attendances lista de chamadas do período
     * @return lista de resumos por classe
     */
    private List<ClassPeriodSummaryDTO> buildClassSummaries(List<Attendance> attendances) {
        Map<Long, ClassPeriodSummaryDTO> summaryMap = new LinkedHashMap<>();

        for (Attendance att : attendances) {
            EbdClass ebdClass = att.getEbdClass();
            ClassPeriodSummaryDTO summary = summaryMap.computeIfAbsent(ebdClass.getId(), id ->
                    new ClassPeriodSummaryDTO(id, ebdClass.getNome(), 0, 0, 0, 0.0, 0, 0, 0.0)
            );

            summary.setTotalSessions(summary.getTotalSessions() + 1);

            List<AttendanceStudent> records = attendanceStudentRepository.findByAttendanceId(att.getId());
            for (AttendanceStudent r : records) {
                if (r.isPresent()) {
                    summary.setTotalPresent(summary.getTotalPresent() + 1);
                } else {
                    summary.setTotalAbsent(summary.getTotalAbsent() + 1);
                }
                if (r.isBible()) {
                    summary.setTotalBibles(summary.getTotalBibles() + 1);
                }
                if (r.isBook()) {
                    summary.setTotalBooks(summary.getTotalBooks() + 1);
                }
            }

            double sessionOffering = attendanceOfferingRepository.findByAttendanceId(att.getId())
                    .map(AttendanceOffering::getTotalOffering)
                    .orElse(0.0);
            summary.setTotalOfferings(roundDouble(summary.getTotalOfferings() + sessionOffering));
        }

        for (ClassPeriodSummaryDTO summary : summaryMap.values()) {
            double rate = calculatePercentage(summary.getTotalPresent(), summary.getTotalPresent() + summary.getTotalAbsent());
            summary.setAttendanceRate(rate);
        }

        return new ArrayList<>(summaryMap.values());
    }

    /**
     * Calcula a porcentagem arredondada para duas casas decimais.
     *
     * @param part  valor parcial
     * @param total valor total
     * @return porcentagem calculada (0.0 a 100.0)
     */
    private double calculatePercentage(int part, int total) {
        if (total == 0) {
            return 0.0;
        }
        double value = ((double) part / total) * 100.0;
        return roundDouble(value);
    }

    /**
     * Arredonda um valor double para 2 casas decimais.
     *
     * @param value valor a ser arredondado
     * @return valor com duas casas decimais
     */
    private double roundDouble(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
