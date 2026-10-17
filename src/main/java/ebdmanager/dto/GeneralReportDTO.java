package ebdmanager.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO para relatório geral quantitativo da EBD.
 */
public class GeneralReportDTO {

    private long totalClasses;
    private long totalStudents;
    private double averageStudentsPerClass;
    private List<ClassStudentCountDTO> classes = new ArrayList<>();

    public GeneralReportDTO() {
    }

    public GeneralReportDTO(long totalClasses, long totalStudents, double averageStudentsPerClass, List<ClassStudentCountDTO> classes) {
        this.totalClasses = totalClasses;
        this.totalStudents = totalStudents;
        this.averageStudentsPerClass = averageStudentsPerClass;
        this.classes = classes != null ? classes : new ArrayList<>();
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

    public List<ClassStudentCountDTO> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassStudentCountDTO> classes) {
        this.classes = classes != null ? classes : new ArrayList<>();
    }
}
