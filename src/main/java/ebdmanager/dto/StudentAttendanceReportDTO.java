package ebdmanager.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para relatório de frequência e participação detalhado por aluno.
 */
public class StudentAttendanceReportDTO {

    private Long studentId;
    private String studentName;
    private Integer studentAge;
    private String className;
    private int totalSessions;
    private int presentCount;
    private int absentCount;
    private double attendanceRate;
    private int bibleCount;
    private int bookCount;
    private double totalOffering;
    private List<StudentSessionHistoryDTO> sessions = new ArrayList<>();

    public StudentAttendanceReportDTO() {
    }

    public StudentAttendanceReportDTO(Long studentId, String studentName, Integer studentAge, String className, int totalSessions, int presentCount, int absentCount, double attendanceRate, int bibleCount, int bookCount, double totalOffering, List<StudentSessionHistoryDTO> sessions) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentAge = studentAge;
        this.className = className;
        this.totalSessions = totalSessions;
        this.presentCount = presentCount;
        this.absentCount = absentCount;
        this.attendanceRate = attendanceRate;
        this.bibleCount = bibleCount;
        this.bookCount = bookCount;
        this.totalOffering = totalOffering;
        this.sessions = sessions != null ? sessions : new ArrayList<>();
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getStudentAge() {
        return studentAge;
    }

    public void setStudentAge(Integer studentAge) {
        this.studentAge = studentAge;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
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

    public double getAttendanceRate() {
        return attendanceRate;
    }

    public void setAttendanceRate(double attendanceRate) {
        this.attendanceRate = attendanceRate;
    }

    public int getBibleCount() {
        return bibleCount;
    }

    public void setBibleCount(int bibleCount) {
        this.bibleCount = bibleCount;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    public double getTotalOffering() {
        return totalOffering;
    }

    public void setTotalOffering(double totalOffering) {
        this.totalOffering = totalOffering;
    }

    public List<StudentSessionHistoryDTO> getSessions() {
        return sessions;
    }

    public void setSessions(List<StudentSessionHistoryDTO> sessions) {
        this.sessions = sessions != null ? sessions : new ArrayList<>();
    }

    public static class StudentSessionHistoryDTO {
        private Long attendanceId;
        private LocalDateTime date;
        private boolean present;
        private boolean book;
        private boolean bible;
        private double offering;

        public StudentSessionHistoryDTO() {
        }

        public StudentSessionHistoryDTO(Long attendanceId, LocalDateTime date, boolean present, boolean book, boolean bible, double offering) {
            this.attendanceId = attendanceId;
            this.date = date;
            this.present = present;
            this.book = book;
            this.bible = bible;
            this.offering = offering;
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

        public boolean isPresent() {
            return present;
        }

        public void setPresent(boolean present) {
            this.present = present;
        }

        public boolean isBook() {
            return book;
        }

        public void setBook(boolean book) {
            this.book = book;
        }

        public boolean isBible() {
            return bible;
        }

        public void setBible(boolean bible) {
            this.bible = bible;
        }

        public double getOffering() {
            return offering;
        }

        public void setOffering(double offering) {
            this.offering = offering;
        }
    }
}
