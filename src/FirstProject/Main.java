package FirstProject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final FileHandler fileHandler = new FileHandler();

    public static void main(String[] args) {
        System.out.println("Welcome to the Student Management System");

        while (true) {
            System.out.println("\n <==== Main Menu ===> ");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Sort Students");
            System.out.println("7. Show Topper");
            System.out.println("8. Count Students");
            System.out.println("9. Export to Text File");
            System.out.println("10. Exit");

            int choice = readIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewAllStudents();
                case 3 -> searchStudent();
                case 4 -> updateStudent();
                case 5 -> deleteStudent();
                case 6 -> sortStudents();
                case 7 -> showTopper();
                case 8 -> countStudents();
                case 9 -> exportToTextFile();
                case 10 -> {
                    System.out.println("Shutting down the Student Management System. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid selection. Please try again.");
            }
        }
    }

    private static void addStudent() {
        int id = readPositiveIntInput("Enter Student ID: ");
        System.out.print("Enter full name: ");
        String name = scanner.nextLine().trim();
        if(name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        int subCount = readPositiveIntInput("Enter number of subjects: ");
        List<Integer> marks = new ArrayList<>();

        for (int i = 0; i < subCount; i++) {
            while (true) {
                int mark = readIntInput("Enter mark for subject " + (i + 1) + " (0-100): ");
                if (mark >= 0 && mark <= 100) {
                    marks.add(mark);
                    break;
                }
                System.out.println("Invalid mark. Please enter a value between 0 and 100.");
            }
        }

        boolean success = fileHandler.addStudent(new Student(id, name, marks));
        if (success) {
            System.out.println("Student record added successfully.");
        } else {
            System.out.println("Failed to add: A student with ID " + id + " already exists.");
        }
    }

    private static void viewAllStudents() {
        List<Student> students = fileHandler.getAllStudents();
        displayStudents(students);
    }

    private static void searchStudent() {
        if(fileHandler.getCount() == 0){
            System.out.println("No records found in the system.");
            return;
        }
        int id = readPositiveIntInput("Enter Student ID to search: ");
        Optional<Student> studentOpt = fileHandler.getStudent(id);

        if (studentOpt.isPresent()) {
            System.out.println("=== Student Found ===");
            printHeader();
            System.out.println(studentOpt.get());
        } else {
            System.out.println("Student ID not found.");
        }
    }

    private static void updateStudent() {
        if (fileHandler.getCount() == 0) {
            System.out.println("No records available to update.");
            return;
        }

        int id = readPositiveIntInput("Enter Student ID to update: ");
        Optional<Student> studentOpt = fileHandler.getStudent(id);

        if (studentOpt.isEmpty()) {
            System.out.println("Student ID not found.");
            return;
        }

        Student st = studentOpt.get();
        System.out.println("Student found: " + st.getFullName());
        System.out.println("1. Update Name");
        System.out.println("2. Update Marks");
        int choice = readIntInput("Choose what to update: ");

        if (choice == 1) {
            System.out.print("Enter new name: ");
            String newName = scanner.nextLine().trim();
            st.setFullName(newName);
            fileHandler.updateStudent(st);
            System.out.println("Name updated successfully!");
        } else if (choice == 2) {
            int n = readPositiveIntInput("Enter new number of subjects: ");
            List<Integer> newMarks = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                while (true) {
                    int mark = readIntInput("Enter marks for subject " + (i + 1) + ": ");
                    if (mark >= 0 && mark <= 100) {
                        newMarks.add(mark);
                        break;
                    }
                    System.out.println("Invalid marks. Minimum 0, Maximum 100.");
                }
            }
            st.setSubjectMarks(newMarks);
            fileHandler.updateStudent(st);
            System.out.println("Marks updated successfully.");
        } else {
            System.out.println("Invalid option selected.");
        }
    }

    private static void deleteStudent() {
        if (fileHandler.getCount() == 0) {
            System.out.println("No records to delete.");
            return;
        }
        int id = readPositiveIntInput("Enter Student ID to delete: ");
        if (fileHandler.deleteStudent(id)) {
            System.out.println("Student with ID " + id + " has been deleted.");
        } else {
            System.out.println("Student ID not found.");
        }
    }

    private static void sortStudents() {
        if (fileHandler.getCount() == 0) {
            System.out.println("No records available.");
            return;
        }

        System.out.println("Select Sorting Parameter:");
        System.out.println("1. By Percentage (Highest to Lowest)");
        System.out.println("2. By Name (A-Z)");
        System.out.println("3. By ID (Ascending)");
        
        int choice = readIntInput("Enter choice: ");
        List<Student> sortedList = fileHandler.getAllStudents();

        switch (choice) {
            case 1 -> sortedList.sort(new SortByPercentage());
            case 2 -> sortedList.sort(new SortByName());
            case 3 -> sortedList.sort(new SortById());
            default -> {
                System.out.println("Invalid choice. Operation aborted.");
                return;
            }
        }
        displayStudents(sortedList);
    }

    private static void showTopper() {
        List<Student> all = fileHandler.getAllStudents();
        if (all.isEmpty()) {
            System.out.println("No records available to determine a topper.");
            return;
        }
        Student topper = all.get(0);
        for(Student s : all) {
            if(s.getCalculatedPercentage() > topper.getCalculatedPercentage()) {
                topper = s;
            }
        }
        System.out.println("=== Top Performing Student ===");
        printHeader();
        System.out.println(topper);
    }

    private static void countStudents() {
        System.out.println("Total Registered Students: " + fileHandler.getCount());
    }

    private static void exportToTextFile() {
        List<Student> students = fileHandler.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No records to export.");
            return;
        }
        String defaultPath = "studentReport.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(defaultPath))) {
            bw.write(String.format("%-5s | %-20s | %-10s | %s", "ID", "Name", "Subjects", "Percentage"));
            bw.newLine();
            bw.write("-".repeat(55));
            bw.newLine();
            for (Student s : students) {
                bw.write(s.toString());
                bw.newLine();
            }
            System.out.println("Data successfully exported to: " + defaultPath);
        } catch (IOException e) {
            System.out.println("Failed to export text file: " + e.getMessage());
        }
    }

    private static void displayStudents(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }
        printHeader();
        for (Student s : students) {
            System.out.println(s);
        }
    }

    private static void printHeader() {
        System.out.println(String.format("%-5s | %-20s | %-10s | %s", "ID", "Name", "Subjects", "Percentage"));
        System.out.println("-".repeat(55));
    }

    private static int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Numeric value required. Try again.");
            }
        }
    }

    private static int readPositiveIntInput(String prompt) {
        while (true) {
            int val = readIntInput(prompt);
            if (val > 0) return val;
            System.out.println("Value must be a positive number.");
        }
    }
}
