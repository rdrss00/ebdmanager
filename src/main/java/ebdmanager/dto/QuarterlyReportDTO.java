package ebdmanager.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para relatório geral da EBD consolidado por trimestre.
 */
public class QuarterlyReportDTO {

    private int year;
    private int quarter;
    private LocalDate startDate;
    private LocalDate endDate;
    private long totalClasses;
    private long totalStudents;
    private double averageStudentsPerClass;
    private int totalSessions;
    private int totalPresent;
    private int totalAbsent;
    private double attendanceRate;
    private int totalBibles;
    private int totalBooks;
    private double totalOfferings;
    private List<ClassPeriodSummaryDTO> classSummaries = new ArrayList<>();

    public QuarterlyReportDTO() {
    }

    public QuarterlyReportDTO(int year, int quarter, LocalDate startDate, LocalDate endDate, long totalClasses, long totalStudents, double averageStudentsPerClass, int totalSessions, int totalPresent, int totalAbsent, double attendanceRate, int totalBibles, int totalBooks, double totalOfferings, List<ClassPeriodSummaryDTO> classSummaries) {
        this.year = year;
        this.quarter = quarter;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalClasses = totalClasses;
        this.totalStudents = totalStudents;
        this.averageStudentsPerClass = averageStudentsPerClass;
        this.totalSessions = totalSessions;
        this.totalPresent = totalPresent;
        this.totalAbsent = totalAbsent;
        this.attendanceRate = attendanceRate;
        this.totalBibles = totalBibles;
        this.totalBooks = totalBooks;
        this.totalOfferings = totalOfferings;
        this.classSummaries = classSummaries != null ? classSummaries : new ArrayList<>();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public long getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(long totalClasses) {
        this.totalClasses = totalClasses;
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public double getAverageStudentsPerClass() {
        return averageStudentsPerClass;
    }

    public void setAverageStudentsPerClass(double averageStudentsPerClass) {
        this.averageStudentsPerClass = averageStudentsPerClass;
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

    public List<ClassPeriodSummaryDTO> getClassSummaries() {
        return classSummaries;
    }

    public void setClassSummaries(List<ClassPeriodSummaryDTO> classSummaries) {
        this.classSummaries = classSummaries != null ? classSummaries : new ArrayList<>();
    }
}
