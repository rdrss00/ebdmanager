package ebdmanager.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para relatório de frequência e participação detalhado de uma classe.
 */
public class ClassAttendanceReportDTO {

    private Long classId;
    private String className;
    private long totalEnrolledStudents;
    private int totalSessions;
    private int totalPresent;
    private int totalAbsent;
    private double attendanceRate;
    private int totalBibles;
    private int totalBooks;
    private double totalOfferings;
    private List<ClassSessionHistoryDTO> sessions = new ArrayList<>();

    public ClassAttendanceReportDTO() {
    }

    public ClassAttendanceReportDTO(Long classId, String className, long totalEnrolledStudents, int totalSessions, int totalPresent, int totalAbsent, double attendanceRate, int totalBibles, int totalBooks, double totalOfferings, List<ClassSessionHistoryDTO> sessions) {
        this.classId = classId;
        this.className = className;
        this.totalEnrolledStudents = totalEnrolledStudents;
        this.totalSessions = totalSessions;
        this.totalPresent = totalPresent;
        this.totalAbsent = totalAbsent;
        this.attendanceRate = attendanceRate;
        this.totalBibles = totalBibles;
        this.totalBooks = totalBooks;
        this.totalOfferings = totalOfferings;
        this.sessions = sessions != null ? sessions : new ArrayList<>();
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public long getTotalEnrolledStudents() {
        return totalEnrolledStudents;
    }

    public void setTotalEnrolledStudents(long totalEnrolledStudents) {
        this.totalEnrolledStudents = totalEnrolledStudents;
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
    }

    public int getTotalPresent() {
        return totalPresent;
    }

    public void setTotalPresent(int totalPresent) {
        this.totalPresent = totalPresent;
    }

    public int getTotalAbsent() {
        return totalAbsent;
    }

    public void setTotalAbsent(int totalAbsent) {
        this.totalAbsent = totalAbsent;
    }

    public double getAttendanceRate() {
        return attendanceRate;
    }

    public void setAttendanceRate(double attendanceRate) {
        this.attendanceRate = attendanceRate;
    }

    public int getTotalBibles() {
        return totalBibles;
    }

    public void setTotalBibles(int totalBibles) {
        this.totalBibles = totalBibles;
    }

    public int getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(int totalBooks) {
        this.totalBooks = totalBooks;
    }

    public double getTotalOfferings() {
        return totalOfferings;
    }

    public void setTotalOfferings(double totalOfferings) {
        this.totalOfferings = totalOfferings;
    }

    public List<ClassSessionHistoryDTO> getSessions() {
        return sessions;
    }

    public void setSessions(List<ClassSessionHistoryDTO> sessions) {
        this.sessions = sessions != null ? sessions : new ArrayList<>();
    }

    public static class ClassSessionHistoryDTO {
        private Long attendanceId;
        private LocalDateTime date;
        private int presentCount;
        private int absentCount;
        private double totalOffering;

        public ClassSessionHistoryDTO() {
        }

        public ClassSessionHistoryDTO(Long attendanceId, LocalDateTime date, int presentCount, int absentCount, double totalOffering) {
            this.attendanceId = attendanceId;
            this.date = date;
            this.presentCount = presentCount;
            this.absentCount = absentCount;
            this.totalOffering = totalOffering;
        }

        public Long getAttendanceId() {
            return attendanceId;
        }

        public void setAttendanceId(Long attendanceId) {
            this.attendanceId = attendanceId;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }

        public int getPresentCount() {
            return presentCount;
        }

        public void setPresentCount(int presentCount) {
            this.presentCount = presentCount;
        }

        public int getAbsentCount() {
            return absentCount;
        }

        public void setAbsentCount(int absentCount) {
            this.absentCount = absentCount;
        }

        public double getTotalOffering() {
            return totalOffering;
        }

        public void setTotalOffering(double totalOffering) {
            this.totalOffering = totalOffering;
        }
    }
}
