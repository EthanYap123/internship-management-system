
/**
 * Member A:
 * Class: DIT/FT/2B/21
 * Student ID: P2523752
 * Name: Yap Kai Jun Ethan
 *
 * Dedicated FileHandler class for CA2 File I/O operations.
 * Handles reading and writing Company and Student data from/to text files (e.g., internships.txt).
 *
 * Member B:
 * Class: DIT/FT/2B/21
 * Student ID: P2523075
 * Name: Tay Yu Xiang Janston
 *
 * Added the object-serialization methods (saveSerializedData / readSerializedData)
 * required by CA2 Function 4, so that ALL file I/O still lives in this one class.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FileHandler {

    /**
     * Default name of the object serialized file written on admin exit.
     * (Member B — CA2 Function 4)
     */
    public static final String SERIALIZED_FILE = "internships.dat";

    /**
     * Simple container that holds both collections so that a single object can
     * be passed around, and so that one writeObject() call saves everything.
     * Made Serializable by Member B for CA2 Function 4.
     */
    public static class SystemData implements Serializable {

        private static final long serialVersionUID = 1L;

        public ArrayList<Company> companies;
        public ArrayList<Student> students;

        public SystemData(ArrayList<Company> companies, ArrayList<Student> students) {
            this.companies = companies;
            this.students = students;
        }
    }

    /**
     * Reads Company and Student data from specified text file.
     */
    public static SystemData readData(String filePath) throws IOException {
        ArrayList<Company> companies = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();

        File file = new File(filePath);
        if (!file.exists()) {
            File srcFile = new File("src/" + filePath);
            if (srcFile.exists()) {
                file = srcFile;
            } else {
                throw new FileNotFoundException("File not found: " + filePath);
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line == null) {
                return new SystemData(companies, students);
            }

            int numCompanies = Integer.parseInt(line.trim());
            for (int i = 0; i < numCompanies; i++) {
                line = br.readLine();
                if (line != null && !line.trim().isEmpty()) {
                    String[] parts = line.split(";");
                    if (parts.length >= 3) {
                        String compName = parts[0].trim();
                        String contact = parts[1].trim();
                        double allowance = Double.parseDouble(parts[2].trim());
                        companies.add(new Company(compName, allowance, contact));
                    }
                }
            }

            line = br.readLine();
            if (line != null && !line.trim().isEmpty()) {
                int numStudents = Integer.parseInt(line.trim());
                for (int i = 0; i < numStudents; i++) {
                    line = br.readLine();
                    if (line != null && !line.trim().isEmpty()) {
                        String[] parts = line.split(";");
                        if (parts.length >= 6) {
                            String course = parts[0].trim();
                            String studentId = parts[1].trim();
                            String name = parts[2].trim();
                            String compName = parts[3].trim();
                            String role = parts[4].trim();
                            int numRatings = Integer.parseInt(parts[5].trim());

                            Company assignedCompany = null;
                            for (Company c : companies) {
                                if (c.getCompanyName().equalsIgnoreCase(compName)) {
                                    assignedCompany = c;
                                    break;
                                }
                            }
                            if (assignedCompany == null) {
                                assignedCompany = new Company(compName, 0.0, "N/A");
                                companies.add(assignedCompany);
                            }

                            Student student = new Student(studentId, name, course, assignedCompany, role);

                            int index = 6;
                            for (int r = 0; r < numRatings; r++) {
                                if (index + 1 < parts.length) {
                                    int weeks = Integer.parseInt(parts[index].trim());
                                    double marks = Double.parseDouble(parts[index + 1].trim());
                                    student.addRating(weeks, marks);
                                    index += 2;
                                }
                            }
                            students.add(student);
                        }
                    }
                }
            }
        }

        return new SystemData(companies, students);
    }

    /**
     * Saves Company and Student data to specified text file.
     */
    public static void saveData(String filePath, ArrayList<Company> companies, ArrayList<Student> students) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println(companies.size());
            for (Company c : companies) {
                pw.println(c.getCompanyName() + ";" + c.getContactPerson() + ";" + (int) c.getMonthlyAllowance());
            }

            pw.println(students.size());
            for (Student s : students) {
                StringBuilder sb = new StringBuilder();
                sb.append(s.getCourseOfStudy()).append(";")
                        .append(s.getStudentId()).append(";")
                        .append(s.getName()).append(";")
                        .append(s.getAssignedCompany().getCompanyName()).append(";")
                        .append(s.getInternshipRole()).append(";")
                        .append(s.getRatings().size());

                for (Student.PerformanceRating r : s.getRatings()) {
                    sb.append(";").append(r.getWeeks()).append(";").append((int) r.getMarks());
                }
                pw.println(sb.toString());
            }
        }
    }

    // =====================================================================
    // Member B — CA2 Function 4: Object Serialization
    //
    // The Admin System must save all current Student and Company data to an
    // object serialized file before it terminates. Both collections are
    // wrapped in one SystemData object so a single writeObject() call stores
    // everything. Because a Student holds a reference to its Company, Java
    // serialization automatically follows that link and writes each shared
    // Company object only once.
    // =====================================================================
    /**
     * Saves the current Company and Student objects to an object serialized
     * file using ObjectOutputStream.
     *
     * @param filePath name of the object file to write (e.g. internships.dat)
     * @param companies the current company collection
     * @param students the current student collection
     * @throws IOException if the file cannot be created or written to
     */
    public static void saveSerializedData(String filePath, ArrayList<Company> companies,
            ArrayList<Student> students) throws IOException {

        SystemData data = new SystemData(companies, students);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(data);
        }
    }

    /**
     * Reads Company and Student objects back from an object serialized file
     * using ObjectInputStream. Provided so the saved file can be verified.
     *
     * @param filePath name of the object file to read
     * @return the SystemData object holding both collections
     * @throws IOException if the file is missing or cannot be read
     * @throws ClassNotFoundException if the saved classes no longer match
     */
    public static SystemData readSerializedData(String filePath) throws IOException, ClassNotFoundException {

        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("Object file not found: " + filePath);
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (SystemData) in.readObject();
        }
    }

    // =====================================================================
    // Member B — Advanced Feature: timestamped backups.
    //
    // This lives here rather than in the GUI for the same reason as the
    // methods above: this class is the one place that touches files.
    // =====================================================================
    /**
     * Copies the existing object file to a timestamped backup before it gets
     * overwritten, so an earlier save can always be recovered by hand.
     *
     * @param filePath the object file about to be replaced
     * @return the name of the backup created, or null if there was nothing to
     *         back up yet (the very first save)
     * @throws IOException if the copy fails
     */
    public static String createBackup(String filePath) throws IOException {

        File source = new File(filePath);
        if (!source.exists()) {
            return null; // nothing saved yet, so nothing to back up
        }

        String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String baseName = filePath;
        int dot = baseName.lastIndexOf('.');
        if (dot > 0) {
            baseName = baseName.substring(0, dot);
        }
        String backupPath = baseName + "_backup_" + stamp + ".dat";

        Files.copy(source.toPath(), new File(backupPath).toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        return backupPath;
    }
}
