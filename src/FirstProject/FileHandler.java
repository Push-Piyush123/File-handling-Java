package FirstProject;

import java.io.*;
import java.util.*;

public class FileHandler {
    private static final String FILE_NAME = "student_data.csv";
    private final Map<Integer, Student> cache = new HashMap<>();

    public FileHandler() {
        loadData();
    }

    private void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                List<Integer> marks = new ArrayList<>();
                for (int i = 2; i < parts.length; i++) {
                    if (!parts[i].trim().isEmpty()) {
                        marks.add(Integer.parseInt(parts[i].trim()));
                    }
                }
                cache.put(id, new Student(id, name, marks));
            }
        } catch (Exception e) {
            System.err.println("Warning: Data load issue. Creating fresh database.");
        }
    }

    public void saveData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student student : cache.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append(student.getStudentId()).append(",").append(student.getFullName());
                for (int mark : student.getSubjectMarks()) {
                    sb.append(",").append(mark);
                }
                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving local data.");
        }
    }

    public boolean addStudent(Student student) {
        if(cache.containsKey(student.getStudentId())) return false;
        cache.put(student.getStudentId(), student);
        saveData();
        return true;
    }

    public Optional<Student> getStudent(int id) {
        return Optional.ofNullable(cache.get(id));
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(cache.values());
    }

    public void updateStudent(Student student) {
        cache.put(student.getStudentId(), student); // Auto updates in Map
        saveData();
    }

    public boolean deleteStudent(int id) {
        if (cache.remove(id) != null) {
            saveData();
            return true;
        }
        return false;
    }

    public int getCount() {
        return cache.size();
    }
}
