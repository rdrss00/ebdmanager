package ebdmanager.dto;

/**
 * DTO para exibir a contagem de alunos por classe no relatório geral.
 */
public class ClassStudentCountDTO {

    private Long classId;
    private String className;
    private long studentCount;
    private long enrollmentCount;

    public ClassStudentCountDTO() {
    }

    public ClassStudentCountDTO(Long classId, String className, long studentCount, long enrollmentCount) {
        this.classId = classId;
        this.className = className;
        this.studentCount = studentCount;
        this.enrollmentCount = enrollmentCount;
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

    public long getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(long studentCount) {
        this.studentCount = studentCount;
    }

    public long getEnrollmentCount() {
        return enrollmentCount;
    }

    public void setEnrollmentCount(long enrollmentCount) {
        this.enrollmentCount = enrollmentCount;
    }
}
