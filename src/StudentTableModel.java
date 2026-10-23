
/**
 * Member B:
 * Class: DIT/FT/2B/21
 * Student ID: P2523075
 * Name: Tay Yu Xiang Janston
 *
 * Advanced Feature — Student Management JTable (table model).
 *
 * A JTable does not store data itself: it asks a TableModel for it. This class
 * extends AbstractTableModel and reads straight from the shared student
 * ArrayList, so the table is always showing the live data rather than a copy.
 */
import javax.swing.table.AbstractTableModel;

public class StudentTableModel extends AbstractTableModel {

    // Column positions are named so the rest of the program never uses
    // "magic numbers" like 5 when it means the allowance column.
    public static final int COL_ID = 0;
    public static final int COL_NAME = 1;
    public static final int COL_COURSE = 2;
    public static final int COL_COMPANY = 3;
    public static final int COL_ROLE = 4;
    public static final int COL_ALLOWANCE = 5;
    public static final int COL_RATINGS = 6;
    public static final int COL_SCORE = 7;

    private static final String[] COLUMN_NAMES = {
        "Student ID", "Name", "Course", "Company", "Role", "Allowance", "Ratings", "Overall Score"
    };

    private final InternshipManagement manager;

    public StudentTableModel(InternshipManagement manager) {
        this.manager = manager;
    }

    @Override
    public int getRowCount() {
        return manager.getStudents().size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    /**
     * Telling the table the real type of each column is what makes sorting
     * correct: without this, 1200 would sort before 900 because both would be
     * compared as text.
     */
    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case COL_ALLOWANCE:
            case COL_SCORE:
                return Double.class;
            case COL_RATINGS:
                return Integer.class;
            default:
                return String.class;
        }
    }

    /** The table is a read-only view; editing is done through the Admin tab. */
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Object getValueAt(int row, int column) {
        Student student = getStudentAt(row);
        if (student == null) {
            return "";
        }

        switch (column) {
            case COL_ID:
                return student.getStudentId();
            case COL_NAME:
                return student.getName();
            case COL_COURSE:
                return student.getCourseOfStudy();
            case COL_COMPANY:
                return student.getAssignedCompany() == null
                        ? "(none)" : student.getAssignedCompany().getCompanyName();
            case COL_ROLE:
                return student.getInternshipRole();
            case COL_ALLOWANCE:
                return student.getMonthlyAllowance();
            case COL_RATINGS:
                return student.getRatings().size();
            case COL_SCORE:
                return student.calculateOverallScore();
            default:
                return "";
        }
    }

    /**
     * Returns the Student on a given MODEL row. Callers that have a screen row
     * must convert it first with JTable.convertRowIndexToModel(), because
     * sorting and filtering change the on-screen order.
     */
    public Student getStudentAt(int modelRow) {
        if (modelRow < 0 || modelRow >= manager.getStudents().size()) {
            return null;
        }
        return manager.getStudents().get(modelRow);
    }

    /** Finds the model row holding a particular student, or -1. */
    public int rowOf(Student student) {
        return manager.getStudents().indexOf(student);
    }

    /**
     * Tells the JTable that the underlying list changed and it should redraw.
     * Called after every create, delete and rating change.
     */
    public void refresh() {
        fireTableDataChanged();
    }
}
