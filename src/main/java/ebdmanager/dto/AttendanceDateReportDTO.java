package ebdmanager.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para relatório de frequência e participação por data.
 */
public class AttendanceDateReportDTO {

    private LocalDate date;
    private int totalClassesHeld;
    private int totalPresent;
    private int totalAbsent;
    private double overallAttendanceRate;
    private int totalBibles;
    private int totalBooks;
    private double totalOfferings;
    private List<ClassPeriodSummaryDTO> classBreakdowns = new ArrayList<>();

    public AttendanceDateReportDTO() {
    }

    public AttendanceDateReportDTO(LocalDate date, int totalClassesHeld, int totalPresent, int totalAbsent, double overallAttendanceRate, int totalBibles, int totalBooks, double totalOfferings, List<ClassPeriodSummaryDTO> classBreakdowns) {
        this.date = date;
        this.totalClassesHeld = totalClassesHeld;
        this.totalPresent = totalPresent;
        this.totalAbsent = totalAbsent;
        this.overallAttendanceRate = overallAttendanceRate;
        this.totalBibles = totalBibles;
        this.totalBooks = totalBooks;
        this.totalOfferings = totalOfferings;
        this.classBreakdowns = classBreakdowns != null ? classBreakdowns : new ArrayList<>();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getTotalClassesHeld() {
        return totalClassesHeld;
    }

    public void setTotalClassesHeld(int totalClassesHeld) {
        this.totalClassesHeld = totalClassesHeld;
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
