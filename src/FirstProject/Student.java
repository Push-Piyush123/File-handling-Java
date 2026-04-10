package FirstProject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Student {
    private final int studentId;
    private String fullName;
    private final List<Integer> subjectMarks;
    private double calculatedPercentage;
    private int subjectCount;

    public Student(int studentId, String fullName, List<Integer> subjectMarks) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.subjectMarks = new ArrayList<>();
        if (subjectMarks != null) {
            this.subjectMarks.addAll(subjectMarks);
        }
        this.subjectCount = this.subjectMarks.size();
        computePerformance();
    }

    private void computePerformance() {
        if (subjectCount == 0) {
            this.calculatedPercentage = 0.0;
            return;
        }
        int total = subjectMarks.stream().mapToInt(Integer::intValue).sum();
        this.calculatedPercentage = (double) total / subjectCount;
    }

    public int getStudentId() { return studentId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public List<Integer> getSubjectMarks() { return Collections.unmodifiableList(subjectMarks); }
    public void setSubjectMarks(List<Integer> newMarks) {
        this.subjectMarks.clear();
        if (newMarks != null) {
            this.subjectMarks.addAll(newMarks);
        }
        this.subjectCount = this.subjectMarks.size();
        computePerformance();
    }
    public double getCalculatedPercentage() { return calculatedPercentage; }
    public int getSubjectCount() { return subjectCount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentId == student.studentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }

    @Override
    public String toString() {
        return String.format("%-5d | %-20s | %-10d | %.2f", studentId, fullName, subjectCount, calculatedPercentage);
    }
}
