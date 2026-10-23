
/**
 * Member B:
 * Class: DIT/FT/2B/21
 * Student ID: P2523075
 * Name: Tay Yu Xiang Janston
 *
 * Advanced Feature — Data Integrity & Attention Alerts.
 *
 * Scans every student record and reports data problems the admin should act
 * on. This class contains NO Swing code at all: it just returns a list of
 * Alert objects, and the GUI decides how to display them. Keeping the rules
 * separate from the screen makes each rule easy to read and easy to change.
 */
import java.util.ArrayList;

public class DataIntegrityChecker {

    /** How serious a problem is. Used by the GUI to pick a colour. */
    public enum Severity {
        CRITICAL("Critical"),
        WARNING("Warning"),
        INFO("Info");

        private final String label;

        Severity(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    /** One problem found in the data. */
    public static class Alert {

        private final Severity severity;
        private final Student student;   // the record the problem belongs to
        private final String issue;      // short title
        private final String detail;     // explanation for the admin

        public Alert(Severity severity, Student student, String issue, String detail) {
            this.severity = severity;
            this.student = student;
            this.issue = issue;
            this.detail = detail;
        }

        public Severity getSeverity() {
            return severity;
        }

        public Student getStudent() {
            return student;
        }

        public String getIssue() {
            return issue;
        }

        public String getDetail() {
            return detail;
        }

        @Override
        public String toString() {
            String who = (student == null) ? "System" : student.getName() + " (" + student.getStudentId() + ")";
            return "[" + severity.getLabel() + "] " + who + " - " + issue;
        }
    }

    // Thresholds are named constants so they are easy to find and change.
    private static final double LOW_SCORE_THRESHOLD = 2.00;
    private static final int MINIMUM_TOTAL_WEEKS = 4;

    /**
     * Runs every rule over the whole system and returns the problems found,
     * most serious first.
     */
    public static ArrayList<Alert> checkAll(InternshipManagement manager) {
        ArrayList<Alert> alerts = new ArrayList<>();

        checkDuplicateStudentIds(manager, alerts);

        for (Student student : manager.getStudents()) {
            checkCompanyExists(manager, student, alerts);
            checkHasRatings(student, alerts);
            checkLowScore(student, alerts);
            checkEnoughWeeks(student, alerts);
            checkDecliningTrend(student, alerts);
            checkAllowanceSet(student, alerts);
        }

        sortBySeverity(alerts);
        return alerts;
    }

    // ----- individual rules -----
    /** Two students must never share a student ID. */
    private static void checkDuplicateStudentIds(InternshipManagement manager, ArrayList<Alert> alerts) {
        ArrayList<Student> students = manager.getStudents();

        for (int i = 0; i < students.size(); i++) {
            for (int j = i + 1; j < students.size(); j++) {
                if (students.get(i).getStudentId().equalsIgnoreCase(students.get(j).getStudentId())) {
                    alerts.add(new Alert(Severity.CRITICAL, students.get(j),
                            "Duplicate Student ID",
                            "The ID \"" + students.get(j).getStudentId()
                            + "\" is used by both " + students.get(i).getName()
                            + " and " + students.get(j).getName() + "."));
                }
            }
        }
    }

    /** Every student must be placed at a company that still exists. */
    private static void checkCompanyExists(InternshipManagement manager, Student student, ArrayList<Alert> alerts) {
        Company assigned = student.getAssignedCompany();

        if (assigned == null) {
            alerts.add(new Alert(Severity.CRITICAL, student,
                    "No company assigned",
                    "This student has no internship company."));
            return;
        }

        if (manager.findCompanyByName(assigned.getCompanyName()) == null) {
            alerts.add(new Alert(Severity.CRITICAL, student,
                    "Company not in system",
                    "Placed at \"" + assigned.getCompanyName()
                    + "\", which is not in the company list."));
        }
    }

    /** A student with no ratings cannot be assessed. */
    private static void checkHasRatings(Student student, ArrayList<Alert> alerts) {
        if (student.getRatings().isEmpty()) {
            alerts.add(new Alert(Severity.WARNING, student,
                    "No ratings recorded",
                    "No performance rating has been entered for this internship yet."));
        }
    }

    /** Flag students who are performing poorly overall. */
    private static void checkLowScore(Student student, ArrayList<Alert> alerts) {
        if (student.getRatings().isEmpty()) {
            return; // already reported by checkHasRatings
        }
        double score = student.calculateOverallScore();
        if (score < LOW_SCORE_THRESHOLD) {
            alerts.add(new Alert(Severity.WARNING, student,
                    "Low overall score",
                    String.format("Overall internship score is %.2f / 4.00, below the %.2f threshold.",
                            score, LOW_SCORE_THRESHOLD)));
        }
    }

    /** Too few weeks assessed means the score is not yet meaningful. */
    private static void checkEnoughWeeks(Student student, ArrayList<Alert> alerts) {
        if (student.getRatings().isEmpty()) {
            return;
        }
        int totalWeeks = 0;
        for (Student.PerformanceRating r : student.getRatings()) {
            totalWeeks += r.getWeeks();
        }
        if (totalWeeks < MINIMUM_TOTAL_WEEKS) {
            alerts.add(new Alert(Severity.INFO, student,
                    "Very few weeks assessed",
                    "Only " + totalWeeks + " week(s) assessed in total, so the overall score"
                    + " is based on very little data."));
        }
    }

    /** Compare the newest rating against the first one to spot a slide. */
    private static void checkDecliningTrend(Student student, ArrayList<Alert> alerts) {
        ArrayList<Student.PerformanceRating> ratings = student.getRatings();
        if (ratings.size() < 2) {
            return;
        }

        double first = ratings.get(0).getMarks();
        double last = ratings.get(ratings.size() - 1).getMarks();

        if (last < first) {
            alerts.add(new Alert(Severity.INFO, student,
                    "Declining performance",
                    String.format("Marks fell from %.1f in the first rating period to %.1f in the latest.",
                            first, last)));
        }
    }

    /** An allowance of zero usually means it was never filled in. */
    private static void checkAllowanceSet(Student student, ArrayList<Alert> alerts) {
        if (student.getMonthlyAllowance() <= 0) {
            alerts.add(new Alert(Severity.INFO, student,
                    "No allowance recorded",
                    "The monthly allowance for this internship is $0.00."));
        }
    }

    /**
     * Simple selection sort so the most serious alerts appear at the top.
     * Severity.ordinal() is 0 for CRITICAL, 1 for WARNING, 2 for INFO.
     */
    private static void sortBySeverity(ArrayList<Alert> alerts) {
        for (int i = 0; i < alerts.size() - 1; i++) {
            int lowest = i;
            for (int j = i + 1; j < alerts.size(); j++) {
                if (alerts.get(j).getSeverity().ordinal() < alerts.get(lowest).getSeverity().ordinal()) {
                    lowest = j;
                }
            }
            if (lowest != i) {
                Alert temp = alerts.get(i);
                alerts.set(i, alerts.get(lowest));
                alerts.set(lowest, temp);
            }
        }
    }

    /**
     * Returns the most serious severity affecting one student, or null if that
     * student has no problems. Used by the student table to tint rows.
     */
    public static Severity worstSeverityFor(Student student, ArrayList<Alert> alerts) {
        Severity worst = null;
        for (Alert a : alerts) {
            if (a.getStudent() == student) {
                if (worst == null || a.getSeverity().ordinal() < worst.ordinal()) {
                    worst = a.getSeverity();
                }
            }
        }
        return worst;
    }
}
