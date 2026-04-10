package FirstProject;

import java.util.Comparator;

public class SortById implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        return Integer.compare(s1.getStudentId(), s2.getStudentId());
    }
}
