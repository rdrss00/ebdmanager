package ebdmanager.dto;

/**
 * DTO para exibir o resumo de frequência e participação de uma classe em um período.
 */
public class ClassPeriodSummaryDTO {

    private Long classId;
    private String className;
    private int totalSessions;
    private int totalPresent;
    private int totalAbsent;
    private double attendanceRate;
    private int totalBibles;
    private int totalBooks;
    private double totalOfferings;

    public ClassPeriodSummaryDTO() {
    }

    public ClassPeriodSummaryDTO(Long classId, String className, int totalSessions, int totalPresent, int totalAbsent, double attendanceRate, int totalBibles, int totalBooks, double totalOfferings) {
        this.classId = classId;
        this.className = className;
        this.totalSessions = totalSessions;
        this.totalPresent = totalPresent;
        this.totalAbsent = totalAbsent;
        this.attendanceRate = attendanceRate;
        this.totalBibles = totalBibles;
        this.totalBooks = totalBooks;
        this.totalOfferings = totalOfferings;
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
}
