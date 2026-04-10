package FirstProject;

import java.util.Comparator;

public class SortByPercentage implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        // Descending order sort (highest percentage first)
        return Double.compare(s2.getCalculatedPercentage(), s1.getCalculatedPercentage());
    }
}
