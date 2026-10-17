package ebdmanager.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para relatório geral de frequência e participação em um período especificado.
 */
public class PeriodReportDTO {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int totalSessions;
    private int totalPresent;
    private int totalAbsent;
    private double overallAttendanceRate;
    private int totalBibles;
    private int totalBooks;
    private double totalOfferings;
    private List<ClassPeriodSummaryDTO> classBreakdowns = new ArrayList<>();

    public PeriodReportDTO() {
    }

    public PeriodReportDTO(LocalDateTime startDate, LocalDateTime endDate, int totalSessions, int totalPresent, int totalAbsent, double overallAttendanceRate, int totalBibles, int totalBooks, double totalOfferings, List<ClassPeriodSummaryDTO> classBreakdowns) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalSessions = totalSessions;
        this.totalPresent = totalPresent;
        this.totalAbsent = totalAbsent;
        this.overallAttendanceRate = overallAttendanceRate;
        this.totalBibles = totalBibles;
        this.totalBooks = totalBooks;
        this.totalOfferings = totalOfferings;
        this.classBreakdowns = classBreakdowns != null ? classBreakdowns : new ArrayList<>();
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
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

    public double getOverallAttendanceRate() {
        return overallAttendanceRate;
    }

    public void setOverallAttendanceRate(double overallAttendanceRate) {
        this.overallAttendanceRate = overallAttendanceRate;
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

    public List<ClassPeriodSummaryDTO> getClassBreakdowns() {
        return classBreakdowns;
    }

    public void setClassBreakdowns(List<ClassPeriodSummaryDTO> classBreakdowns) {
        this.classBreakdowns = classBreakdowns != null ? classBreakdowns : new ArrayList<>();
    }
}
