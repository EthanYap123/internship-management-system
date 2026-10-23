
/**
 * Member A:
 * Class: DIT/FT/2B/21
 * Student ID: P2523752
 * Name: Yap Kai Jun Ethan
 *
 * Member B:
 * Class: DIT/FT/2B/21
 * Student ID: P2523075
 * Name: Tay Yu Xiang Janston
 *
 * Added the shared lookup / add / remove helper methods at the bottom of this
 * class (see the "Member B" section) so that the Admin GUI does not have to
 * repeat the same search loops. Member A's own methods were not changed.
 */
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class InternshipManagement {

    private ArrayList<Company> companies;
    private ArrayList<Student> students;

    public InternshipManagement() {
        companies = new ArrayList<>();
        students = new ArrayList<>();
        loadDataFromFile();
    }

    public void loadDataFromFile() {
        try {
            FileHandler.SystemData data = FileHandler.readData("internships.txt");
            if (!data.companies.isEmpty() || !data.students.isEmpty()) {
                this.companies = data.companies;
                this.students = data.students;
                return;
            }
        } catch (Exception e) {
            System.err.println("Could not load internships.txt, initializing default data. " + e.getMessage());
        }
        initializeMockData();
        saveDataToFile();
    }

    public void saveDataToFile() {
        try {
            FileHandler.saveData("internships.txt", companies, students);
        } catch (Exception e) {
            System.err.println("Failed to save to internships.txt: " + e.getMessage());
        }
    }

    // initialization
    private void initializeMockData() {
        Company techCorp = new Company("TechCorp Pte Ltd", 1200.0, "Ms. Alice");
        Company dataSys = new Company("DataSys Solutions", 1000.0, "Mr. Bob");
        Company startupXyz = new Company("StartupXYZ Pte Ltd", 800.0, "Charlie");

        companies.add(techCorp);
        companies.add(dataSys);
        companies.add(startupXyz);

        Student s1 = new Student("S2312333", "John Tan", "DIT", techCorp, "Software Intern");
        s1.addRating(4, 75);
        s1.addRating(4, 63);
        s1.addRating(6, 72);
        s1.addRating(2, 89);

        Student s2 = new Student("S2312555", "Samsudin", "DIT/FT/2A/02", dataSys, "IT Support Intern");
        s2.addRating(12, 78);

        Student s3 = new Student("S2312999", "Esther Lim", "DIT", techCorp, "QA Intern");
        s3.addRating(8, 85);

        students.add(s1);
        students.add(s2);
        students.add(s3);
    }

    public void displayAllInternships() {
        // error case if no students found
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No internship records available.", "All Internship Records",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Use html
        StringBuilder sb = new StringBuilder("<html><body style='font-family: sans-serif; padding: 5px;'>");
        sb.append("<h3 style='margin-bottom: 10px; color: #333;'>All Internship Records</h3>");
        sb.append(
                "<table border='1' cellpadding='6' cellspacing='0' style='border-collapse: collapse; border: 1px solid #ccc;'>");

        // for table headers
        sb.append("<tr bgcolor='#e6e6e6' style='font-weight: bold; text-align: left;'>")
                .append("<th style='padding: 8px;'>ID</th>")
                .append("<th style='padding: 8px;'>Name</th>")
                .append("<th style='padding: 8px;'>Course</th>")
                .append("<th style='padding: 8px;'>Company</th>")
                .append("<th style='padding: 8px;'>Role</th>")
                .append("<th style='padding: 8px;'>Allowance</th>")
                .append("<th style='padding: 8px;'>Ratings</th>")
                .append("</tr>");

        // for table rows
        for (Student s : students) {
            sb.append("<tr>")
                    .append("<td style='padding: 6px;'>").append(s.getStudentId()).append("</td>")
                    .append("<td style='padding: 6px; font-weight: bold;'>").append(s.getName()).append("</td>")
                    .append("<td style='padding: 6px;'>").append(s.getCourseOfStudy()).append("</td>")
                    .append("<td style='padding: 6px;'>").append(s.getAssignedCompany().getCompanyName())
                    .append("</td>")
                    .append("<td style='padding: 6px;'>").append(s.getInternshipRole()).append("</td>")
                    .append("<td style='padding: 6px;'>$").append(String.format("%.0f", s.getMonthlyAllowance()))
                    .append("/mth</td>")
                    .append("<td style='padding: 6px; font-size: 11px;'>").append(s.getRatingsString()).append("</td>")
                    .append("</tr>");
        }

        sb.append("</table></body></html>");

        JOptionPane.showMessageDialog(null, sb.toString(), "All Internship Records (Table View)",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // required feature
    public void searchByStudentName() {
        String searchName = JOptionPane.showInputDialog(null, "Enter the student name to search:", "Search",
                JOptionPane.QUESTION_MESSAGE);
        if (searchName == null || searchName.trim().isEmpty()) {
            return;
        }
        Student foundStudent = null;
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(searchName.trim())) {
                foundStudent = s;
                break;
            }
        }
        if (foundStudent != null) {
            String message = "Name: " + foundStudent.getName() + "\n"
                    + "Student ID: " + foundStudent.getStudentId() + "\n"
                    + "Course: " + foundStudent.getCourseOfStudy() + "\n"
                    + "Company: " + foundStudent.getAssignedCompany().getCompanyName() + "\n"
                    + "Role: " + foundStudent.getInternshipRole() + "\n"
                    + "Allowance: $" + String.format("%.0f", foundStudent.getMonthlyAllowance())
                    + "/mth\n\n"
                    + "Internship Score: " + String.format("%.2f", foundStudent.calculateOverallScore());
            JOptionPane.showMessageDialog(null, message, "Student Internship Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Cannot find the student \"" + searchName + "\"!!", "Info",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // bonus feature 1
    public void searchByStudentId() {
        String idInput = JOptionPane.showInputDialog(null, "Enter Student ID (e.g., S2312333):", "Search by ID",
                JOptionPane.QUESTION_MESSAGE);
        if (idInput == null || idInput.trim().isEmpty()) {
            return;
        }

        String formattedId = idInput.trim().toUpperCase();

        // validation of student id, start with 's', 7 numbers after that
        if (!formattedId.matches("^S\\d{7}$")) {
            JOptionPane.showMessageDialog(null, "Invalid format! Student ID must start with 'S' followed by 7 digits.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student foundStudent = null;
        for (Student s : students) {
            if (s.getStudentId().equalsIgnoreCase(formattedId)) {
                foundStudent = s;
                break;
            }
        }

        if (foundStudent != null) {
            // structured display of information (bonus feature 2)
            String message = "Name: " + foundStudent.getName() + "\n"
                    + "Student ID: " + foundStudent.getStudentId() + "\n"
                    + "Course: " + foundStudent.getCourseOfStudy() + "\n"
                    + "Company: " + foundStudent.getAssignedCompany().getCompanyName() + "\n"
                    + "Role: " + foundStudent.getInternshipRole() + "\n"
                    + "Allowance: $" + String.format("%.0f", foundStudent.getMonthlyAllowance()) + "/mth\n"
                    + "Ratings: " + foundStudent.getRatingsString() + "\n\n"
                    + "Overall Internship Score: " + String.format("%.2f", foundStudent.calculateOverallScore());

            JOptionPane.showMessageDialog(null, message, "Student Internship Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Student ID " + formattedId + " not found!", "Not Found",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // required feature
    public void searchByCompany() {
        String searchCompany = JOptionPane.showInputDialog(null, "Enter the company name to search:", "Search",
                JOptionPane.QUESTION_MESSAGE);
        if (searchCompany == null || searchCompany.trim().isEmpty()) {
            return;
        }

        int placementCount = 0;
        double combinedScoresSum = 0;
        StringBuilder studentListBuilder = new StringBuilder();

        for (Student s : students) {
            if (s.getAssignedCompany().getCompanyName().equalsIgnoreCase(searchCompany.trim())) {
                placementCount++;
                combinedScoresSum += s.calculateOverallScore();

                studentListBuilder.append("<li style='margin: 2px 0;'><b>")
                        .append(s.getName())
                        .append("</b> (")
                        .append(s.getStudentId())
                        .append(") - ")
                        .append(s.getInternshipRole())
                        .append("</li>");
            }
        }

        if (placementCount > 0) {
            double averageScore = combinedScoresSum / placementCount;

            StringBuilder message = new StringBuilder("<html><body style='font-family: sans-serif; padding: 0px;'>");
            message.append("<h3 style='margin: 0 0 6px 0; color: #333;'>Company Summary</h3>")
                    .append("<div style='margin: 2px 0;'><b>Company:</b> ").append(searchCompany.trim())
                    .append("</div>")
                    .append("<div style='margin: 2px 0;'><b>Number of student(s):</b> ").append(placementCount)
                    .append("</div>")
                    .append("<div style='margin: 2px 0;'><b>Average Internship Score:</b> ")
                    .append(String.format("%.2f", averageScore)).append("</div>")
                    .append("<hr style='border: 0; border-top: 1px solid #ccc; margin: 8px 0;'>")
                    .append("<div style='font-weight: bold; color: #555; margin: 4px 0;'>Assigned Students:</div>")
                    .append("<ul style='margin: 0 0 0 15px; padding: 0;'>")
                    // bonus feature 3, adding information about student to filter by company
                    .append(studentListBuilder.toString())
                    .append("</ul>")
                    .append("</body></html>");

            JOptionPane.showMessageDialog(null, message.toString(), "Company Summary", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No student found from company!", "Company Summary",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // calling functions
    public ArrayList<Student> getStudents() {
        return students;
    }

    public ArrayList<Company> getCompanies() {
        return companies;
    }

    // =====================================================================
    // Member B — shared lookup and update helpers.
    //
    // This class owns the two ArrayLists, so the searching is written here
    // once and reused by the Admin GUI. All matching is case-insensitive.
    // =====================================================================
    /**
     * Finds a student by student ID.
     *
     * @return the matching Student, or null if no student has that ID
     */
    public Student findStudentById(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }
        for (Student s : students) {
            if (s.getStudentId().equalsIgnoreCase(studentId.trim())) {
                return s;
            }
        }
        return null;
    }

    /**
     * Finds a student by full name.
     *
     * @return the first matching Student, or null if no student has that name
     */
    public Student findStudentByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name.trim())) {
                return s;
            }
        }
        return null;
    }

    /**
     * Finds a company by company name.
     *
     * @return the matching Company, or null if that company does not exist
     */
    public Company findCompanyByName(String companyName) {
        if (companyName == null || companyName.trim().isEmpty()) {
            return null;
        }
        for (Company c : companies) {
            if (c.getCompanyName().equalsIgnoreCase(companyName.trim())) {
                return c;
            }
        }
        return null;
    }

    /**
     * Adds a new student to the shared student collection.
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Puts a student back at a specific position. Used by the Admin GUI's Undo
     * feature so a restored record returns to its original place in the list.
     * The index is clamped in case other records were removed in the meantime.
     */
    public void addStudentAt(int index, Student student) {
        int safeIndex = index;
        if (safeIndex < 0) {
            safeIndex = 0;
        }
        if (safeIndex > students.size()) {
            safeIndex = students.size();
        }
        students.add(safeIndex, student);
    }

    /**
     * Removes a student from the shared student collection.
     *
     * @return true if the student was in the collection and was removed
     */
    public boolean removeStudent(Student student) {
        return students.remove(student);
    }
}
