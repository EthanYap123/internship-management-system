
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
 * Only change made by Member B: made Student and its nested PerformanceRating
 * class Serializable so that student records (including their ratings and the
 * Company they reference) can be written to the object file by FileHandler
 * (CA2 Function 4).
 */
import java.io.Serializable;
import java.util.ArrayList;

public class Student implements Serializable {

    // Version stamp used by Java serialization to check that a saved file
    // still matches this version of the class.
    private static final long serialVersionUID = 1L;

    private String studentId;
    private String name;
    private String courseOfStudy;
    private Company assignedCompany;
    private String internshipRole;
    private double monthlyAllowance; // allowance for student
    private ArrayList<PerformanceRating> ratings;

    public static class PerformanceRating implements Serializable {

        private static final long serialVersionUID = 1L;

        int weeks;
        double marks;

        public PerformanceRating(int weeks, double marks) {
            this.weeks = weeks;
            this.marks = marks;
        }

        public int getWeeks() {
            return weeks;
        }

        public double getMarks() {
            return marks;
        }

        public int getGradePoint() {
            if (marks >= 80) {
                return 4;
            }
            if (marks >= 70) {
                return 3;
            }
            if (marks >= 60) {
                return 2;
            }
            if (marks >= 50) {
                return 1;
            }
            return 0;
        }
    }

    // default constructor to set student allowance as company allowance
    public Student(String studentId, String name, String courseOfStudy, Company assignedCompany,
            String internshipRole) {
        this.studentId = studentId;
        this.name = name;
        this.courseOfStudy = courseOfStudy;
        this.assignedCompany = assignedCompany;
        this.internshipRole = internshipRole;
        this.monthlyAllowance = assignedCompany.getMonthlyAllowance();
        this.ratings = new ArrayList<>();
    }

    // manual constructor for allowance to be set using admin
    public Student(String studentId, String name, String courseOfStudy, Company assignedCompany,
            String internshipRole, double monthlyAllowance) {
        this.studentId = studentId;
        this.name = name;
        this.courseOfStudy = courseOfStudy;
        this.assignedCompany = assignedCompany;
        this.internshipRole = internshipRole;
        this.monthlyAllowance = monthlyAllowance;
        this.ratings = new ArrayList<>();
    }

    public void addRating(int weeks, double marks) {
        this.ratings.add(new PerformanceRating(weeks, marks));
    }

    // calculator function
    public double calculateOverallScore() {
        if (ratings.isEmpty()) {
            return 0.0;
        }
        double totalWeightedPoints = 0;
        int totalWeeks = 0;
        for (PerformanceRating r : ratings) {
            totalWeightedPoints += (r.getGradePoint() * r.weeks);
            totalWeeks += r.weeks;
        }
        if (totalWeeks == 0) {
            return 0.0;
        }
        return totalWeightedPoints / totalWeeks;
    }

    public String getRatingsString() {
        if (ratings.isEmpty()) {
            return "No ratings yet";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ratings.size(); i++) {
            sb.append(ratings.get(i).marks).append(" (").append(ratings.get(i).weeks).append(" wks)");
            if (i < ratings.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    // calling functions
    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getCourseOfStudy() {
        return courseOfStudy;
    }

    public Company getAssignedCompany() {
        return assignedCompany;
    }

    public String getInternshipRole() {
        return internshipRole;
    }

    public double getMonthlyAllowance() {
        return monthlyAllowance;
    }

    public ArrayList<PerformanceRating> getRatings() {
        return ratings;
    }
}
