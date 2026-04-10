package FirstProject;

import java.util.Comparator;

public class SortByName implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        return String.CASE_INSENSITIVE_ORDER.compare(s1.getFullName(), s2.getFullName());
    }
}
