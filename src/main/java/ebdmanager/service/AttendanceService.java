package ebdmanager.service;

import ebdmanager.dto.AttendanceCreateDTO;
import ebdmanager.dto.AttendanceRecordItemDTO;
import ebdmanager.dto.AttendanceResponseDTO;
import ebdmanager.dto.AttendanceStudentResponseDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelo registro e consulta de chamadas/presenças de turmas da EBD.
 */
@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceStudentRepository attendanceStudentRepository;
    private final AttendanceOfferingRepository attendanceOfferingRepository;
    private final EbdClassRepository classRepository;
    private final EbdStudentRepository studentRepository;

    /**
     * Construtor para injeção de dependência.
     *
     * @param attendanceRepository         repositório de chamadas
     * @param attendanceStudentRepository  repositório de presenças individuais
     * @param attendanceOfferingRepository repositório de ofertas de chamadas
     * @param classRepository              repositório de classes
     * @param studentRepository            repositório de alunos
     */
    public AttendanceService(AttendanceRepository attendanceRepository,
                             AttendanceStudentRepository attendanceStudentRepository,
                             AttendanceOfferingRepository attendanceOfferingRepository,
                             EbdClassRepository classRepository,
                             EbdStudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.attendanceStudentRepository = attendanceStudentRepository;
        this.attendanceOfferingRepository = attendanceOfferingRepository;
        this.classRepository = classRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Registra a chamada completa de uma classe em uma data específica.
     *
     * @param dto dados da chamada e itens de presença dos alunos
     * @return detalhes da chamada registrada
     */
    @Transactional
    public AttendanceResponseDTO recordAttendance(AttendanceCreateDTO dto) {
        EbdClass ebdClass = classRepository.findById(dto.getEbdClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Classe não encontrada com o ID: " + dto.getEbdClassId()));

        LocalDateTime sessionDate = dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now();

        Attendance attendance = new Attendance(ebdClass, sessionDate);
        Attendance savedAttendance = attendanceRepository.save(attendance);

        Set<Long> processedStudentIds = new HashSet<>();
        double calculatedOfferings = 0.0;
        List<AttendanceStudent> studentRecords = new ArrayList<>();

        for (AttendanceRecordItemDTO item : dto.getStudents()) {
            if (!processedStudentIds.add(item.getStudentId())) {
                throw new BusinessRuleException("Aluno com ID " + item.getStudentId() + " duplicado no envio da mesma chamada.");
            }

            EbdStudent student = studentRepository.findById(item.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com o ID: " + item.getStudentId()));

            double offering = item.getOffering() != null ? item.getOffering() : 0.0;
            calculatedOfferings += offering;

            AttendanceStudent record = new AttendanceStudent(
                    savedAttendance,
                    student,
                    item.isPresent(),
                    item.isBook(),
                    item.isBible(),
                    offering
            );
            record.setCreatedAt(sessionDate);
            studentRecords.add(record);
        }

        List<AttendanceStudent> savedRecords = attendanceStudentRepository.saveAll(studentRecords);

        // Se totalOffering foi fornecido explicitamente no DTO, utiliza-o; caso contrário, usa a soma dos alunos
        double finalTotalOffering = (dto.getTotalOffering() != null && dto.getTotalOffering() >= 0)
                ? dto.getTotalOffering()
                : calculatedOfferings;

        AttendanceOffering offering = new AttendanceOffering(savedAttendance, finalTotalOffering);
        offering.setCreatedAt(sessionDate);
        attendanceOfferingRepository.save(offering);

        savedAttendance.setStudentAttendances(savedRecords);
        savedAttendance.setOffering(offering);

        return toResponseDTO(savedAttendance, savedRecords, finalTotalOffering);
    }

    /**
     * Busca os dados de uma chamada pelo seu identificador.
     *
     * @param id identificador da chamada
     * @return chamada encontrada com lista de alunos
     */
    @Transactional(readOnly = true)
    public AttendanceResponseDTO getAttendanceById(Long id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chamada não encontrada com o ID: " + id));

        List<AttendanceStudent> records = attendanceStudentRepository.findByAttendanceId(id);
        double totalOffering = attendanceOfferingRepository.findByAttendanceId(id)
                .map(AttendanceOffering::getTotalOffering)
                .orElse(0.0);

        return toResponseDTO(attendance, records, totalOffering);
    }

    /**
     * Retorna o histórico de chamadas realizadas para uma determinada classe.
     *
     * @param classId identificador da classe
     * @return lista de chamadas da classe
     */
    @Transactional(readOnly = true)
    public List<AttendanceResponseDTO> getAttendancesByClass(Long classId) {
        if (!classRepository.existsById(classId)) {
            throw new ResourceNotFoundException("Classe não encontrada com o ID: " + classId);
        }

        List<Attendance> attendances = attendanceRepository.findByEbdClassId(classId);
        return attendances.stream()
                .map(att -> {
                    List<AttendanceStudent> records = attendanceStudentRepository.findByAttendanceId(att.getId());
                    double offering = attendanceOfferingRepository.findByAttendanceId(att.getId())
                            .map(AttendanceOffering::getTotalOffering)
                            .orElse(0.0);
                    return toResponseDTO(att, records, offering);
                })
                .collect(Collectors.toList());
    }

    /**
     * Converte os dados de uma chamada em DTO de resposta detalhado.
     *
     * @param attendance    entidade Attendance
     * @param records       registros de presenças individuais
     * @param totalOffering valor total da oferta arrecadada
     * @return DTO com contagens e itens
     */
    private AttendanceResponseDTO toResponseDTO(Attendance attendance, List<AttendanceStudent> records, double totalOffering) {
        int presentCount = 0;
        int absentCount = 0;

        List<AttendanceStudentResponseDTO> studentDTOs = new ArrayList<>();
        for (AttendanceStudent r : records) {
            if (r.isPresent()) {
                presentCount++;
            } else {
                absentCount++;
            }

            studentDTOs.add(new AttendanceStudentResponseDTO(
                    r.getId(),
                    r.getStudent().getId(),
                    r.getStudent().getNome(),
                    r.isPresent(),
                    r.isBook(),
                    r.isBible(),
                    r.getOffering(),
                    r.getCreatedAt()
            ));
        }

        return new AttendanceResponseDTO(
                attendance.getId(),
                attendance.getEbdClass().getId(),
                attendance.getEbdClass().getNome(),
                attendance.getCreatedAt(),
                totalOffering,
                presentCount,
                absentCount,
                studentDTOs
        );
    }
}
